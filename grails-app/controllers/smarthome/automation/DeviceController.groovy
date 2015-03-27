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
		int recordsTotal
		def search = QueryUtils.decorateMatchAll(deviceSearch)
		def userId = principal.id

		
		def devices = Device.createCriteria().list(this.getPagination([:])) {
			user {
				idEq(userId)
			}
			
			if (deviceSearch) {
				or {
					ilike 'label', search
					ilike 'groupe', search		
				}
			}
		}
		recordsTotal = devices.totalCount
		
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
		int recordsTotal
		def userId = principal.id
		def search = QueryUtils.decorateMatchAll(deviceSearch)
		
		def devices = Device.createCriteria().list(this.getPagination([:])) {
			user {
				idEq(userId)
			}
			
			if (deviceSearch) {
				or {
					ilike 'label', search
					ilike 'groupe', search
				}
			}
		}
		
		recordsTotal = devices.totalCount
		
		// devices est accessible depuis le model avec la variable device[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond devices, model: [recordsTotal: recordsTotal, deviceSearch: deviceSearch]
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
		model.agents = Agent.findByUser(authenticatedUser)
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
		this.preAuthorize(device)
		checkErrors(this, device)
		deviceService.saveAndSendMessage(device, null)
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
		device.validate() // important car les erreurs sont traitées lors du binding donc le device.user sorrt en erreur
		checkErrors(this, device)
		deviceService.saveAndSendMessage(device, null)
		redirect(action: COMMAND_NAME + 's')
	}
	
	
	/**
	 * Graphique des values du device
	 * 
	 * @return
	 */
	def chart(Device device, Long sinceHour) {
		this.preAuthorize(device)
		render(view: 'chart', model: [device: device, sinceHour: sinceHour])
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
}
