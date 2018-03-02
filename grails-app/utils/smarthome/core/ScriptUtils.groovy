package smarthome.core

import groovy.lang.GroovyClassLoader;

import java.lang.reflect.Method;

import org.apache.camel.model.FinallyDefinition;
import org.apache.commons.lang.StringUtils;

/**
 * Méthodes utilitaires pour l'exécution de scriplets Groovy
 * 
 * @author gregory
 *
 */
class ScriptUtils {
	/**
	 * Exécute le script avec le contexte spécifié
	 * 
	 * @param script
	 * @param params
	 * @return
	 */
	static def runScript(String scriptText, Map params) throws SmartHomeException {
		if (!scriptText) {
			throw new SmartHomeException("Can't run script : empty source !")
		}
		
		GroovyShell groovyShell
		def result
		
		try {
			groovyShell = new GroovyShell(ScriptUtils.getClassLoader())
			Script script = groovyShell.parse(scriptText)
			if (params) {
				script.setBinding(new Binding(params))
			}
			result = script.run()
		} catch (Exception e) {
			throw new SmartHomeException(e)
		} finally {
			groovyShell?.resetLoadedClasses()
		}
		
		return result
	}
	
}
