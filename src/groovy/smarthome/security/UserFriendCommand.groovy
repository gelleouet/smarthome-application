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
class UserFriendCommand {
	long userId
	String search
	boolean confirm

	
	static constraints = {
		search nullable: true	
	}
	
	
	/**
	 * Retourne la copie de l'objet mais en mode à confirmer
	 * @return
	 */
	UserFriendCommand copyToConfirm() {
		return new UserFriendCommand(userId: this.userId,
			search: this.search, confirm: false)
	}
}
