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
	
	
    static constraints = {
		
    }
	
	static mapping = {
		device index: "DeviceValue_Device_Idx"
	}
}
