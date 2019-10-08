package smarthome.automation

import grails.validation.Validateable
import smarthome.security.Profil
import smarthome.security.User


@Validateable
class CompteurIndexCommand {
	DeviceType deviceType
	Profil profil
	User admin


	static constraints = {
		deviceType nullable: true
		profil nullable: true
	}


	/**
	 * Bind l'admin et relance les validations
	 * 
	 * @param admin
	 * @return
	 */
	CompteurIndexCommand admin(User admin) {
		this.admin = admin
		this.validate()
		return this
	}
}
