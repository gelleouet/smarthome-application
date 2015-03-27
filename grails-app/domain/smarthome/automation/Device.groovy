package smarthome.automation

import org.apache.commons.lang.StringUtils;

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
	static transients = ['params']
	
	String label
	String groupe
	String mac
	String value
	Date dateValue
	DeviceType deviceType
	
	Map params = [:]
	
	
    static constraints = {
		agent nullable: true
		mac unique: ['agent']
		groupe nullable: true
		value nullable: true
		dateValue nullable: true
		params bindable: true, nullable: true
    }
	
	static mapping = {
		mac index: "Device_MacAgent_Idx"
		agent index: "Device_MacAgent_Idx"
		user index: "Device_User_Idx"
		values cascade: 'all-delete-orphan'
		sort 'label'
	}
	
	
	static {
		grails.converters.JSON.registerObjectMarshaller(Device) {
			[id: it.id, mac: it.mac, label: it.label, groupe: it.groupe, value: it.value, dateValue: it.dateValue, deviceType: it.deviceType, params: it.params]
		}
	}
	
	
	String view() {
		def className = Class.forName(deviceType.implClass)
		StringUtils.uncapitalize(className.simpleName)
	}
}
