package smarthome.core

import groovy.lang.GroovyClassLoader;

import java.lang.reflect.Method;

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
		
		try {
			GroovyShell groovyShell = new GroovyShell()
			Script script = groovyShell.parse(scriptText)
			if (params) {
				script.setBinding(new Binding(params))
			}
			
			def result = script.run()
			groovyShell.resetLoadedClasses()
			return result
		} catch (Exception e) {
			throw new SmartHomeException(e)
		}
	}
	
}
