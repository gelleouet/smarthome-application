package smarthome.core

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.springframework.transaction.annotation.Transactional;

import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigableActions;


class PluginService extends AbstractService {
	GrailsApplication grailsApplication
	
	
	private static List navigationItems = null
	
	
	/**
	 * Recherche les actions items définies dans l'application
	 * 
	 * @return
	 */
	List navigationItems() {
		if (navigationItems == null) {
			navigationItems = []
			
			for (controller in grailsApplication.controllerClasses) {
				// Scanne toutes les méthodes avec annotation NavigableAction ou NavigableActions
				// on ne regarde que dans les méthodes de la classe
				controller.clazz.getDeclaredMethods().each { method ->
					if (method.getAnnotation(NavigableAction) || method.getAnnotation(NavigableActions)) {
						navigationItems << ['controller': controller, 'method': method]
					}
				}
			}
		}
		
		return navigationItems
	}
}
