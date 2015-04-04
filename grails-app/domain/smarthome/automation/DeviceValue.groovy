package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Toutes les valeurs historisées d'un device
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceValue {
	static belongsTo = [device: Device]
	
	String value
	Date dateValue
	
	// permet d'avoir plusieurs types de valeur pour un device
	// si un seul type, ne rien mettre
	String name 
	
	
    static constraints = {
		name nullable: true
    }
	
	static mapping = {
		name index: "DeviceValue_DeviceName_Idx"
		device index: "DeviceValue_DeviceName_Idx"
	}
}
