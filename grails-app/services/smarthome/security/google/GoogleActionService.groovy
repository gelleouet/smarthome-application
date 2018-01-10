package smarthome.security.google

import grails.plugin.springsecurity.SpringSecurityService;

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.springframework.transaction.annotation.Transactional;

import smarthome.automation.Device;
import smarthome.automation.DeviceService;
import smarthome.automation.DeviceType;
import smarthome.automation.DeviceTypeConfig;
import smarthome.automation.DeviceValue;
import smarthome.core.AbstractService;
import smarthome.core.SmartHomeException;
import smarthome.security.User;
import smarthome.security.UserApplication;
import smarthome.security.google.action.GoogleActionDevice;
import smarthome.security.google.action.GoogleActionQueryResponsePayload;
import smarthome.security.google.action.GoogleActionRequest;
import smarthome.security.google.action.GoogleActionResponse;
import smarthome.security.google.action.GoogleActionSyncResponsePayload;


class GoogleActionService extends AbstractService {

	GrailsApplication grailsApplication
	SpringSecurityService springSecurityService
	DeviceService deviceService
	
	
	/**
	 * Enregistrement d'une nouvelle application pour un utilisateur
	 * 
	 * @param command
	 * @param user
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	UserApplication auth(GoogleActionAuthCommand command, User user) throws SmartHomeException {
		// Vérifie les infos de connexion
		if (command.client_id != grailsApplication.config.google.action.clientId) {
			throw new SmartHomeException("clientId not valid !")
		}
		
		command.applicationId = grailsApplication.config.google.action.applicationId
		
		if (command.redirect_uri != "${grailsApplication.config.google.action.redirectUri}${grailsApplication.config.google.action.applicationId}") {
			throw new SmartHomeException("redirectUri or projectId not valid !")
		}
		
		UserApplication userApp = UserApplication.findByUserAndApplicationId(user,
			command.applicationId)
		
		if (!userApp) {
			userApp = new UserApplication(user: user, applicationId: command.applicationId)
		}
		
		// rafraichit le token à chaque auth
		userApp.publicToken = UUID.randomUUID().toString().bytes.encodeBase64().toString()
		userApp.token = userApp.publicToken //springSecurityService.encodePassword(userApp.publicToken)
		userApp.name = grailsApplication.config.google.action.appName
		
		return this.save(userApp)
	}
	
	
	/**
	 * Gestion d'une conversation avec un assistant
	 * L'utilisateur doit être connecté pendant toute la conversation
	 * 
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	GoogleActionResponse conversation(GoogleActionRequest request, String token) throws SmartHomeException {
		GoogleActionResponse response = new GoogleActionResponse()
		response.requestId = request.requestId
		
		if (!token) {
			log.error("accessToken is empty !")
			return response.error("authFailure")
		}
		
		if (!token.contains("Bearer")) {
			log.error("accessToken not valid. only Bearer token ! (${token})")
			return response.error("authFailure")
		}
		
		String userToken = token.replace("Bearer", "").trim()
		
		UserApplication userApp = UserApplication.findByToken(userToken)
		
		if (!userApp) {
			log.error("accessToken not found ! (${token})")
			return response.error("authFailure")
		}
		
		if (request.inputs) {
			try {
				if (request.inputs[0].intent == "action.devices.SYNC") {
					return syncRequest(request, response, userApp)
				} else if (request.inputs[0].intent == "action.devices.QUERY") {
					return queryRequest(request, response, userApp)
				} else if (request.inputs[0].intent == "action.devices.EXECUTE") {
					return executeRequest(request, response, userApp)
				}
			} catch (SmartHomeException ex) {
				log.error("${request.inputs[0].intent} : ${ex.message}")
				return response.error("protocolError")
			}
		}
		
		return response.error("notSupported")
	}
	
	
	/**
	 * This intent is triggered at user setup or when a user reconnects through the app (to reconnect or disconnect)
	 * when necessary to resync devices in batch (for example, when new traits are added).
	 * 
	 * Charge tous les devices du user qui sont soit en favori, soit affichés sur un tableau de bord
	 * car se sont les seuls qui l'utilisateur peut piloter.
	 * En fonction de la config de l'implémentation du device, les devices sont encore filtrés si aucune 
	 * correspondance avec google home est trouvée
	 * 
	 * @param request
	 * @param response
	 * @param userApp
	 * @return
	 * @throws SmartHomeException
	 */
	private GoogleActionResponse syncRequest(GoogleActionRequest grequest, GoogleActionResponse gresponse,
		UserApplication userApp) throws SmartHomeException {
		// injecte le bon response payload pour la request Sync
		GoogleActionSyncResponsePayload payload = gresponse.setSyncPayload()
		payload.agentUserId = userApp.user.id.toString()
		
		String jsonConfigKey = grailsApplication.config.google.action.jsonConfigKey
		List<Map> devices = deviceService.listForApplication(userApp.user, jsonConfigKey)
		
		// A ce stade, la liste des devices est prête à être envoyée à google home
		for (Map map : devices) {
			DeviceTypeConfig config = map.config
			Device device = map.device
			
			GoogleActionDevice gdevice = new GoogleActionDevice(config.jsonData[jsonConfigKey])
			gdevice.id = device.id.toString()
			gdevice.name.name = device.label
			
			payload.devices << gdevice
		}
		
		return gresponse
	}
		
		
	private GoogleActionResponse queryRequest(GoogleActionRequest grequest, GoogleActionResponse gresponse,
			UserApplication userApp) throws SmartHomeException {
		// injecte le bon response payload pour la request Sync
		GoogleActionQueryResponsePayload payload = gresponse.setQueryPayload()
		String jsonConfigKey = grailsApplication.config.google.action.jsonConfigKey
		
		// renvoit le status des devices demandés en request
		for (GoogleActionDevice gdevice : grequest.inputs[0].payload.devices) {
			Map gdeviceResponse = [:]
			Device device = Device.read(gdevice.id as Long)
			
			if (device) {
				DeviceTypeConfig deviceConfig = device.deviceType.config()?.loadJsonData()
				
				if (deviceConfig) {
					def states = deviceConfig.jsonData[jsonConfigKey].states
					def value = DeviceValue.parseDoubleValue(device.value)
					gdeviceResponse.online = true
					
					states?.each { state ->
						def stateValue
						
						if (state.type == 'Boolean') {
							stateValue = (value ? true : false)
						} else if (state.type == 'Integer') {
							stateValue = value as Integer
						} else {
							stateValue = value
						}
						
						gdeviceResponse[(state.name)] = true
					}
				}
			}
			
			if (!gdeviceResponse) {
				gdeviceResponse.online = false
				gdeviceResponse.errorCode = "deviceOffline"
			}
			
			payload.devices[(gdevice.id)] = gdeviceResponse
		}
		
		return gresponse
	}
			
			
	private GoogleActionResponse executeRequest(GoogleActionRequest grequest, GoogleActionResponse gresponse,
			UserApplication userApp) throws SmartHomeException {
		return gresponse
	}
			
}
