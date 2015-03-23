package smarthome.core

import java.lang.reflect.Method;

/**
 * Méthodes utilitaires sur les classes
 * 
 * @author gregory
 *
 */
class ClassUtils {
	/**
	 * Recherche méthodes par leurs noms
	 * 
	 * @param classe
	 * @param methodName
	 * @return
	 */
	static Method[] findMethods(Class classe, String methodName) {
		classe.methods.findAll { method ->
			method.name == methodName
		}
	}
}
