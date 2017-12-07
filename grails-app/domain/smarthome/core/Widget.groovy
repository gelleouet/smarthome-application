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
	String gsp
	
	
    static constraints = {
		
    }
	
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		sort 'libelle'
	}
}
