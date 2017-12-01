package smarthome.rule

import java.util.List;
import java.util.Map;

import smarthome.core.ApplicationUtils;
import smarthome.core.ClassUtils;
import smarthome.core.ScriptRule;
import smarthome.core.SmartHomeException;
import smarthome.rule.Rule;
import grails.plugin.springsecurity.SpringSecurityService;
import grails.util.Environment;
import groovy.lang.Closure;

/**
 * Exécution d'un script Groovy enregistré en base
 * 
 * @author gregory
 *
 */
class GroovyScriptRuleService<I, O> extends AbstractRuleService<I, O> {

	SpringSecurityService springSecurityService
	
	
	@Override
	O executeFromScript(String script, I object) throws SmartHomeException {
		return executeFromScript(script, object, [:])
	}
	
	
	@Override
	O executeFromScript(String script, I object, Map parameters) throws SmartHomeException {
		Rule rule = (Rule) ClassUtils.newInstance(script)
		return this.executeRule(rule, object, parameters)
	}
	
	
	@Override
	List<Map> executeBatch(List<Map> objects, boolean ruleObligatoire) throws SmartHomeException {
		Rule rule = this.newRuleInstance(ruleObligatoire)
		
		if (rule) {
			for (Map object : objects) {
				object.result = this.executeRule(rule, object.object, object.parameters)
			}	
		}
		
		return objects
	}
	
	
	@Override
	O execute(I object, boolean ruleObligatoire) throws SmartHomeException {
		return execute(object, ruleObligatoire, [:])
	}
	
	
	@Override
	O execute(I object, boolean ruleObligatoire, Map parameters) throws SmartHomeException {
		Rule rule = this.newRuleInstance(ruleObligatoire)
		
		if (rule) {
			return executeRule(rule, object, parameters)
		} else {
			return null
		}
	}

	
	/**
	 * Injecte les paramètres dans la règle
	 * 
	 * @param rule
	 * @param parameters
	 */
	private void injectParameters(Rule rule, Map parameters) {
		if (rule.respondsTo("setParameters")) {
			if (parameters == null) {
				parameters = [:]
			}
			
			// injecte par défaut le service spring security
			parameters.springSecurityService = springSecurityService
			
			rule.parameters = parameters
		}
		
		ApplicationUtils.autowireBean(rule)
	} 
	
	
	/**
	 * Instancie la rule
	 * 
	 * @return
	 */
	private Rule newRuleInstance(boolean ruleObligatoire) throws SmartHomeException {
		Rule rule
		
		// recherche de la règle via la nom de l'implémentation (sans le mot Service de fin)
		def className = this.class.name.substring(0, this.class.name.length()-7)
		
		
		if (Environment.getCurrent() != Environment.PRODUCTION) {
			try {
				rule = (Rule) ClassUtils.forNameInstance(className)
			} catch (Exception e) {
				log.error("Cannot create instance ${className}")
			}
		} else {
			ScriptRule scriptRule = ScriptRule.findByRuleName(className)
			
			if (scriptRule) {
				rule = (Rule) ClassUtils.newInstance(scriptRule.script)
			}
		}
		
		if (!rule && ruleObligatoire) {
			throw new SmartHomeException("Règle obligatoire $className introuvable !")
		}
		
		return rule
	}
	
	
	/**
	 * Exécution de la règle
	 * 
	 * @param rule
	 * @param object
	 * @param parameters
	 * @return
	 * @throws SmartHomeException
	 */
    private O executeRule(Rule rule, I object, Map parameters) throws SmartHomeException {
		injectParameters(rule, parameters)
		log.info "Exécution rule ${rule.class.simpleName}"
		return rule.execute(object)
	}
}
