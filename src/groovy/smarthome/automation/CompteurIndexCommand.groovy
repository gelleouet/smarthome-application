package smarthome.automation

import grails.validation.Validateable
import smarthome.core.PaginableCommand
import smarthome.security.Profil
import smarthome.security.User


@Validateable
class CompteurIndexCommand extends PaginableCommand implements Serializable {
	Long deviceTypeId
	Long profilId
	Long adminId
	String userSearch
	String ville
	

	static constraints = {
		deviceTypeId nullable: true
		profilId nullable: true
		userSearch nullable: true
		ville nullable: true
	}


	/**
	 * Bind l'admin et relance les validations
	 * 
	 * @param admin
	 * @return
	 */
	CompteurIndexCommand admin(Long adminId) {
		this.adminId = adminId
		this.clearErrors()
		this.validate()
		return this
	}
	
	
}
