package smarthome.automation

import grails.validation.Validateable;


@Validateable
class DeviceValueCommand {
	long deviceId
	Map pagination = [:]
	Date dateIndex
	
	
	static constraints = {
		
	}
	
}
