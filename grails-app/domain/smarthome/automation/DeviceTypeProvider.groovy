package smarthome.automation

import java.io.Serializable;

import smarthome.core.SmartHomeCoreConstantes;
import grails.validation.Validateable;

/**
 * Fournissuer pour un type de device
 * 
 * Ex : edf, eau, gaz, etc..
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceTypeProvider implements Serializable {
	String libelle
	DeviceType deviceType
	
	
    static constraints = {
		libelle unique: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		sort 'libelle'
	}
}
