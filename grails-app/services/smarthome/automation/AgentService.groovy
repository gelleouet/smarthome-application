package smarthome.automation

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;

import org.codehaus.groovy.grails.web.mapping.LinkGenerator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.ClassUtils;
import smarthome.core.ExchangeType;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class AgentService extends AbstractService {

	// auto inject
	LinkGenerator grailsLinkGenerator
	
	// auto inject
	def grailsApplication
	
	/**
	 * Activation d'un agent pour qu'il se puisse se connecter au websocket
	 * 
	 * @param agent
	 * @param actif
	 * @return
	 * 
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def activer(Agent agent, boolean actif) throws SmartHomeException {
		log.info("agent ${agent.mac} activation : ${actif}")
		agent.locked = !actif
		
		if (!agent.save()) {
			throw new SmartHomeException("Erreur activation agent !", agent)
		}
	}
	
	
	/**
	 * Démarre l'inclusion automatique de nouveaux devices sur un agent
	 * 
	 * @param agent
	 * @return
	 * @throws SmartHomeException
	 */
	def startInclusion(Agent agent) throws SmartHomeException {
		if (agent.locked) {
			throw new SmartHomeException("L'agent ${agent.libelle} n'est pas activé !", agent)
		}
		
		if (!agent.online) {
			throw new SmartHomeException("L'agent ${agent.libelle} n'est pas connecté !", agent)
		}
		
		this.sendMessage(agent, [header: 'startInclusion'])
	}
	
	
	/**
	 * Demande connexion au websocket
	 * 
	 * @param mac
	 * @param username
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def subscribe(Agent agent, String username, String applicationKey) throws SmartHomeException {
		log.info("agent subscribe for user ${username}")
		
		// recherche user
		def user = User.findByUsername(username)
		
		if (!user) {
			throw new SmartHomeException("User not found !")
		}
		
		if (user.applicationKey != applicationKey) {
			throw new SmartHomeException("Application key not valid !")
		}
		
		// recherche d'un agent en fonction mac
		def domainAgent = Agent.findByMacAndUser(agent.mac, user)
		def agentToken = null
		
		if (domainAgent) {
			// l'agent doit être activé
			if (domainAgent.locked) {
				throw new SmartHomeException("Agent not activated !")
			}
			
			// recherche d'un token non expiré
			if (! domainAgent.tokens.empty) {
				agentToken = domainAgent.tokens[0]
			} else {
				agentToken = new AgentToken(agent: domainAgent)
			}
			
			if (agentToken.hasExpired()) {
				agentToken.refreshToken()
				
				if (!agentToken.save()) {
					throw new SmartHomeException("Erreur refresh expired token !")
				}
			}
			
			// mise à jour dernière connexion et des IP
			domainAgent.lastConnexion = new Date()
			domainAgent.privateIp = agent.privateIp
			domainAgent.publicIp = agent.publicIp
			
			if (!domainAgent.save()) {
				throw new SmartHomeException("Erreur subscribe agent !")
			}
		} else {
			// pas d'agent mais les identifiants sont bons donc on le créé auto mais 
			//en mode bloqué le temps de l'activation par le user
			agent.lastConnexion = new Date()
			agent.locked = true
			agent.user = user
			
			if (!agent.save()) {
				throw new SmartHomeException("Auto-created agent not activated !")
			}
		}
		
		// on y glisse l'url du websocket en gérant le SSL ou pas
		def urlApplication = grailsLinkGenerator.link(uri: AgentEndPoint.URL, absolute: true)
		
		if (urlApplication.startsWith('https')) {
			agentToken.websocketUrl = urlApplication.replace('https', 'wss')
		} else {
			agentToken.websocketUrl = urlApplication.replace('http', 'ws')
		}
		
		return agentToken
	}
	
	
	/**
	 * Associe une session websocket à un token d'agent
	 * 
	 * @param websocketId
	 * @param message
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def bindWebsocket(String websocketId, AgentEndPointMessage message) throws SmartHomeException {
		log.info "Bind websocket ${message.username}"
		
		// recherche utilisateur avec paire username, applicationKey
		def user = User.findByUsernameAndApplicationKey(message.username, message.applicationKey)
		
		if (!user) {
			throw new SmartHomeException("Utilisateur non valide !")
		}
		
		def agent = Agent.findByMacAndUser(message.mac, user)
		
		if (!agent) {
			throw new SmartHomeException("Agent non valide !")
		}
		
		if (agent.locked) {
			throw new SmartHomeException("Agent verrouillé !")
		}
		
		// recherche token
		def token = AgentToken.find {
			token == message.token && agent == agent 
		}
		
		if (!token) {
			throw new SmartHomeException("Token non valide !")
		}
		
		if (token.hasExpired()) {
			throw new SmartHomeException("Token has expired !")
		}
		
		// associe le websocket au token et rend l'agent online
		token.websocketKey = websocketId
		def serverId = grailsApplication.config.smarthome.cluster.serverId
		
		if (!serverId) {
			throw new SmartHomeException("smarthome.cluster.serverId property must be set !")
		}
		
		token.serverId = serverId
		
		if (!token.save()) {
			throw new SmartHomeException("Can't bind websocket : ${token.errors}")
		}
		
		agent.online = true
		
		if (!agent.save()) {
			throw new SmartHomeException("Can't bind websocket : ${agent.errors}")
		}
		
		return token
	}
	
	
	/**
	 * 
	 * @param websocketId
	 * @param token
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def unbindWebsocket(String token) throws SmartHomeException {
		log.info "Unbind websocket ${token}"
		
		def agentToken = AgentToken.findByToken(token)
		
		if (!agentToken) {
			throw new SmartHomeException("Token not found !")
		}
		
		agentToken.delete()
		
		def agent = agentToken.agent
		agent.online = false
		agent.save()
	}
	
	
	/**
	 * Réception d'un message. Ce service ne fait presque rien à part vérifier le bon format des datas.
	 * Le système de message asynchrone avec des règles de routage est utilisé pour lancer le bon service en fonction des datas
	 * Il authentifie l'agent pour éviter de le faire dans les autres services 
	 * 
	 * @param message
	 * @param agentToken
	 * 
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousMessage()
	def receiveMessage(AgentEndPointMessage message, AgentToken agentToken) throws SmartHomeException {
		log.info "Receive message from agent token ${agentToken.token}..."
		
		if (! message.data) {
			throw new SmartHomeException("Data is empty !")
		}
		
		if (agentToken.hasExpired()) {
			throw new SmartHomeException("Token has expired !")
		}
		
		agentToken.agent
	}
	
	
	
	/**
	 * Envoi d'un message à l'agent en passant par le websocket. Les messages doivent être dirigés
	 * sur le bon serveur dans un environnement clusterisé car seul un serveur est connecté au websocket
	 * 
	 * @param agent
	 * @param message
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	def sendMessage(Agent agent, Map data) throws SmartHomeException {
		log.info "send message to agent ${agent.mac}"
		
		if (!agent.attached) {
			agent.attach()
		}
		
		if (!agent.tokens.size()) {
			throw new SmartHomeException("Agent not subscribe !")
		}
		
		def token = agent.tokens[0]
		
		if (!token.serverId || !token.websocketKey) {
			throw new SmartHomeException("Websocket not bind !")
		}
		
		if (token.hasExpired()) {
			throw new SmartHomeException("Token has expired !")
		}
		
		// prépare le message avec les infos de connexion pour permette aussi à l'agent d'authentifier les messages recus
		AgentEndPointMessage message = new AgentEndPointMessage(mac: agent.mac, token: token.token, 
			username: agent.user.username, applicationKey: agent.user.applicationKey, data: data, websocketKey: token.websocketKey)
		
		// il faut envoyer le message au bon serveur dans la bonne Queue 
		// on se sert du serverId qu'on passe en routingKey
		// Seul le bon serveur ayant le websocket va recevoir le message à traiter
		this.sendAsynchronousMessage("amq.direct", ClassUtils.prefixAMQ(this) + '.sendMessage.' + token.serverId, message, ExchangeType.DIRECT)
	}
	
	
	/**
	 * Méthode bas niveau pour envoyer un message à l'agent. cette méthode ne doit pas être appelée directement (utiliser sendMessage)
	 * Cette méthode doit être appelée sur le bon serveur ayant le websocket
	 * 
	 * 
	 * @param token
	 * @param websocketKey
	 * @param message
	 * @return
	 * @throws SmartHomeException
	 */
	def sendMessageToWebsocket(String token, String websocketKey, String message) throws SmartHomeException {
		log.info "Send message to websocket token ${token}"
		AgentEndPoint.sendMessage(token, websocketKey, message)
	}
	
	
	/**
	 * Utile pour les environnements sans session hibernate automatique
	 * Ex : Camel ESB
	 * 
	 * @param id
	 * @return
	 */
	def findById(Serializable id) {
		Agent.get(id)
	}
	
	
	/**
	 * Utile pour les environnements sans session hibernate automatique
	 * Ex : Camel ESB
	 *
	 * @param device
	 * @return
	 */
	def findByDevice(Device device) {
		if (!device.attached) {
			device.attach()
		}
		
		return device.agent
	}
}
