package smarthome.core

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.springframework.transaction.annotation.Transactional;



class WorkflowService extends AbstractService {
	RepositoryService repositoryService
	RuntimeService runtimeService
	GrailsApplication grailsApplication
	
	
	/**
	 * Enregistrement
	 *
	 * @param workflow
	 *
	 * @return workflow
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Workflow save(Workflow workflow) throws SmartHomeException {
		if (!workflow.save()) {
			throw new SmartHomeException("Erreur enregistrement workflow", workflow);
		}
		
		// supprime le workflow dans le repo pour qu'il soit remis à jour au runtime
		this.deleteDeployment(workflow)
		this.createDeployment(workflow)
		
		return workflow
	}
	
	
	/**
	 * Suppression d'un workflow et aussi dans le repo
	 *
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Object delete(Object domain) {
		super.delete(domain)
		this.deleteDeployment(domain)
		return domain
	}
	
	
	/**
	 * Exécution d'un workflow en mode asynchrone.
	 *
	 * @param workflowName
	 * @param context
	 *
	 * @throws SmartHomeException
	 */
	void asyncExecute(String workflowName, def context) throws SmartHomeException {
		if (!workflowName) {
			throw new SmartHomeException("workflowName is required !")
		}
		
		if (context == null) {
			context = [:]
		}
		
		// ajout du workflow à exécuter dans le payload
		context.workflowName = workflowName
		
		asyncSendMessage(SmartHomeCoreConstantes.DIRECT_EXCHANGE, SmartHomeCoreConstantes.WORKFLOW_QUEUE,
			context, ExchangeType.DIRECT)
	}
	
	
	/**
	 * Exécute le workflow sur le contexte donné
	 * La transaction est en écriture dans le cas de mise à jour depuis le workflow
	 *
	 * @param workflow
	 * @param context
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Workflow execute(Workflow workflow, def context) throws SmartHomeException {
		Chronometre chrono = new Chronometre()
		
		// recherche du workflow dans le repo
		def deployment = this.findAndCreateDeployment(workflow)
		
		// récupère définition du process
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
			.deploymentId(deployment.getId()).singleResult();
			
		// on démarre une instance du workflow sur le contexte donné
		ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId(),
			[context: context])
		
		log.info "Execute workflow ${workflow.libelle} : ${chrono.stop()}ms"
		
		return workflow
	}
	
	
	/**
	 * Suppression d'un workflow existant dans le repo
	 *
	 * @param workflow
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Deployment deleteDeployment(Workflow workflow) throws SmartHomeException {
		def deployment = this.findDeployment(workflow)
		
		if (deployment) {
			try {
				repositoryService.deleteDeployment(deployment.getId(), true)
			} catch (Exception e) {
				throw new SmartHomeException(e)
			}
			
		}
		
		return deployment
	}
	
	
	/**
	 * Charge le workflow depuis le repo
	 *
	 * @param workflow
	 * @return
	 * @throws SmartHomeException
	 */
	Deployment findDeployment(Workflow workflow) throws SmartHomeException {
		try {
			return repositoryService.createDeploymentQuery()
				.deploymentName(workflow.libelle).singleResult()
		} catch (Exception ex) {
			throw new SmartHomeException(ex)
		}
	}
	
	
	/**
	 * Recherche dans le repo d'un workflow. S'il n'est pas déployé, on le fait
	 *
	 * @param workflow
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Deployment findAndCreateDeployment(Workflow workflow) throws SmartHomeException {
		def deployment = this.findDeployment(workflow)
		
		if (!deployment) {
			deployment = this.createDeployment(workflow)
		}
		
		return deployment
	}
	
	
	/**
	 * Création d'un workflow dans le repo.
	 * Cette méthode ne vérifie pas si le workflow est déjà déployé
	 *
	 * @param workflow
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Deployment createDeployment(Workflow workflow) throws SmartHomeException {
		if (! workflow.data) {
			throw new SmartHomeException("Workflow data is empty ${workflow.libelle} !")
		}
		
		def deployment
		
		new ByteArrayInputStream(workflow.data).withStream { stream ->
			try {
				deployment = repositoryService.createDeployment()
					.addInputStream(workflow.bpmnKey(), stream).name(workflow.libelle).deploy()
			} catch (Exception ex) {
				throw new SmartHomeException(ex)
			}
		}
		
		return deployment
	}
	
	
	/**
	 * Récupère une image d'un workflow. Si le workflow n'est déployé, on le fait
	 *
	 * @param workflow
	 * @param outStream
	 *
	 * @return
	 * @throws SmartHomeException
	 */
	void renderDiagramImage(Workflow workflow, OutputStream outStream) throws SmartHomeException {
		def deployment = this.findAndCreateDeployment(workflow)
		
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
			.deploymentId(deployment.getId()).singleResult();
			
		outStream.withStream { out ->
			repositoryService.getProcessDiagram(processDefinition.getId()).withStream { inStream ->
				out << inStream
			}
		}
	}
	
	
	/**
	 * Recherche un workflow par son libelle
	 *
	 * @param libelle
	 * @return
	 */
	Workflow findByLibelle(String libelle) {
		return Workflow.findByLibelle(libelle)
	}
}
