package smarthome.automation

import smarthome.core.DateUtils;
import smarthome.core.PaginableCommand
import grails.validation.Validateable;
import groovy.time.TimeCategory;


@Validateable
class SupervisionCommand extends PaginableCommand implements Serializable {
	Long deviceTypeId
	Long profilId
	Long adminId
	String userSearch
	Date dateDebut = new Date()
	Date dateFin = new Date()
	
	
	
	static constraints = {
		deviceTypeId nullable: true
		profilId nullable: true
		userSearch nullable: true
	}
	
	
	/**
	 * Bind l'admin et relance les validations
	 *
	 * @param admin
	 * @return
	 */
	SupervisionCommand admin(Long adminId) {
		this.adminId = adminId
		this.clearErrors()
		this.validate()
		return this
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
