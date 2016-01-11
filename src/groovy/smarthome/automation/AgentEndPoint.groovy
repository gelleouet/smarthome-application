package smarthome.automation

import grails.converters.JSON;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
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
import org.springframework.stereotype.Component;

import smarthome.core.EndPointUtils;
import smarthome.core.SmartHomeException;

/**
 * Websocket entre l'application et un agent
 * 
 * @author gregory
 *
 */
class AgentEndPoint extends Endpoint {

	static final String URL = "/websocket"
	private static final log = LogFactory.getLog(this)
	
	// variable de classe car une instance de AgentEndPoint sera créee pour chaque connexion
	private static AgentService agentService
	
	// maintient la liste des connexions indexée avec le token de connexion
	public static Map<String, Session> sessions = new ConcurrentHashMap<String, Session>()
	
	
	/**
	 * Enregistrment du websocket
	 * 
	 * @return
	 */
	static ServerEndpointConfig register(GrailsApplication grailsApplication, ServletContext servletContext) {
		log.info "register endpoint $URL..."
		agentService = grailsApplication.mainContext.agentService
		return EndPointUtils.register(servletContext, AgentEndPoint, URL)
	}

	
	
	/**
	 * Envoi d'un message sur le bon websocket
	 * 
	 * @param token
	 * @param websocketKey
	 * @param data
	 * @throws SmartHomeException
	 */
	static void sendMessage(String token, String websocketKey, String message) throws SmartHomeException {
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
	
	

	@Override
	void onOpen(Session session, EndpointConfig config) {
		log.info "websocket onopen" 
		
		final RemoteEndpoint remote = session.getBasicRemote()
		
		session.addMessageHandler(new MessageHandler.Whole<String>() {
			public void onMessage(String text) {
				log.info "websocket onmessage : $text"
				
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
		});
	}


	@Override
	void onClose(Session session, CloseReason closeReason) {
		super.onClose(session, closeReason)
		log.info "websocket onclose"
		closeSession(session)
	}


	@Override
	void onError(Session session, Throwable throwable) {
		super.onError(session, throwable)
		log.info "websocket onerror"
		closeSession(session)
	}
	
	
	
	private def closeSession(Session session) {
		session.close()
		
		def entry = sessions.find {
			it.value == session
		}
		
		// supprime la session de la liste et déassocie le token
		if (entry) {
			def object = sessions.remove(entry.key)
			agentService.unbindWebsocket(entry.key)
			
			log.info "Closing websocket... ${object != null}. Found ${sessions.size()} connected websocket."
		}
	}
}
