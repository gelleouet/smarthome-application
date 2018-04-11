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
					AgentToken agentToken = agentService.bindWebsocket(session.getId(), message)
					
					// on conserve le token dans la session et il sert d'index
					// dans la map des sessions
					session.userProperties.token = agentToken.token
					sessions.put(agentToken.token, session)
				} else {
					// vérifie que le message correspond bien à la session
					if (message.token != session.userProperties.token) {
						throw new Exception("Session incompatible avec le token !")
					}
					
					// Vérifie que la session n'a pas expirée ou fantôme : elle est rattachée
					// à un token qui n'existe plus
					AgentToken agentToken = agentService.findAgentToken(session.userProperties.token)
					
					if (!agentToken) {
						throw new Exception("Session expirée : token n'existe plus !")
					}

					// tout est ok pour traiter le message					
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
	
	
	/**
	 * Fermeture d'une session : ferme la connexion
	 * La supprime de la liste globale des sessions
	 * et marque l'agent comme déconnecté
	 * 
	 * @param session
	 */
	private void closeSession(Session session) {
		String token = session.userProperties.token
		
		try {
			session.close()
		} catch (Exception e) {}
		
		// supprime la session de la liste et déassocie le token
		// s'il existe (seulement si la connexion avait été acceptée à l'ouverture)
		if (token) {
			sessions.remove(token)
			agentService.unbindWebsocket(token)
		}
	}
}
