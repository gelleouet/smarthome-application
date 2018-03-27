package smarthome.endpoint

import grails.converters.JSON;

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
import smarthome.automation.Device;
import smarthome.automation.DeviceService;
import smarthome.core.EndPointUtils;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


/**
 * Websocket entre navigateur web et application pour le mode trace des compteur
 * Les données sont remontées en temps réel depuis l'agent et affichées directement sur un widget
 * Cela permet de suivre en direct la puissance consommée
 *
 * La connexion sera uni-directionnelle : on n'acceptera aucun message du websocket client
 * On ne fait que lui envoyer les trames teleinfo
 *
 * IMPORTANT : rajouter instruction dans boostrap pour enregister le endoint en mode DEV
 * EndPointUtils.register(servletContext, smarthome.endpoint.TeleinfoEndPoint)
 * 
 * IMPORTANT : rajouter une règle Spring Security URL pour autoriser l'accès
 *
 * @author gregory
 *
 */
@ServerEndpoint(value = TeleinfoEndPoint.URL, 
	configurator = smarthome.endpoint.SmarthomeEndpointConfigurator)
class TeleinfoEndPoint {
	private static final log = LogFactory.getLog(this)
	static final String URL = "/teleinfo-endpoint/{deviceId}"
	// maintient la liste des connexions indexée avec l'Id du device
	static Map<Long, Session> sessions = new ConcurrentHashMap<Long, Session>()
	
	
	@Autowired
	AgentService agentService
	
	@Autowired
	DeviceService deviceService

	
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
		
		String deviceId = session.pathParameters.deviceId
		
		if (!deviceId) {
			throw new SmartHomeException("parameter deviceId required !")
		}
		
		Device device = deviceService.assertOwnerAccess(deviceId as Long, session.userPrincipal.principal.id)
		
		Agent agent = agentService.findByDevice(device)
		
		if (!agent) {
			throw new SmartHomeException("Ce device n'est pas rattaché à un agent !")
		}
		
		try {
			agentService.sendMessage(agent, [header: 'teleinfo-start-trace', data: [mac: device.mac]])
			
			// si pas d'erreur, on stocke les infos dans properties
			session.userProperties.deviceId = device.id
			sessions.put(device.id, session)
			
			log.info "Start mode trace on teleinfo ${device.label}"
		} catch (SmartHomeException ex) {
			log.error "Envoi message start trace : ${ex.message}"
			session.close()
		}
	}
	
	
	@OnClose
	void onClose(Session session, CloseReason closeReason) {
		Long deviceId = session.userProperties.deviceId
		log.info "Session closed for device ${deviceId} : ${closeReason?.closeCode}"
		
		if (deviceId) {
			sessions.remove(deviceId)	
			
			// on prévient l'agent de terminer le mode trace
			Device device = deviceService.findById(deviceId)
			Agent agent = agentService.findByDevice(device)
			
			try {
				agentService.sendMessage(agent, [header: 'teleinfo-stop-trace', data: [mac: device.mac]])
			} catch (SmartHomeException ex) {
				log.error("Envoi message stop trace : ${ex.message}")
			}
		}
	}


	@OnError
	void onError(Session session, Throwable throwable) {
		log.error "Session error : ${throwable?.cause?.target?.message}"
	}
	
	
	/**
	 * Envoi des trames teleinfo vers le client
	 * 
	 * @param datas
	 */
	void sendMessage(Agent agent, def datas) {
		// recherche du device en fonction mac et agent
		Device device = Device.findByAgentAndMac(agent, datas.mac)
		
		if (device) {
			Session session = sessions.get(device.id)
			
			if (session) {
				if (session.isOpen()) {
					if (datas.header == "teleinfo-trace") {
						session.basicRemote.sendText(new JSON(datas.metavalues).toString(false))
					} else if (datas.header == "teleinfo-trace-stop") {
						session.close()
					}
				} else {
					sessions.remove(device.id)
				}
			}
		}
	}

}
