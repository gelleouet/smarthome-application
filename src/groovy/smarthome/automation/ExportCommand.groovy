package smarthome.automation

import grails.validation.Validateable;


@Validateable
class ExportCommand {
	String deviceTypeClass
	long userId
	long adminId
	Date dateDebut
	Date dateFin
	
	
	static constraints = {
		deviceTypeClass nullable: true
	}
}
