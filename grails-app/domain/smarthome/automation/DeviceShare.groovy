package smarthome.automation

import java.io.Serializable;

import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Partage de devices entre plusieurs utilisateurs avec notion de modification / execution / consultation
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceShare implements Serializable {
	Device device
	User sharedUser
	
	
	static belongsTo = [device: Device]
	
	
    static constraints = {
		device unique: 'sharedUser' 	
    }
	
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		device index: "DeviceShare_Device_Idx"
	}
	
}
