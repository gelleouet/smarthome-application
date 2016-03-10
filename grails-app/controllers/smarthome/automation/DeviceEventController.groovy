package smarthome.automation



import org.springframework.security.access.annotation.Secured;

import smarthome.automation.notification.NotificationAccountEnum;
import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.QueryUtils;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;

@Secured("isAuthenticated()")
class DeviceEventController extends AbstractController {

    private static final String COMMAND_NAME = 'deviceEvent'
	
	DeviceEventService deviceEventService
	WorkflowService workflowService
	DeviceService deviceService
	
	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Evénements", navigation = NavigationEnum.configuration, header = "Options avancées", breadcrumb = [
		NavigableAction.CONFIG_APPLICATION,
		"Domotique"
	])
	def deviceEvents(String deviceEventSearch) {
		def search = QueryUtils.decorateMatchAll(deviceEventSearch)
		
		def deviceEvents = deviceEventService.listByUser(deviceEventSearch, principal.id, this.getPagination([:])) 
		def recordsTotal = deviceEvents.totalCount

		// deviceEventInstances est accessible depuis le model avec la variable deviceEventInstance[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond deviceEvents, model: [recordsTotal: recordsTotal, deviceEventSearch: deviceEventSearch]
	}
	
	
	/**
	 * Edition
	 *
	 * @param deviceEvent
	 * @return
	 */
	def edit(DeviceEvent deviceEvent) {
		def editDeviceEvent = parseFlashCommand(COMMAND_NAME, deviceEvent)
		this.preAuthorize(deviceEvent)
		
		editDeviceEvent.notificationSms = editDeviceEvent.notifications.find({it.type == NotificationAccountEnum.sms}) != null
		editDeviceEvent.notificationMail = editDeviceEvent.notifications.find({it.type == NotificationAccountEnum.mail}) != null

		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editDeviceEvent]))
	}


	/**
	 * Création
	 *
	 * @return
	 */
	def create() {
		def editDeviceEvent = parseFlashCommand(COMMAND_NAME, new DeviceEvent())
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editDeviceEvent]))
	}
	
	
	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]
		
		model.workflows = workflowService.listByUser([:], null)
		model.devices = deviceService.listByUser(new DeviceSearchCommand(userId: principal.id))
		
		// on remplit avec les infos du user
		model << userModel
		
		return model
	}
	
	
	/**
	 * Enregistrement modification
	 *
	 * @param deviceEvent
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = DeviceEventController.COMMAND_NAME)
	def saveEdit(DeviceEvent deviceEvent) {
		this.preAuthorize(deviceEvent)
		checkErrors(this, deviceEvent)
		deviceEvent.clearNotPersistTriggers()
		deviceEventService.save(deviceEvent)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Enregistrement d'un nouveau
	 *
	 * @param deviceEvent
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "create", modelName = DeviceEventController.COMMAND_NAME)
	def saveCreate(DeviceEvent deviceEvent) {
		deviceEvent.user = authenticatedUser
		deviceEvent.validate() // important car les erreurs sont traitées lors du binding donc le deviceEvent.user sort en erreur
		checkErrors(this, deviceEvent)
		deviceEvent.clearNotPersistTriggers()
		deviceEventService.save(deviceEvent)
		redirect(action: COMMAND_NAME + 's')
	}
	
	
	/**
	 * Suppression
	 *
	 * @param deviceEvent
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "deviceEvents")
	def delete(DeviceEvent deviceEvent) {
		deviceEventService.delete(deviceEvent)
		redirect(action: COMMAND_NAME + 's')
	}
	
	
	/**
	 * Supprime un event en fonction de sa position
	 *
	 * @param device
	 * @param status
	 * @return
	 */
	def deleteTrigger(DeviceEvent deviceEvent, Integer status) {
		deviceEvent.clearNotPersistTriggers()
		deviceEvent.triggers?.removeAll {
			it.status == status
		}
		render(template: 'triggers', model: fetchModelEdit([deviceEvent: deviceEvent]))
	}

	def addTrigger(DeviceEvent deviceEvent) {
		deviceEvent.clearNotPersistTriggers()
		deviceEvent.triggers << new DeviceEventTrigger()
		render(template: 'triggers', model: fetchModelEdit([deviceEvent: deviceEvent]))
	}
	
	
	def templateTriggers(DeviceEvent deviceEvent) {
		deviceEvent.clearNotPersistTriggers()
		render(template: 'triggers', model: fetchModelEdit([deviceEvent: deviceEvent]))
	}
	
	
	def dialogNotification(DeviceEvent deviceEvent, String typeNotification) {
		this.preAuthorize(deviceEvent)
		def notification = DeviceEventNotification.findByDeviceEventAndType(deviceEvent, NotificationAccountEnum.valueOf(typeNotification))
		render (view: 'dialogNotification', model: [notification: notification, typeNotification: typeNotification,
			deviceEvent: deviceEvent])	
	}
	
	
	def saveNotification(DeviceEventNotification notification) {
		deviceEventService.saveNotification(notification)
		nop()
	}
}
