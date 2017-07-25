package smarthome.automation

import java.io.Serializable;

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Les infos spécifiques à un type device
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceMetadata implements Serializable {
	static belongsTo = [device: Device]
	
	String name
	String label
	String value
	String type
	String values
	
	
    static constraints = {
		label nullable: true
		value nullable: true
		type nullable: true
		values nullable: true
    }
	
	static mapping = {
		device index: "DeviceMetadata_Device_Idx"
		values type: 'text'
	}
}
