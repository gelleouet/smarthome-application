package smarthome.automation

import org.springframework.security.access.annotation.Secured;

import smarthome.core.AbstractController;
import smarthome.core.ChartUtils;
import smarthome.core.DateUtils;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.QueryUtils;
import smarthome.core.chart.GoogleChart;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;
import smarthome.security.User;


@Secured("isAuthenticated()")
class DeviceController extends AbstractController {

    private static final String COMMAND_NAME = 'device'
	
	DeviceService deviceService
	DeviceValueService deviceValueService
	DeviceAlertService deviceAlertService
	EventService eventService
	
	
	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Objets connectés", navigation = NavigationEnum.configuration, header = "Smarthome")
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
	def devicesGrid(DeviceSearchCommand search) {
		search.userId = principal.id
		def tableauBords = deviceService.groupByTableauBord(principal.id)
		
		// activation favori si aucun tableau de bord
		if (!search.tableauBord && !search.sharedDevice) {
			search.favori = true	
		}
		
		def devices = deviceService.listByUser(search)
		
		// devices est accessible depuis le model avec la variable device[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond devices, model: [user: authenticatedUser, search: search, tableauBords: tableauBords]
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
		model.user = authenticatedUser
		model.agents = Agent.findAllByUser(model.user)
		model.deviceTypes = DeviceType.list()
		model.deviceEvents = eventService.listByUser(null, model.user.id, [:])
		
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
	def saveEdit(Device device) {
		checkErrors(this, device)
		deviceService.saveWithLevelAlerts(device)
		edit(device)
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
	def saveCreate(Device device) {
		device.user = authenticatedUser
		device.validate() // important car les erreurs sont traitées lors du binding donc le device.user sort en erreur
		checkErrors(this, device)
		deviceService.saveWithLevelAlerts(device)
		edit(device)
	}
	
	
	/**
	 * Graphique des values du device
	 * 
	 * @return
	 */
	def deviceChart(DeviceChartCommand command) {
		def user = authenticatedUser
		deviceService.assertSharedAccess(command.device, user)
		
		def tableauBords = deviceService.groupByTableauBord(user.id)
		command.navigation()
		command.deviceImpl = command.device.deviceType.newDeviceType()
		
		def datas = deviceValueService.values(command)
		GoogleChart chart = command.deviceImpl.googleChart(command, datas)
		
		render(view: 'deviceChart', model: [command: command, datas: datas, tableauBords: tableauBords,
			chart: chart])
	}
	
	
	/**
	 * Exécute une action sur un device
	 * 
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "devicesGrid", modelName = "")
	def invokeAction(Device device, String actionName) {
		deviceService.assertSharedAccess(device, authenticatedUser)
		deviceService.execute(device, actionName, device.params)
		
		if (request.xhr) {
			nop()
		} else {
			redirect(action: 'devicesGrid')
		}
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
		deviceService.assertSharedAccess(device, user)
		deviceService.execute(device, actionName, device.params)
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
	
	
	/**
	 * Suppression d'une valeur
	 * 
	 * @param deviceValue
	 * @return
	 */
	def deleteDeviceValue(DeviceValue deviceValue) {
		deviceValueService.delete(deviceValue)
		nop()
	}
	
	
	/**
	 * Modification d'une valeur
	 * 
	 * @param deviceValue
	 * @return
	 */
	def saveDeviceValue(DeviceValue deviceValue) {
		deviceValueService.save(deviceValue)
		nop()
	}
	
	
	/**
	 * Ajout d'une valeur
	 * 
	 * @param deviceValue
	 * @return
	 */
	def addDeviceValue(DeviceValue deviceValue) {
		// binding datetime à la main
		deviceValue.dateValue = Date.parse(DateUtils.FORMAT_DATETIME_USER, params.dateValue)
		deviceValue.validate()
		deviceValueService.addValue(deviceValue)
		nop()
	}
	
	
	/**
	 * Boite de dialogue pour modifier valeur
	 * 
	 * @param deviceValue
	 * @return
	 */
	def dialogDeviceValue(DeviceValue deviceValue) {
		render(template: 'dialogDeviceValue', model: [deviceValue: deviceValue])	
	}
	
	
	/**
	 * Boite de dialogue pour ajouter valeur
	 * 
	 * @param deviceValue
	 * @return
	 */
	def dialogAddDeviceValue(Device device) {
		render(template: 'dialogAddDeviceValue', model: [device: device])	
	}
	
	
	/**
	 * Vue détaillée d'un device
	 * 
	 * @param device
	 * @return
	 */
	def deviceView(Device device) {
		def filActualite = deviceValueService.lastValuesByDevices([device], this.getPagination([:]))
		render(view: 'deviceView', model: [device: device, user: device.user, filActualite: filActualite])
	}
	
	
	/**
	 * Exécute une action sur un device
	 *
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "devices", modelName = "")
	def favori(Device device, boolean favori) {
		deviceService.favori(device, favori)
		
		if (request.xhr) {
			nop()
		} else {
			redirect(action: 'devices')
		}
		
	}
	
	
	/**
	 * Déplace un device sur un autre tableau de bord
	 *
	 * @param device
	 * @param tableauBord
	 * @return
	 */
	def moveToTableauBord(Device device, String tableauBord) {
		deviceService.moveToTableauBord(device, tableauBord)
		nop()
	}
	
	
	/**
	 * Déplace un device sur un autre groupe
	 *
	 * @param device
	 * @param groupe
	 * @return
	 */
	def moveToGroupe(Device device, String groupe) {
		deviceService.moveToGroupe(device, groupe)
		nop()
	}
	
	
	/**
	 * Synthèse devices
	 *
	 * @return
	 */
	def synthese() {
		def lastDevices = deviceService.listLastByUser(principal.id, 10, 7)
		def countOpenAlert = deviceAlertService.countOpenAlert(principal.id)
		render (template: 'deviceActivite', model: [lastDevices: lastDevices,
			countOpenAlert: countOpenAlert])
	}
}
