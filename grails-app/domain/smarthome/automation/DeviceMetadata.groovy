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
	String label
	String value
	String help
	String type
	String values
	
	
    static constraints = {
		label nullable: true
		value nullable: true
		help nullable: true
		type nullable: true
		values nullable: true
    }
	
	static mapping = {
		device index: "DeviceMetadata_Device_Idx"
		help type: 'text'
		values type: 'text'
	}
}
