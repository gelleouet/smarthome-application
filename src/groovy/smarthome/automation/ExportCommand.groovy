package smarthome.automation

import smarthome.core.DateUtils;
import smarthome.core.query.HQL
import grails.validation.Validateable;
import groovy.time.TimeCategory;


@Validateable
class ExportCommand {
	String deviceTypeClass
	long userId
	long adminId
	Date dateDebut = new Date()
	Date dateFin = new Date()
	String exportImpl
	String search
	List<String> metavalueNames = []
	
	
	static constraints = {
		deviceTypeClass nullable: true
		exportImpl nullable: true
		search nullable: true
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
	 * Calcule date/heure fin en fonction dateFin
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
	
	
	/**
	 * Applique les crières du command sur une query
	 * 
	 * @param query
	 * @return
	 */
	HQL applyCriterion(HQL query) {
		query.addCriterion("deviceValue.dateValue BETWEEN :dateDebut AND :dateFin", [dateDebut:
			datetimeDebut(), dateFin: datetimeFin()])
		
		if (userId) {
			query.addCriterion("device.user.id = :userId", [userId: userId])
		} else {
			query.addCriterion("""device.user.id IN (select userAdmin.user.id from UserAdmin userAdmin
				where userAdmin.admin.id = :adminId)""", [adminId: adminId])
		}

		if (deviceTypeClass) {
			query.addCriterion("deviceType.implClass = :implClass", [implClass: deviceTypeClass])
		}
		
		if (search) {
			query.addCriterionLike("device.label", search)
		}
		
		if (metavalueNames) {
			query.addCriterion("deviceValue.name in :metavalueNames", [metavalueNames: metavalueNames])
		}
		
		return query
	}
	
}
