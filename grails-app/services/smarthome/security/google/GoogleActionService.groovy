package smarthome.security.google

import grails.converters.JSON;
import grails.plugin.springsecurity.SpringSecurityService;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.springframework.security.access.prepost.PreAuthorize;
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
import smarthome.security.UserApplicationService;
import smarthome.security.google.action.GoogleActionDevice;
import smarthome.security.google.action.GoogleActionExecuteResponsePayload;
import smarthome.security.google.action.GoogleActionExecution;
import smarthome.security.google.action.GoogleActionQueryResponsePayload;
import smarthome.security.google.action.GoogleActionRequest;
import smarthome.security.google.action.GoogleActionRequestCommand;
import smarthome.security.google.action.GoogleActionResponse;
import smarthome.security.google.action.GoogleActionSyncResponsePayload;


class GoogleActionService extends AbstractService {

	GrailsApplication grailsApplication
	SpringSecurityService springSecurityService
	DeviceService deviceService
	UserApplicationService userApplicationService
	
	
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
			log.error("Google auth clientId: ${command.command.client_id}")
			throw new SmartHomeException("clientId not valid !")
		}
		
		command.applicationId = grailsApplication.config.google.action.applicationId
		command.applicationName = grailsApplication.config.google.action.appName
		
		if (command.redirect_uri != "${grailsApplication.config.google.action.redirectUri}${grailsApplication.config.google.action.applicationId}" &&
			command.redirect_uri != "https://developers.google.com/oauthplayground") {
			log.error("Google auth redirectUri: ${command.redirect_uri}")
			throw new SmartHomeException("redirectUri or projectId not valid !")
		}
		
		return userApplicationService.registerApplication(user, command.applicationId, command.applicationName)
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
					log.info "Google Action Intent 'action.devices.SYNC'..."
					return syncRequest(request, response, userApp)
				} else if (request.inputs[0].intent == "action.devices.QUERY") {
					log.info "Google Action Intent 'action.devices.QUERY'..."
					return queryRequest(request, response, userApp)
				} else if (request.inputs[0].intent == "action.devices.EXECUTE") {
					log.info "Google Action Intent 'action.devices.EXECUTE'..."
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
	 * Request Sync will tell Google to make a SYNC call for any Google user with devices that have 
	 * the specified agentUserId associated with them (which you sent in the original SYNC request).
	 * All users linked to this identifier will receive a SYNC request.
	 * 
	 * @param userApplication
	 * @throws SmartHomeException
	 */
	@PreAuthorize("hasPermission(#userApplication, 'OWNER')")
	void triggerRequestSync(UserApplication userApplication) throws SmartHomeException {
		log.info "Trigger Google Action Request Sync..."
		
		try {
			URI homeGraphUri = new URIBuilder("${grailsApplication.config.google.action.homeGraphUrl}:requestSync")
				.setParameter("key", grailsApplication.config.google.action.homeGraphAPIKey)
				.build()
				
			def body = ['agent_user_id': userApplication.user.id.toString()]
			
			HttpResponse response = Request.Post(homeGraphUri)
				.addHeader("Content-type", "${ContentType.APPLICATION_JSON.mimeType}")
				.bodyString((body as JSON).toString(), ContentType.APPLICATION_JSON)
				.execute()
				.returnResponse()
			 
			if (response.statusLine.statusCode  != 200) {
				throw new SmartHomeException("Request Sync return ${response.statusLine.statusCode} code !")
			}
		} catch (Exception ex) {
			log.error "Trigger request sync : ${ex.message}"
			throw new SmartHomeException(ex)
		}
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
		// injecte le bon response payload pour la request SYNC
		GoogleActionSyncResponsePayload payload = gresponse.setSyncPayload()
		payload.agentUserId = userApp.user.id.toString()
		
		String jsonConfigKey = grailsApplication.config.google.action.jsonConfigKey
		List<Map> devices = deviceService.listForApplication(userApp.user, jsonConfigKey)
		
		// A ce stade, la liste des devices est prête à être envoyée à google home
		for (Map map : devices) {
			DeviceTypeConfig config = map.config
			Device device = map.device
			def gconfig = config.jsonData[jsonConfigKey]
			
			GoogleActionDevice gdevice = new GoogleActionDevice()
			gdevice.id = device.id.toString()
			gdevice.name.name = device.label
			gdevice.type = gconfig.type
			gdevice.traits = gconfig.traits
			gdevice.attributes = gconfig.attributes
			gdevice.willReportState = gconfig.willReportState
			
			payload.devices << gdevice
		}
		
		return gresponse
	}
		
	
	/**
	 * This intent queries for the current states of devices from the partner.
	 * It is used for queries where truly real-time accuracy is required (for example, the status of a door lock).
	 * Only states are returned when action.devices.QUERY is triggered
	 * 
	 * @param grequest
	 * @param gresponse
	 * @param userApp
	 * @return
	 * @throws SmartHomeException
	 */
	private GoogleActionResponse queryRequest(GoogleActionRequest grequest, GoogleActionResponse gresponse,
			UserApplication userApp) throws SmartHomeException {
		// injecte le bon response payload pour la request QUERY
		GoogleActionQueryResponsePayload payload = gresponse.setQueryPayload()
		String jsonConfigKey = grailsApplication.config.google.action.jsonConfigKey
		
		// renvoit le status des devices demandés en request
		for (GoogleActionDevice gdevice : grequest.inputs[0].payload.devices) {
			Device device = Device.read(gdevice.id as Long)
			DeviceTypeConfig deviceConfig = device?.deviceType?.config()?.loadJsonData()
			payload.devices[(gdevice.id)] = googleActionDeviceState(device, deviceConfig, jsonConfigKey)
		}
		
		return gresponse
	}
	
	
	/**
	 * This intent is triggered to provide commands to execute on Smart Home devices. 
	 * The new state should be provided in response if available. 
	 * One triggered intent can target multiple devices, with multiple commands. 
	 * For example, a triggered intent may set both brightness and color on a set of lights 
	 * or may set multiple lights each to a different color
	 * 
	 * @param grequest
	 * @param gresponse
	 * @param userApp
	 * @return
	 * @throws SmartHomeException
	 */
	private GoogleActionResponse executeRequest(GoogleActionRequest grequest, GoogleActionResponse gresponse,
			UserApplication userApp) throws SmartHomeException {
		// injecte le bon response payload pour la request EXECUTE
		GoogleActionExecuteResponsePayload payload = gresponse.setExecutePayload()
		String jsonConfigKey = grailsApplication.config.google.action.jsonConfigKey
		
		// exécute chaque groupe de commande
		for (GoogleActionRequestCommand gcommand : grequest.inputs[0].payload.commands) {
			// Exécution des commandes sur le groupe d'objets
			for (GoogleActionDevice gdevice : gcommand.devices) {
				Device device = Device.read(gdevice.id as Long)
				DeviceTypeConfig deviceConfig = device?.deviceType?.config()?.loadJsonData()
				String status
				
				if (device && deviceConfig) {
					// Plusieurs exécutions possibles sur un groupe d'objet
					for (GoogleActionExecution gexecution : gcommand.execution) {
						// recherche correspondance action
						def commandConfig = deviceConfig.jsonData[jsonConfigKey].commands[gexecution.command]
						
						// recherche de l'action à exécuter en fonction valeur param
						if (commandConfig) {
							// recherche du parmètre principal
							def paramValue = gexecution.params[commandConfig.param]	
							
							if (paramValue != null) {
								device.value = DeviceValue.toStringValue(paramValue)
								String action
								
								// valeur simple à injecter dans action
								if (commandConfig.action) {
									action = commandConfig.action	
								}
								// recherche d'une action en fonction d'une valeur
								else if (commandConfig.actions) {
									action = commandConfig.actions.find { it.value == paramValue }?.action
								}
								
								// A ce stade, on peut lancer la commande sur l'objet
								if (action) {
									try {
										deviceService.execute(device, action, gexecution.params)
										status = "PENDING" // car l'exécution est asynchrone
									} catch (Exception ex) {
										log.error "Google Action execute ${gexecution.command} on ${device.label} : ${ex.message}"
									}
								}
							}
						}
					}
				} // if (device && deviceConfig)
				
				payload.addCommand(status ?: "ERROR", gdevice.id)
			} // for gdevice
		} // for gcommand
		
		return gresponse
	}

	
	/**
	 * Complète l'état d'un device
	 *
	 * @param device
	 * @param gstate
	 */
	private Map googleActionDeviceState(Device device, DeviceTypeConfig deviceConfig, String jsonConfigKey) throws SmartHomeException {
		Map gstate = [:]
		Device deviceState = device?.metadata('deviceEtat') ?: device
		
		if (deviceState && deviceConfig && deviceConfig.jsonData[jsonConfigKey]) {
			def states = deviceConfig.jsonData[jsonConfigKey].states
			def value = DeviceValue.parseDoubleValue(deviceState.value)
			gstate.online = true
			
			states?.each { state ->
				def stateValue
				
				if (state.type == 'Boolean') {
					stateValue = (value ? true : false)
				} else if (state.type == 'Integer') {
					stateValue = value as Integer
				} else {
					stateValue = value
				}
				
				gstate[(state.name)] = stateValue
			}
		}
		
		if (!gstate) {
			gstate.online = false
			gstate.errorCode = "deviceOffline"
		}
		
		return gstate
	}
}
