package smarthome.automation

import java.util.Map;

import grails.validation.Validateable;

@Validateable
class DeviceSearchCommand {
	Map pagination = [:]
	String search
	boolean filterShow
	long userId
	String searchGroupe
	boolean sharedDevice
	
	static constraints = {
		search nullable: true
		searchGroupe nullable: true
	}
}
