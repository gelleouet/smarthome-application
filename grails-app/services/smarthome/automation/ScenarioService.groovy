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


class ScenarioService extends AbstractService {

	DeviceService deviceService
	
	
	/**
	 * Edition ACL
	 * 
	 * @param scenario
	 * @return
	 */
	@PreAuthorize("hasPermission(#scenario, 'OWNER')")
	Scenario edit(Scenario scenario) {
		return scenario
	}
	
	
	/**
	 * Enregistrement d'un domain
	 *
	 * @param domain
	 *
	 * @return domain
	 */
	@PreAuthorize("hasPermission(#scenario, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Scenario save(Scenario scenario) throws SmartHomeException {
		if (!scenario.save()) {
			throw new SmartHomeException("Erreur enregistrement scenario", scenario);
		}
		
		return scenario
	}
	
	
	/**
	 * Liste les scenarios d'un user
	 *
	 * @param pagination
	 * @return
	 * @throws SmartHomeException
	 */
	def listByUser(String scenarioSearch, Long userId, Map pagination) throws SmartHomeException {
		return Scenario.createCriteria().list(pagination) {
			user {
				idEq(userId)
			}
			
			if (scenarioSearch) {
				ilike 'label', QueryUtils.decorateMatchAll(scenarioSearch)
			}
		}
	}
	
	
	/**
	 * Exécute un scenario. Insère le contexte utilisateur avant exécution (injecte les devices)
	 * 
	 * @param scenario
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def execute(Scenario scenario, Map context) throws SmartHomeException {
		// ajoute le service deviceService pour le déclenchement des actions
		context.deviceService = deviceService
		context.scenario = scenario
		context.log = log
		
		def result = ScriptUtils.runScript(scenario.script, context)
		
		// trace l'exécution seulement si scenario s'est terminé correctement
		if (result) {
			scenario.lastExecution = new Date()
			scenario.save()
		}
		
		return result
	}
	
	
	/**
	 * Suppression d'une instance
	 *
	 * @param domain
	 * @return
	 */
	@PreAuthorize("hasPermission(#scenario, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void delete(Scenario scenario) {
		try {
			// flush direct pour catcher une erreur SQL (ex : clé étrangère) et la renvoyer en SmartHomeException
			// sinon l'erreur est déclenchée hors méthode
			scenario.delete(flush: true)
		} catch (Exception ex) {
			throw new SmartHomeException(ex, scenario)
		}
	}
}
