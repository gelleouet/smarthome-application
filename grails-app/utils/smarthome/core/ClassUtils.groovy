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
	
	
	/**
	 * Créé une classe à partir de sa définition
	 *
	 * @param classe
	 * @return
	 * @throws LimsException
	 */
	static Class newClass(String script) throws SmartHomeException {
		try {
			GroovyClassLoader classLoader = new GroovyClassLoader()
			Class classe = classLoader.parseClass(script)
			//GroovySystem.getMetaClassRegistry().removeMetaClass(classe)
			classLoader.clearCache()
			return classe
		} catch (Exception ex) {
			throw new SmartHomeException(ex.message)
		}
	}
	
	
	/**
	 * Créé une classe à patir de son nom
	 * 
	 * @param className
	 * @return
	 * @throws SmartHomeException
	 */
	static Class forName(String className) throws SmartHomeException {
		try {
			return Class.forName(className)
		} catch (Exception ex) {
			throw new SmartHomeException(ex.message)
		}
	}
	
	
	/**
	 * Instance un objet directement à partir de la classe
	 *
	 * @param classe
	 * @return
	 * @throws LimsException
	 */
	static Object newInstance(Class classe) throws SmartHomeException {
		try {
			return classe.newInstance()
		} catch (Exception ex) {
			throw new SmartHomeException(ex.message)
		}
	}
	
	
	
	/**
	 * Instancie un objet à partir d'un script texte contenant la définition de la classe
	 *
	 * @param scriptText
	 * @return
	 */
	static Object newInstance(String scriptText) throws SmartHomeException {
		Class instanceClass = newClass(scriptText)
		newInstance(instanceClass)
	}
	
	
	/**
	 * Instance un objet directement à partir du nom de la classe
	 *
	 * @param className
	 * @return
	 * @throws SmartHomeException
	 */
	static Object forNameInstance(String className) throws SmartHomeException {
		Class classe = forName(className)
		return newInstance(classe)
	}
	
}
