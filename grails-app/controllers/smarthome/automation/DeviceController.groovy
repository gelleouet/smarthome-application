package smarthome.automation

import org.springframework.security.access.annotation.Secured;

import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.QueryUtils;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;

@Secured("isAuthenticated()")
class DeviceController extends AbstractController {

    private static final String COMMAND_NAME = 'device'
	
	DeviceService deviceService
	WorkflowService workflowService
	
	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Périphériques", defaultGroup = true, navigation = NavigationEnum.configuration, header = "Installation", breadcrumb = [
		NavigableAction.CONFIG_APPLICATION,
		"Domotique"
	])
	def devices(String deviceSearch) {
		def devices = deviceService.listByUser(this.getPagination([:]), deviceSearch, false)
		def recordsTotal = devices.totalCount
		
		// devices est accessible depuis le model avec la variable device[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond devices, model: [recordsTotal: recordsTotal, deviceSearch: deviceSearch]
	}
	
	
	
	/**
	 * Tous les devices organisés par groupe sous forme de grille
	 * 
	 * @return
	 */
	@NavigableAction(label = "Périphériques", navigation = NavigationEnum.navbarPrimary)
	def devicesGrid(String deviceSearch) {
		def devices = deviceService.listByUser([:], deviceSearch, true)
		
		// devices est accessible depuis le model avec la variable device[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond devices, model: [deviceSearch: deviceSearch]
	}
	
	
	/**
	 * Edition
	 *
	 * @param device
	 * @return
	 */
	def edit(Device device) {
		def editDevice = parseFlashCommand(COMMAND_NAME, device)
		this.preAuthorize(device)
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editDevice]))
	}


	/**
	 * Création
	 *
	 * @return
	 */
	def create() {
		def editDevice = parseFlashCommand(COMMAND_NAME, new Device())
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editDevice]))
	}
	
	
	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]
		
		// Compléter le model
		model.agents = Agent.findAllByUser(authenticatedUser)
		model.deviceTypes = DeviceType.list()
		
		// on remplit avec les infos du user
		model << userModel
		
		return fetchModelEvent(model)
	}
	
	
	def fetchModelEvent(model) {
		model.workflows = workflowService.listByUser([:], null)
		model.devices = deviceService.listByUser([:], null, false)
		return model
	}
	
	
	/**
	 * Enregistrement modification
	 *
	 * @param device
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = DeviceController.COMMAND_NAME)
	def saveEdit(Device device) {
		this.preAuthorize(device)
		checkErrors(this, device)
		deviceService.saveWithEvents(device)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Enregistrement d'un nouveau
	 *
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "create", modelName = DeviceController.COMMAND_NAME)
	def saveCreate(Device device) {
		device.user = authenticatedUser
		device.validate() // important car les erreurs sont traitées lors du binding donc le device.user sort en erreur
		checkErrors(this, device)
		deviceService.saveWithEvents(device)
		redirect(action: COMMAND_NAME + 's')
	}
	
	
	/**
	 * Graphique des values du device
	 * 
	 * @return
	 */
	def chartView(Device device, Long sinceHour) {
		sinceHour = sinceHour ?: 1
		this.preAuthorize(device)
		def datas = deviceService.values(device, sinceHour, null)
		def chartType = device.defaultChartType()
		def timesAgo = [1: '1 heure', 6: '6 heures', 12: '12 heures', 24: '24 heures']
		render(view: 'chartView', model: [device: device, sinceHour: sinceHour, chartType: chartType, datas: datas, timesAgo: timesAgo])
	}
	
	
	/**
	 * Exécute une action sur un device
	 * 
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "devicesGrid", modelName = "")
	def invokeAction(Device device, String actionName) {
		this.preAuthorize(device)
		deviceService.invokeAction(device, actionName)
		redirect(action: 'devicesGrid')
	}
	
	
	/**
	 * Exécute une action sur un device
	 *
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "devices", modelName = "")
	def delete(Device device) {
		this.preAuthorize(device)
		deviceService.delete(device)
		redirect(action: 'devices')
	}
	
	
	/**
	 * Supprime un event en fonction de sa position
	 * 
	 * @param device
	 * @param status
	 * @return
	 */
	def deleteEvent(Device device, Integer status) {
		device.clearNotPersistEvents()
		device.events?.removeAll {
			it.status == status
		}
		render(template: 'events', model: fetchModelEvent([device: device]))
	}

		def addEvent(Device device) {
		device.clearNotPersistEvents()
		device.events << new DeviceEvent()
		render(template: 'events', model: fetchModelEvent([device: device]))
	}
	
	
	def templateEvents(Device device) {
		device.clearNotPersistEvents()
		render(template: 'events', model: fetchModelEvent([device: device]))
	}
}
