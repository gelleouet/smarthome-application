package smarthome.core



import org.springframework.security.access.annotation.Secured

import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.SmartHomeException
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum


@Secured("hasRole('ROLE_ADMIN')")
class ConfigController extends AbstractController {

	private static final String COMMAND_NAME = 'config'

	ConfigService configService


	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Configuration", navigation = NavigationEnum.configuration,
	header = "Système")
	def configs(ConfigCommand command) {
		def configs = configService.list(command, this.getPagination([:]))
		def recordsTotal = configs.totalCount

		// configs est accessible depuis le model avec la variable config[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond configs, model: [recordsTotal: recordsTotal, command: command]
	}


	/**
	 * Edition
	 *
	 * @param config
	 * @return
	 */
	def edit(Config config) {
		def editConfig = parseFlashCommand(COMMAND_NAME, config)
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editConfig]))
	}


	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]

		// TODO Compléter le model
		// model.toto = 'toto'

		// on remplit avec les infos du user
		model << userModel

		return model
	}


	/**
	 * Enregistrement modification
	 *
	 * @param config
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = ConfigController.COMMAND_NAME)
	def save(Config config) {
		checkErrors(this, config)
		configService.save(config)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Suppression
	 *
	 * @param config
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "configs")
	def delete(Config config) {
		configService.delete(config)
		redirect(action: COMMAND_NAME + 's')
	}
}
