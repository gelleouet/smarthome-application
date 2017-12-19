package smarthome.automation

import java.io.Serializable;

import smarthome.core.SmartHomeCoreConstantes;
import grails.validation.Validateable;

/**
 * Mode de chauffage
 * 
 * @author gregory
 *
 */
@Validateable
class Chauffage implements Serializable {
	String libelle
	
	
    static constraints = {
		libelle unique: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		sort 'libelle'
	}
}
