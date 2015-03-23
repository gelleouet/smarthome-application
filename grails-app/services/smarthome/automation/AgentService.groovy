package smarthome.automation

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;

import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class AgentService extends AbstractService {

	
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
		agent.locked = !actif
		
		if (!agent.save()) {
			throw new SmartHomeException("Erreur activation agent !", agent)
		}
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
				agentToken.dateExpiration = new Date() + 1 // 24H
				agentToken.token = UUID.randomUUID()
				
				if (!agentToken.save()) {
					throw new SmartHomeException("Erreur refresh expired token !")
				}
			}
			
			// mise à jour dernière connexion
			domainAgent.lastConnexion = new Date()
			
			if (!domainAgent.save()) {
				throw new SmartHomeException("Erreur subscribe agent !")
			}
		} else {
			// pas d'agent mais les identifiants sont bons donc on le créé auto mais 
			//en mode bloqué le temps de l'activation
			agent.lastConnexion = new Date()
			agent.locked = true
			agent.user = user
			
			if (!agent.save()) {
				throw new SmartHomeException("Erreur subscribe agent !")
			}
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
		token.save()
		agent.online = true
		agent.save()
		
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
	 * 
	 * @param message
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousMessage()
	def receiveMessage(AgentEndPointMessage message) throws SmartHomeException {
		if (message.data) {
			try {
				JSON.parse(message.data)
			} catch (Exception e) {
				throw new SmartHomeException("Erreur format JSON data !")
			}
		}
		
		return null
	}
}
