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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.ClassUtils;
import smarthome.core.QueryUtils;
import smarthome.automation.Chart;
import smarthome.core.ExchangeType;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class WorkflowService extends AbstractService {

	static GroovyClassLoader loader = new GroovyClassLoader(WorkflowService.class.getClassLoader())
	
	/**
	 * Liste les workflows d'un user
	 *
	 * @param pagination
	 * @return
	 * @throws SmartHomeException
	 */
	def listByUser(Map pagination, String workflowSearch) throws SmartHomeException {
		def userId = springSecurityService.principal.id
		def search = QueryUtils.decorateMatchAll(workflowSearch)
		
		return Workflow.createCriteria().list(pagination) {
			user {
				idEq(userId)
			}
			
			if (workflowSearch) {
				ilike 'label', search
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
	def execute(Workflow workflow) throws SmartHomeException {
		GroovyShell groovyShell = new GroovyShell(loader)
		Script script = groovyShell.parse(workflow.script)
		script.run()
	}
}
