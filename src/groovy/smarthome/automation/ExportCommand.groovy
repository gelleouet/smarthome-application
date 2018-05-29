package smarthome.automation

import smarthome.core.DateUtils;
import grails.validation.Validateable;
import groovy.time.TimeCategory;


@Validateable
class ExportCommand {
	String deviceTypeClass
	long userId
	long adminId
	Date dateDebut = new Date()
	Date dateFin = new Date()
	
	// usage interne uniquement
	List userIdsExport = []
	
	
	static constraints = {
		deviceTypeClass nullable: true
	}
	
	
	/**
	 * Calcule date/heure début en fonction dateDebut
	 * 
	 * @return
	 */
	Date datetimeDebut() {
		return DateUtils.copyTruncDay(dateDebut)
	}
	
	
	/**
	 * Calcule date/heure fin en fonction dateDebut
	 *
	 * @return
	 */
	Date datetimeFin() {
		Date datetimeFin
		
		use(TimeCategory) {
			datetimeFin = DateUtils.copyTruncDay(dateFin) + 23.hours + 59.minutes + 59.seconds
		}	
		
		return datetimeFin
	}
}
