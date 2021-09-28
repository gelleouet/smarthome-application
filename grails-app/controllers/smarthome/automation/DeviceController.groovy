package smarthome.automation

import org.springframework.security.access.annotation.Secured

import smarthome.core.AbstractController
import smarthome.core.ChartUtils
import smarthome.core.DateUtils
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.QueryUtils
import smarthome.core.SmartHomeException
import smarthome.core.Widget
import smarthome.core.chart.GoogleChart
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum
import smarthome.security.User
import smarthome.security.UserFriendService
import smarthome.security.UserService


@Secured("isAuthenticated()")
class DeviceController extends AbstractController {

	private static final String COMMAND_NAME = 'device'

	AgentService agentService
	DeviceService deviceService
	DeviceValueService deviceValueService
	DeviceAlertService deviceAlertService
	EventService eventService
	UserFriendService userFriendService
	HouseService houseService
	DeviceUtilService deviceUtilService
	DevicePlanningService devicePlanningService



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
	@NavigableAction(label = "Mes objets", navigation = NavigationEnum.navbarPrimary)
	def devicesGrid(DeviceSearchCommand search) {
		search.userId = principal.id
		def user = authenticatedUser
		def tableauBords = deviceService.groupByTableauBord(principal.id)

		// activation favori si aucun tableau de bord
		if (!search.tableauBord) {
			search.favori = true
		}

		def devices = deviceService.listByUser(search)
		deviceService.prepareForView(devices)

		// devices est accessible depuis le model avec la variable device[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond devices, model: [user: user, search: search, tableauBords: tableauBords,
			secUser: user]
	}


	/**
	 * Les objets partagés d'un ami
	 * 
	 * @param friend
	 * @return
	 */
	def deviceShareGrid(User friend) {
		def user = authenticatedUser
		userFriendService.assertFriend(user, friend)

		def devices = deviceService.listByUser(new DeviceSearchCommand(
				userId: friend.id, userSharedId: user.id))
		deviceService.prepareForView(devices)

		render(template: 'deviceShareGrid', model: [devices: devices, user: user])
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
		if (userModel.device.id) {
			model.devicePlannings = devicePlanningService.listByDevice(userModel.device)
		} else {
			model.devicePlannings = []
		}

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
		deviceService.saveWithAssociations(device)
		redirect(action: 'devices')
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
		deviceService.syncMetadata(device, metadataName)
		nop()
	}


	/**
	 * Synchronise les plannigs vers l'agent
	 *
	 * @param device
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = DeviceController.COMMAND_NAME)
	def syncPlannings(Device device) {
		checkErrors(this, device)
		deviceService.syncPlannings(device)
		edit(device)
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
		deviceService.saveWithAssociations(device)
		redirect(action: 'devices')
	}


	/**
	 * Graphique des values du device
	 * 
	 * @return
	 */
	def deviceChart(DeviceChartCommand command) {
		def user = authenticatedUser
		deviceService.assertSharedAccess(command.device, user)

		GoogleChart chart = deviceValueService.createChart(command)
		GoogleChart compareChart

		if (command.comparePreviousYear) {
			compareChart = deviceValueService.createChart(command.cloneForLastYear())
		}

		render(view: 'deviceChart', model: [command: command, chart: chart, secUser: user,
			compareChart: compareChart])
	}


	/**
	 * Lance le calcul des données aggrégées
	 */
	def aggregateValues(DeviceChartCommand command) {
		deviceUtilService.aggregateWholeDevice(command.device.id)
		deviceChart(command)
	}


	/**
	 * Graphique comparatif pour les devices liés à la maison entre 2 users
	 * 
	 */
	def compareHouseDeviceChart(DeviceChartCommand command) {
		def user = authenticatedUser
		House house = houseService.findDefaultByUser(user)
		// recherche du device associé de l'utilisateur
		def device = house?.findSameDevice(command.device)

		if (device) {
			command.compareDevices << device
		}

		deviceChart(command)
	}


	/**
	 * Juste le graphique du device sans les barres de menu et autre
	 * 
	 * @param command
	 * @return
	 */
	def templateDeviceChart(DeviceChartCommand command) {
		def user = authenticatedUser
		deviceService.assertSharedAccess(command.device, user)

		GoogleChart chart = deviceValueService.createChart(command)

		render(template: 'deviceChart', model: [command: command, chart: chart, secUser: user,
			suffixId: params.suffixId])
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
	 * Voie de secours pour un agent pour envoyer des nouvelles valeurs
	 * sans passer par le websocket
	 * 
	 * @param device
	 * @param actionName
	 * @param applicationKey
	 * @return
	 */
	@Secured("permitAll()")
	def publicChangeValueFromAgent(MessageAgentCommand command) {
		command.publicIp = request.remoteAddr

		try {
			def agentToken = agentService.subscribe(command)

			if (agentToken) {
				deviceService.changeValueFromAgent(agentToken.agent, command.data)
				nop()
			} else {
				render(status: 400, text: 'No token !')
			}
		} catch (Exception ex) {
			log.error("Public change value : ${ex.message}")
			render(status: 400, text: ex.message)
		}
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
		deviceService.edit(deviceValue.device)
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
		deviceService.edit(deviceValue.device)
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
		deviceService.edit(deviceValue.device)

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
		deviceService.edit(device)
		render(template: 'dialogDeviceValue', model: [deviceValue: deviceValue])
	}


	/**
	 * Boite de dialogue pour ajouter valeur
	 * 
	 * @param deviceValue
	 * @return
	 */
	def dialogAddDeviceValue(Device device) {
		deviceService.edit(device)
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
	
	
	/**
	 * Affichage du widget objet connecté
	 * 
	 * @param device
	 * @param widgetSrcId
	 * @return
	 */
	def widget(Device device, long widgetSrcId) {
		User user = authenticatedUser	// inject by spring security plugin
		deviceService.prepareForView([device])
		Widget widget = Widget.read(widgetSrcId)
		render template: 'deviceView', model: [device: device, user: user, widget: widget,
			height: widget.intHeight()]
	}
}
