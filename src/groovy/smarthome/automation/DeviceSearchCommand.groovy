package smarthome.automation

import java.util.Map;

import grails.validation.Validateable;

@Validateable
class DeviceSearchCommand {
	Map pagination = [:]
	String search
	String deviceTypeClass
	boolean filterShow
	long userId
	String searchGroupe
	boolean sharedDevice
	
	static constraints = {
		search nullable: true
		searchGroupe nullable: true
		deviceTypeName nullable: true
	}
}
