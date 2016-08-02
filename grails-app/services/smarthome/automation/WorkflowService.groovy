package smarthome.automation

import java.util.List;
import java.util.Map;

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import org.codehaus.groovy.grails.web.mapping.LinkGenerator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.ClassUtils;
import smarthome.core.QueryUtils;
import smarthome.core.ScriptUtils;
import smarthome.automation.Chart;
import smarthome.core.ExchangeType;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class WorkflowService extends AbstractService {

	DeviceService deviceService
	
	
	/**
	 * Edition ACL
	 * 
	 * @param workflow
	 * @return
	 */
	@PreAuthorize("hasPermission(#workflow, 'OWNER')")
	Workflow edit(Workflow workflow) {
		return workflow
	}
	
	
	/**
	 * Enregistrement d'un domain
	 *
	 * @param domain
	 *
	 * @return domain
	 */
	@PreAuthorize("hasPermission(#workflow, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Workflow save(Workflow workflow) throws SmartHomeException {
		if (!workflow.save()) {
			throw new SmartHomeException("Erreur enregistrement workflow", workflow);
		}
		
		return workflow
	}
	
	
	/**
	 * Liste les workflows d'un user
	 *
	 * @param pagination
	 * @return
	 * @throws SmartHomeException
	 */
	def listByUser(String workflowSearch, Long userId, Map pagination) throws SmartHomeException {
		return Workflow.createCriteria().list(pagination) {
			user {
				idEq(userId)
			}
			
			if (workflowSearch) {
				ilike 'label', QueryUtils.decorateMatchAll(workflowSearch)
			}
		}
	}
	
	
	/**
	 * Exécute un workflow. Insère le contexte utilisateur avant exécution (injecte les devices)
	 * 
	 * @param workflow
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def execute(Workflow workflow, Map context) throws SmartHomeException {
		// ajoute le service deviceService pour le déclenchement des actions
		context.deviceService = deviceService
		context.workflow = workflow
		context.log = log
		
		def result = ScriptUtils.runScript(workflow.script, context)
		
		// trace l'exécution seulement si workflow s'est terminé correctement
		if (result) {
			workflow.lastExecution = new Date()
			workflow.save()
		}
		
		return result
	}
	
	
	/**
	 * Suppression d'une instance
	 *
	 * @param domain
	 * @return
	 */
	@PreAuthorize("hasPermission(#workflow, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void delete(Workflow workflow) {
		try {
			// flush direct pour catcher une erreur SQL (ex : clé étrangère) et la renvoyer en SmartHomeException
			// sinon l'erreur est déclenchée hors méthode
			workflow.delete(flush: true)
		} catch (Exception ex) {
			throw new SmartHomeException(ex, workflow)
		}
	}
}
