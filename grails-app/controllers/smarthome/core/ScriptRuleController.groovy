package smarthome.core

import org.springframework.security.access.annotation.Secured

import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum

@Secured("hasRole('ROLE_ADMIN')")
class ScriptRuleController extends AbstractController {

	private static final String COMMAND_NAME = 'scriptRule'

	ScriptRuleService scriptRuleService

	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Règles métier", navigation = NavigationEnum.configuration,
	header = "Système")
	def scriptRules(String scriptRuleSearch) {
		def scriptRules
		int recordsTotal
		def pagination = this.getPagination([:])

		if (scriptRuleSearch) {
			def search = QueryUtils.decorateMatchAll(scriptRuleSearch)

			scriptRules = ScriptRule.createCriteria().list(pagination) {
				ilike('ruleName', search)
			}
			recordsTotal = scriptRules.totalCount
		} else {
			recordsTotal = ScriptRule.count()
			scriptRules = ScriptRule.list(pagination)
		}

		// scriptRules est accessible depuis le model avec la variable scriptRule[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond scriptRules, model: [recordsTotal: recordsTotal, scriptRuleSearch: scriptRuleSearch]
	}


	/**
	 * Edition
	 *
	 * @param scriptRule
	 * @return
	 */
	def edit(ScriptRule scriptRule) {
		def editScriptRule = parseFlashCommand(COMMAND_NAME, scriptRule)
		render(view: COMMAND_NAME, model: [(COMMAND_NAME): editScriptRule])
	}


	/**
	 * Création
	 *
	 * @return
	 */
	def create() {
		def editScriptRule = parseFlashCommand(COMMAND_NAME, new ScriptRule())
		render(view: COMMAND_NAME, model: [(COMMAND_NAME): editScriptRule])
	}


	/**
	 * Enregistrement modification
	 *
	 * @param scriptRule
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = ScriptRuleController.COMMAND_NAME)
	def saveEdit(ScriptRule scriptRule) {
		checkErrors(this, scriptRule)
		scriptRuleService.save(scriptRule)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Enregistrement d'un nouveau
	 *
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "create", modelName = ScriptRuleController.COMMAND_NAME)
	def saveCreate(ScriptRule scriptRule) {
		checkErrors(this, scriptRule)
		scriptRuleService.save(scriptRule)
		redirect(action: COMMAND_NAME + 's')
	}
}
