package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Les périphériques à contrôler
 *  
 * @author gregory
 *
 */
@Validateable
class Device {
	static belongsTo = [agent: Agent, user: User]
	static hasMany = [values: DeviceValue]
	
	String label
	String groupe
	String mac
	String value
	Date dateValue
	DeviceType deviceType
	
	
    static constraints = {
		mac unique: ['agent']
		groupe nullable: true
		value nullable: true
		dateValue nullable: true
    }
	
	static mapping = {
		mac index: "Device_MacAgent_Idx"
		agent index: "Device_MacAgent_Idx"
		user index: "Device_User_Idx"
		values cascade: 'all-delete-orphan'
		sort 'label'
	}
}
