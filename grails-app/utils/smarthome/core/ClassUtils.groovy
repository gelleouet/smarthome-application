package smarthome.core

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

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
	
	
	/**
	 * Retourne le nom pour un exchange ou routingKey d'un service
	 * 
	 * @param objet
	 * @return
	 */
	static String prefixAMQ(Object object) {
		object.getClass().getPackage().getName() + "." + StringUtils.uncapitalize(object.class.simpleName)
	}
}
