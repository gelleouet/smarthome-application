package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Un type device
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceType {
	String libelle
	boolean capteur
	
	
    static constraints = {
		libelle unique: true
    }
	
	static mapping = {
		capteur defaultValue: false
	}
}
