package smarthome.automation

import java.util.Map;

import grails.validation.Validateable;

@Validateable
class DeviceSearchCommand {
	Map pagination = [:]
	String search
	String deviceTypeClass
	long userId
	String searchGroupe
	String tableauBord
	boolean favori
	Long userSharedId
	
	
	static constraints = {
		search nullable: true
		searchGroupe nullable: true
		deviceTypeClass nullable: true
		tableauBord nullable: true
		userSharedId nullable: true
	}
}
