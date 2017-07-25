package smarthome.security

import java.io.Serializable;
import java.util.Set;

import grails.validation.Validateable


/**
 * Les amis d'un utilisateur
 * 
 * @author gregory
 *
 */
@Validateable
class UserFriend implements Serializable {

	static belongsTo = [user: User]
	User friend
	
	
	static constraints = {
		friend unique: 'user'
	}

	static mapping = {
		user index: "UserFriend_User_Idx"
	}

}
