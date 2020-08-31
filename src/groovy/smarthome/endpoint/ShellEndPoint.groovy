package smarthome.endpoint

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import smarthome.automation.Agent;
import smarthome.automation.AgentService;
import smarthome.core.EndPointUtils;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


/**
 * Websocket entre navigateur web et application pour l'exécution d'un shell à distance 
 * sur un agent
 * Ce websocket sert de pont entre un navigateur et un agent via le 2e websocket application - agent
 *
 * Le websocket est authentifié par le token application et renvoit un token pour la suite des échanges
 * A chaque échange suivant si token invalide, la connexion est fermée
 *
 * IMPORTANT : rajouter instruction dans boostrap pour enregister le endoint en mode DEV
 * EndPointUtils.register(servletContext, smarthome.endpoint.ShellEndPoint)
 *
 * IMPORTANT : rajouter une règle Spring Security URL pour autoriser l'accès
 * 
 * @author gregory
 *
 */
//@ServerEndpoint(value = ShellEndPoint.URL, 
//	configurator = smarthome.endpoint.SmarthomeEndpointConfigurator)
class ShellEndPoint {
	private static final log = LogFactory.getLog(this)
	static final String URL = "/shell-endpoint/{agentId}"
	// maintient la liste des connexions indexée avec l'Id d'un agent
	static Map<Long, Session> sessions = new ConcurrentHashMap<Long, Session>()
	
	
	@Autowired
	AgentService agentService

	
	/**
	 * Authentification à la connexion du websocket via le applicationId et le agentId
	 * 
	 * @param session
	 * @param config
	 */
	@OnOpen
	void onOpen(Session session, EndpointConfig config) {
		if (!session.userPrincipal?.principal?.id) {
			throw new SmartHomeException("Authentification required !")
		}
		
		String agentId = session.pathParameters.agentId
		
		if (!agentId) {
			throw new SmartHomeException("agentId required !")
		}
		
		Agent agent = agentService.authorize(session.userPrincipal.principal.id, agentId as Long)
		
		agentService.sendMessage(agent, [header: 'shell', data: 'connect-shell'])
		
		// si pas d'erreur, on stocke les infos dans properties
		session.userProperties.agentId = agent.id
		sessions.put(agent.id, session)
		
		log.info "Shell connexion for agent ${agent.mac} / ${agent.libelle}"
	}
	
	
	/**
	 * Réception des messages du websocket
	 * 
	 * @param session
	 * @param text
	 */
	@OnMessage
	void onMessage(Session session, String text) {
		Long agentId = session.userProperties.agentId
		
		if (!agentId) {
			throw new SmartHomeException("Authentification not completed !")
		}
		
		// on envoi le message à l'agent
		Agent agent = agentService.findById(agentId)
		agentService.sendMessage(agent, [header: 'shell', data: text])
	}
	
	
	@OnClose
	void onClose(Session session, CloseReason closeReason) {
		Long agentId = session.userProperties.agentId
		
		if (agentId) {
			sessions.remove(agentId)	
		}
		
		log.info "Session closed for agent ${agentId} : ${closeReason?.closeCode}"
	}


	@OnError
	void onError(Session session, Throwable throwable) {
		log.error "Session error : ${throwable}"
	}
	
	
	/**
	 * Envoi d'un message shell à une console d'un agent
	 * 
	 * @param datas
	 */
	void sendMessage(Agent agent, def datas) {
		Session session = sessions.get(agent.id)
		
		if (session) {
			if (session.isOpen()) {
				session.basicRemote.sendText(datas.value)
			} else {
				sessions.remove(agent.id)
			}
		}
	}

}
