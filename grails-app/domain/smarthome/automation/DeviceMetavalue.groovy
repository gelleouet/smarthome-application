package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Les valeurs spécifiques à un type device
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceMetavalue {
	static belongsTo = [device: Device]
	
	String name
	String label
	String value
	String type
	
	
    static constraints = {
		value nullable: true;
		label nullable: true;
		type nullable: true;
    }
	
	static mapping = {
		device index: "DeviceMetavalue_Device_Idx"
	}
}
