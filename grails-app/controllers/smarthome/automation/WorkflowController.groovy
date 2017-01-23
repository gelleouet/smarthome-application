package smarthome.automation




import org.springframework.security.access.annotation.Secured;

import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;

@Secured("isAuthenticated()")
class WorkflowController extends AbstractController {

    private static final String COMMAND_NAME = 'workflow'
	
	WorkflowService workflowService
	
	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Scénarios", navigation = NavigationEnum.configuration, header = "Administrateur")
	def workflows(String workflowSearch) {
		def workflows = workflowService.listByUser(workflowSearch, principal.id, this.getPagination([:]))
		def recordsTotal = workflows.totalCount

		// workflows est accessible depuis le model avec la variable workflow[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond workflows, model: [recordsTotal: recordsTotal, workflowSearch: workflowSearch]
	}
	
	
	/**
	 * Edition
	 *
	 * @param workflow
	 * @return
	 */
	def edit(Workflow workflow) {
		def editWorkflow = parseFlashCommand(COMMAND_NAME, workflow)
		editWorkflow = workflowService.edit(editWorkflow)
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editWorkflow]))
	}


	/**
	 * Création
	 *
	 * @return
	 */
	def create() {
		def editWorkflow = parseFlashCommand(COMMAND_NAME, new Workflow())
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editWorkflow]))
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
	 * @param workflow
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = WorkflowController.COMMAND_NAME)
	def saveEdit(Workflow workflow) {
		checkErrors(this, workflow)
		workflowService.save(workflow)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Enregistrement d'un nouveau
	 *
	 * @param workflow
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "create", modelName = WorkflowController.COMMAND_NAME)
	def saveCreate(Workflow workflow) {
		workflow.user = authenticatedUser
		workflow.validate() // important car les erreurs sont traitées lors du binding donc le device.user sort en erreur
		checkErrors(this, workflow)
		workflowService.save(workflow)
		redirect(action: COMMAND_NAME + 's')
	}
	
	
	/**
	 * Suppression
	 *
	 * @param workflow
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "workflows")
	def delete(Workflow workflow) {
		workflowService.delete(workflow)
		redirect(action: COMMAND_NAME + 's')
	}
}
