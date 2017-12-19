/**
 * 
 */
package smarthome.security

import grails.validation.Validateable


/**
 * 
 * 
 * @author gregory
 *
 */
@Validateable
class UserCommand {
	String search
	Boolean profilPublic
	List<Long> notInIds = []

	
	static constraints = {
		search nullable: true	
		profilPublic nullable: true	
	}
}
