package smarthome.automation

import org.springframework.security.access.annotation.Secured;

import smarthome.core.AbstractController;
import smarthome.core.DateUtils;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.QueryUtils;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;
import smarthome.security.User;

@Secured("isAuthenticated()")
class DeviceController extends AbstractController {

    private static final String COMMAND_NAME = 'device'
	
	DeviceService deviceService
	ChartService chartService
	
	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Objets", defaultGroup = true, navigation = NavigationEnum.configuration, header = "Configuration", breadcrumb = [
		NavigableAction.CONFIG_APPLICATION,
		"Général"
	])
	def devices(String deviceSearch) {
		def devices = deviceService.listByUser(new DeviceSearchCommand(pagination: this.getPagination([:]), 
			search: deviceSearch, userId: principal.id))
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
	@NavigableAction(label = "Objets", navigation = NavigationEnum.navbarPrimary)
	def devicesGrid(String deviceSearch) {
		def devices = deviceService.listByUser(new DeviceSearchCommand(search: deviceSearch, 
			filterShow: true, userId: principal.id, sharedDevice: true))
		
		// devices est accessible depuis le model avec la variable device[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond devices, model: [deviceSearch: deviceSearch, user: authenticatedUser]
	}
	
	
	/**
	 * Edition
	 *
	 * @param device
	 * @return
	 */
	def edit(Device device) {
		def editDevice = parseFlashCommand(COMMAND_NAME, device)
		editDevice = deviceService.edit(editDevice)
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
		checkErrors(this, device)
		deviceService.save(device)
		redirect(action: COMMAND_NAME + 's')
	}

	
	/**
	 * Changement d'une métadata sur le device avec envoi à l'agent
	 *
	 * @param device
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = DeviceController.COMMAND_NAME)
	def changeMetadata(Device device, String metadataName) {
		checkErrors(this, device)
		deviceService.changeMetadata(device, metadataName)
		nop()
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
		deviceService.save(device)
		redirect(action: COMMAND_NAME + 's')
	}
	
	
	/**
	 * Graphique des values du device
	 * 
	 * @return
	 */
	def chartView(Device device, Long sinceHour, Long offsetHour) {
		deviceService.edit(device)
		sinceHour = sinceHour ?: chartService.defaultTimeAgo()
		offsetHour = offsetHour ?: 0
		
		if (params.offsetStep == 'prev') {
			offsetHour++
		} else if (params.offsetStep == 'next' && offsetHour > 0) {
			offsetHour--
		}
		
		// calcul plage date
		def period = DateUtils.durationToDates(sinceHour, offsetHour)
		def datas = deviceService.values(device, period.start, period.end, null)
		
		def chartType = device.deviceType.newDeviceType().defaultChartType()
		def timesAgo = chartService.timesAgo()
		render(view: 'chartView', model: [device: device, sinceHour: sinceHour, 
			chartType: chartType, datas: datas, timesAgo: timesAgo,
			offsetHour: offsetHour, startDate: period.start, endDate: period.end])
	}
	
	
	/**
	 * Exécute une action sur un device
	 * 
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "devicesGrid", modelName = "")
	def invokeAction(Device device, String actionName) {
		deviceService.assertInvokeAction(device, authenticatedUser, actionName)
		deviceService.invokeAction(device, actionName)
		redirect(action: 'devicesGrid')
	}
	
	
	/**
	 * Exécute une action sur un device
	 * 
	 * @return
	 */
	@Secured("permitAll()")
	@ExceptionNavigationHandler(actionName = "devicesGrid", modelName = "")
	def publicInvokeAction(Device device, String actionName, String applicationKey) {
		User user = User.findByApplicationKey(applicationKey)
		deviceService.assertInvokeAction(device, user, actionName)
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
		deviceService.delete(device)
		redirect(action: 'devices')
	}
	
	
	def deviceView(Device device) {
		render(view: 'deviceView', model: [device: device, user: device.user])
	}
}
