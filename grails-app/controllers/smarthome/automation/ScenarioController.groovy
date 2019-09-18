package smarthome.automation




import org.springframework.security.access.annotation.Secured

import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum

@Secured("isAuthenticated()")
class ScenarioController extends AbstractController {

	private static final String COMMAND_NAME = 'scenario'

	ScenarioService scenarioService

	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Scénarios", navigation = NavigationEnum.configuration, header = "Smarthome")
	def scenarios(String scenarioSearch) {
		def scenarios = scenarioService.listByUser(scenarioSearch, principal.id, this.getPagination([:]))
		def recordsTotal = scenarios.totalCount

		// scenarios est accessible depuis le model avec la variable scenario[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond scenarios, model: [recordsTotal: recordsTotal, scenarioSearch: scenarioSearch]
	}


	/**
	 * Edition
	 *
	 * @param scenario
	 * @return
	 */
	def edit(Scenario scenario) {
		def editscenario = parseFlashCommand(COMMAND_NAME, scenario)
		editscenario = scenarioService.edit(editscenario)
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editscenario]))
	}


	/**
	 * Création
	 *
	 * @return
	 */
	def create() {
		def editscenario = parseFlashCommand(COMMAND_NAME, new Scenario())
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editscenario]))
	}


	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]

		// on remplit avec les infos du user
		model << userModel

		return model
	}


	/**
	 * Enregistrement modification
	 *
	 * @param scenario
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = ScenarioController.COMMAND_NAME)
	def saveEdit(Scenario scenario) {
		checkErrors(this, scenario)
		scenarioService.save(scenario)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Enregistrement d'un nouveau
	 *
	 * @param scenario
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "create", modelName = ScenarioController.COMMAND_NAME)
	def saveCreate(Scenario scenario) {
		scenario.user = authenticatedUser
		scenario.validate() // important car les erreurs sont traitées lors du binding donc le device.user sort en erreur
		checkErrors(this, scenario)
		scenarioService.save(scenario)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Suppression
	 *
	 * @param scenario
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "scenarios", modelName = ScenarioController.COMMAND_NAME)
	def delete(Scenario scenario) {
		scenarioService.delete(scenario)
		redirect(action: COMMAND_NAME + 's')
	}
}
