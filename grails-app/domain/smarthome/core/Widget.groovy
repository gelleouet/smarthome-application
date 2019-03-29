package smarthome.core

import grails.validation.Validateable;

/**
 * Gestion des widgets sur tableau de bord
 * 
 *  
 * @author gregory
 *
 */
@Validateable
class Widget {
	
	String libelle
	String description
	Integer refreshPeriod // nombre de minutes pour rafraichissement auto
	String controllerName
	String actionName
	
	
    static constraints = {
		refreshPeriod nullable: true
    }
	
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
	}
}
