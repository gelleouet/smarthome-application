package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Les infos spécifiques à un type device
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceMetadata {
	static belongsTo = [device: Device]
	
	String name
	String value
	
	
    static constraints = {
		value nullable: true
    }
	
	static mapping = {
		device index: "DeviceMetadata_Device_Idx"
	}
}
