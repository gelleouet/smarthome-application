package smarthome.endpoint

import grails.converters.JSON;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.websocket.ClientEndpointConfig.Configurator;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smarthome.automation.AgentService;
import smarthome.automation.AgentToken;
import smarthome.core.EndPointUtils;
import smarthome.core.SmartHomeException;

/**
 * Websocket entre l'application et un agent
 * 
 * IMPORTANT : rajouter instruction dans boostrap pour enregister le endoint en mode DEV
 * EndPointUtils.register(servletContext, smarthome.endpoint.AgentEndPoint)
 * 
 * IMPORTANT : rajouter une règle Spring Security URL pour autoriser l'accès
 * 
 * @author gregory
 *
 */
@ServerEndpoint(value = AgentEndPoint.URL,
	configurator = smarthome.endpoint.SmarthomeEndpointConfigurator)
class AgentEndPoint {

	private static final log = LogFactory.getLog(this)
	static final String URL = "/websocket"
	// maintient la liste des connexions indexée avec le token de connexion
	static Map<String, Session> sessions = new ConcurrentHashMap<String, Session>()
	
	@Autowired
	AgentService agentService
	
	
	/**
	 * Envoi d'un message sur le bon websocket
	 * 
	 * @param token
	 * @param websocketKey
	 * @param data
	 * @throws SmartHomeException
	 */
	void sendMessage(String token, String websocketKey, String message) throws SmartHomeException {
		if (!token || !websocketKey || ! message) {
			throw new SmartHomeException("sendMessage parameters must be set !")
		}
		
		def session = sessions.get(token)
		
		// recherche session en fonction token
		if (!session) {
			throw new SmartHomeException("Session not found for this token !")
		}
		
		// vérifie les ID sessions
		if (websocketKey != session.getId()) {
			throw new SmartHomeException("Session ID is not compatible !")
		}
		
		// on est clean pour envoyer le message
		if (! session.isOpen()) {
			throw new SmartHomeException("Session is already close !")
		}
		
		session.getBasicRemote().sendText(message);
	}
	
	

	@OnOpen
	void onOpen(Session session, EndpointConfig config) {
		log.info "websocket onopen" 
	}
	
	
	@OnMessage
	void onMessage(Session session, String text) {
		// conversion en JSON et traitement du message
		try {
			AgentEndPointMessage message = AgentEndPointMessage.newInstance(JSON.parse(text))
			
			if (message.token) {
				// 1ere connexion, on bind le websocket à l'agent
				if (! session.userProperties.token) {
					def token = agentService.bindWebsocket(session.getId(), message)
					session.userProperties.token = token
					sessions.put(token.token, session)
					log.info "Bind ${sessions.size()} websockets"
				} else {
					// tout est ok pour traiter le message.
					// on vérifie quand même le token envoyé avec celui de la session
					AgentToken agentToken = session.userProperties.token
					
					def entry = sessions.find {
						it.value == session
					}
					
					if (entry?.key != agentToken.token || message.token != agentToken.token) {
						throw new Exception("Session incompatible avec le token !");
					}
					
					agentService.receiveMessage(message, agentToken)
					
					// ferme la session si le token a expiré
					// On traite quand même le message (sinon perte info) donc on lance l'erreur
					// après la réception du message
					if (agentToken.hasExpired()) {
						throw new SmartHomeException("Token has expired !")
					}
				}
			} else {
				// session est fermée direct car pas de token
				log.error("Websocket token is empty !")
				closeSession(session)
			}
		} catch (Exception e) {
			log.error("Websocket error !", e)
			closeSession(session)
		}
	}


	@OnClose
	void onClose(Session session, CloseReason closeReason) {
		log.info "websocket onclose"
		closeSession(session)
	}


	@OnError
	void onError(Session session, Throwable throwable) {
		log.error "websocket onerror"
		closeSession(session)
	}
	
	
	
	private def closeSession(Session session) {
		def entry = sessions.find {
			it.value == session
		}
		
		try {
			session.close()
		} catch (Exception e) {}
		
		// supprime la session de la liste et déassocie le token
		if (entry) {
			def object = sessions.remove(entry.key)
			agentService.unbindWebsocket(entry.key)
			
			log.info "Closing websocket... ${object != null}. Found ${sessions.size()} connected websocket."
		}
	}
}
