package smarthome.automation



import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.QueryUtils
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum

import org.springframework.security.access.annotation.Secured

@Secured("hasRole('ROLE_ADMIN')")
class DeviceTypeController extends AbstractController {

	private static final String COMMAND_NAME = 'deviceType'

	DeviceTypeService deviceTypeService

	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Catalogue", navigation = NavigationEnum.configuration, header = "Système")
	def deviceTypes(String deviceTypeSearch) {
		def deviceTypes
		int recordsTotal
		def pagination = this.getPagination([:])

		if (deviceTypeSearch) {
			def search = QueryUtils.decorateMatchAll(deviceTypeSearch)

			def query = DeviceType.where {
				libelle =~ search
			}
			deviceTypes = query.list(pagination)
			recordsTotal = query.count()
		} else {
			recordsTotal = DeviceType.count()
			deviceTypes = DeviceType.list(pagination)
		}

		// deviceTypes est accessible depuis le model avec la variable deviceType[]List
		// @see grails.scaffolding.templates.domainSuffix
		respond deviceTypes, model: [recordsTotal: recordsTotal, deviceTypeSearch: deviceTypeSearch]
	}


	/**
	 * Edition
	 *
	 * @param deviceType
	 * @return
	 */
	def edit(DeviceType deviceType) {
		def editDeviceType = parseFlashCommand(COMMAND_NAME, deviceType)
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editDeviceType]))
	}


	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]

		model.deviceTypeConfig = userModel.deviceType?.config()

		// on remplit avec les infos du user
		model << userModel

		return model
	}


	/**
	 * Enregistrement modification
	 *
	 * @param deviceType
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = DeviceTypeController.COMMAND_NAME)
	def save(DeviceType deviceType) {
		checkErrors(this, deviceType)
		deviceTypeService.save(deviceType, params.configuration)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * 
	 * @param implClass
	 * @return
	 */
	def templateMetadataForm(DeviceType deviceType) {
		def device = params.deviceId ? Device.get(params.deviceId) : null
		render(template: deviceType.newDeviceType().viewForm(), model: [device: device,
			user: authenticatedUser])
	}
}
