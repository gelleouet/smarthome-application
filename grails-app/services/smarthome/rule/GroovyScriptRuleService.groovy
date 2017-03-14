package smarthome.rule

import smarthome.core.ClassUtils;
import smarthome.core.ScriptRule;
import smarthome.core.SmartHomeException;
import smarthome.rule.Rule;
import grails.plugin.springsecurity.SpringSecurityService;
import grails.util.Environment;

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
		O result = null
		Rule rule
		
		// Chargement de la classe "Rule" enregistrée dans le script
		try {
			Class ruleClass = ClassUtils.newClass(script)
			rule = (Rule) ruleClass.newInstance()
			injectParameters(rule, parameters)
			
			result = rule.execute(object)
		} catch (SmartHomeException ex) {
			throw ex
		} catch (Exception ex) {
			throw new SmartHomeException(ex)
		}
		
		log.info "Exécution rule ${rule.class.simpleName}"
		return result
	}
	
	
	@Override
	O execute(I object, boolean ruleObligatoire) throws SmartHomeException {
		return execute(object, ruleObligatoire, [:])
	}
	
	
	@Override
	O execute(I object, boolean ruleObligatoire, Map parameters) throws SmartHomeException {
		// recherche de la règle via la nom de l'implémentation (sans le mot Service de fin)
		def className = this.class.name.substring(0, this.class.name.length()-7)
		
		
		if (Environment.getCurrent() != Environment.PRODUCTION) {
			return executeDev(object, ruleObligatoire, className, parameters)
		}
		
		def scriptRule = ScriptRule.find( {
			ruleName == className
		})
		
		if (!scriptRule && ruleObligatoire) {
			throw new SmartHomeException("Aucune règle trouvée en base : $className")
		}
		
		if (scriptRule) {
			return executeFromScript(scriptRule.script, object, parameters)
		} else {
			log.info "La règle $className n'existe pas !"
			return null
		}
	}

	
	
	/**
	 * Exécution de la règle en mode DEV
	 * 
	 * La classe est chargée depuis le classloader directement pour permettre du debuggage
	 * 
	 * @param object
	 * @param ruleObligatoire
	 * @return
	 * @throws LimsException
	 */
	private executeDev(I object, boolean ruleObligatoire, String className, Map parameters) throws SmartHomeException {
		Rule rule = null
		
		try {
			rule = (Rule) Class.forName(className).newInstance()
		} catch (Exception e) {
			e.printStackTrace()
		}
		
		if (!rule && ruleObligatoire) {
			throw new SmartHomeException("Aucune règle trouvée dans le classLoader : $className")
		}
		
		injectParameters(rule, parameters)
		
		def result = rule.execute(object)
		log.info "Exécution rule ${rule.class.simpleName}"
		return result
		
	}
	
	
	/**
	 * INjecte les paramètres dans la règle
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
	} 
    
}
