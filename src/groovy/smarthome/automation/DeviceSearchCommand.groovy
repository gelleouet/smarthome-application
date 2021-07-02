package smarthome.automation

import java.util.Map;

import grails.validation.Validateable;
import smarthome.core.query.HQL

@Validateable
class DeviceSearchCommand {
	Map pagination = [:]
	String search
	String deviceTypeClass
	long userId
	long adminId
	String searchGroupe
	String tableauBord
	boolean favori
	Long userSharedId
	Date dateDebut = new Date()
	Date dateFin = new Date()
	
	
	static constraints = {
		search nullable: true
		searchGroupe nullable: true
		deviceTypeClass nullable: true
		tableauBord nullable: true
		userSharedId nullable: true
	}
	
	
	/**
	 * Applique les crières du command sur une query
	 *
	 * @param query
	 * @return
	 */
	HQL applyCriterion(HQL query) {
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
		
		return query
	}
	
}
