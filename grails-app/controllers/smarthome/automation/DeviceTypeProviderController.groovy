package smarthome.automation



import org.springframework.security.access.annotation.Secured
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.SmartHomeException
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum
import smarthome.core.QueryUtils


@Secured("hasRole('ROLE_ADMIN')")
class DeviceTypeProviderController extends AbstractController {

	private static final String COMMAND_NAME = 'deviceTypeProvider'

	DeviceTypeProviderService deviceTypeProviderService


	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	//@NavigableAction(label = "Fournisseurs", navigation = NavigationEnum.configuration, header = "Système")
	def deviceTypeProviders(String deviceTypeProviderSearch) {
		def search = QueryUtils.decorateMatchAll(deviceTypeProviderSearch)

		def deviceTypeProviders = DeviceTypeProvider.createCriteria().list(this.getPagination([:])) {
			if (deviceTypeProviderSearch) {
				ilike('libelle', search)
			}
			join 'deviceType'
			order 'libelle'
		}
		def recordsTotal = deviceTypeProviders.totalCount

		// deviceTypeProviders est accessible depuis le model avec la variable deviceTypeProvider[]List
		// @see grails.scaffolding.templates.domainSuffix
		respond deviceTypeProviders, model: [recordsTotal: recordsTotal, deviceTypeProviderSearch: deviceTypeProviderSearch]
	}


	/**
	 * Edition
	 *
	 * @param deviceTypeProvider
	 * @return
	 */
	def edit(DeviceTypeProvider deviceTypeProvider) {
		def editDeviceTypeProvider = parseFlashCommand(COMMAND_NAME, deviceTypeProvider)
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editDeviceTypeProvider]))
	}


	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]

		model.deviceTypes = DeviceType.list()

		// on remplit avec les infos du user
		model << userModel

		return model
	}


	/**
	 * Enregistrement modification
	 *
	 * @param deviceTypeProvider
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = DeviceTypeProviderController.COMMAND_NAME)
	def save(DeviceTypeProvider deviceTypeProvider) {
		checkErrors(this, deviceTypeProvider)
		deviceTypeProviderService.save(deviceTypeProvider)
		redirect(action: 'edit', id: deviceTypeProvider.id)
	}


	/**
	 * Suppression
	 *
	 * @param deviceTypeProvider
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "deviceTypeProviders", modelName = DeviceTypeProviderController.COMMAND_NAME)
	def delete(DeviceTypeProvider deviceTypeProvider) {
		deviceTypeProviderService.delete(deviceTypeProvider)
		redirect(action: COMMAND_NAME + 's')
	}
}
