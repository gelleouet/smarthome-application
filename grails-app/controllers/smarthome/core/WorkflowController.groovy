package smarthome.core

import org.springframework.security.access.annotation.Secured

import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum


@Secured("hasRole('ROLE_ADMIN')")
class WorkflowController extends AbstractController {

	private static final String COMMAND_NAME = 'workflow'

	WorkflowService workflowService


	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Workflows", navigation = NavigationEnum.configuration,
	header = "Système")
	def workflows(String workflowSearch) {
		def workflows = Workflow.createCriteria().list(this.getPagination([:])) {
			if (workflowSearch) {
				ilike('libelle', QueryUtils.decorateMatchAll(workflowSearch))
			}
		}
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
		parseBpmnFile(workflow)
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
		parseBpmnFile(workflow)
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
	@ExceptionNavigationHandler(actionName = "workflows", modelName = WorkflowController.COMMAND_NAME)
	def delete(Workflow workflow) {
		workflowService.delete(workflow)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Rendu d'un workflow pour une entité
	 * 
	 * @return
	 */
	@Secured("hasRole('ROLE_WORKFLOW')")
	def renderProgress(Workflow workflow, String domainClass, Long domainId) {
		workflowService.prepareForProgress(workflow, domainClass, domainId)
		render(template: 'workflowProgress', model: [workflow: workflow,
			domainClass: domainClass, domainId: domainId])
	}


	/**
	 * Récupère le fichier template si uploadé
	 */
	def parseBpmnFile(Workflow workflow) {
		def file = request.getFile("bpmnFile")
		if (file && !file.empty) {
			workflow.data = file.getInputStream().getBytes()
			// revalide car le data est injecté
			workflow.validate()
		}
	}


	/**
	 * Renvoit l'image du diagramme BPMN
	 * 
	 * @return
	 */
	def diagramImage(Workflow workflow) {
		workflowService.renderDiagramImage(workflow, response.getOutputStream())
	}


	/**
	 * Affiche une boite de dialogue pour le rendu du diagramme BPMN
	 * 
	 * @param workflow
	 * @return
	 */
	def dialogDiagram(Workflow workflow) {
		render(view: 'dialogDiagram', model: [workflow: workflow])
	}
}
