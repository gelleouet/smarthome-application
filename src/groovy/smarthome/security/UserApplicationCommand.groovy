package smarthome.security

import grails.validation.Validateable


/**
 * 
 * 
 * @author gregory
 *
 */
@Validateable
class UserApplicationCommand {
	String search
	User user

	
	static constraints = {
		search nullable: true	
	}
}
