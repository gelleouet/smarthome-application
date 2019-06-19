package smarthome.automation

import org.springframework.security.access.annotation.Secured
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.SmartHomeException
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum


@Secured("hasRole('ROLE_ADMIN')")
class ProducteurEnergieController extends AbstractController {

	private static final String COMMAND_NAME = 'producteurEnergie'

	ProducteurEnergieService producteurEnergieService

	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Productions solaires", navigation = NavigationEnum.configuration, header = "Administrateur")
	def producteurEnergies(ProducteurEnergieCommand command) {
		def producteurEnergies = producteurEnergieService.search(command, this.getPagination([:]))
		def recordsTotal = producteurEnergies.totalCount

		// producteurEnergieInstances est accessible depuis le model avec la variable producteurEnergieInstance[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond producteurEnergies, model: [recordsTotal: recordsTotal, command: command]
	}


	/**
	 * Edition
	 *
	 * @param producteurEnergie
	 * @return
	 */
	def edit(ProducteurEnergie producteurEnergie) {
		def editProducteurEnergie = parseFlashCommand(COMMAND_NAME, producteurEnergie)
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editProducteurEnergie]))
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
	 * @param producteurEnergie
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = ProducteurEnergieController.COMMAND_NAME)
	def save(ProducteurEnergie producteurEnergie) {
		checkErrors(this, producteurEnergie)
		producteurEnergieService.save(producteurEnergie)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Suppression
	 *
	 * @param producteurEnergie
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "producteurEnergies")
	def delete(ProducteurEnergie producteurEnergie) {
		producteurEnergieService.delete(producteurEnergie)
		redirect(action: COMMAND_NAME + 's')
	}
}
