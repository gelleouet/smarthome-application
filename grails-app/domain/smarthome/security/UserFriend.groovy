package smarthome.security

import java.io.Serializable;
import java.util.Set;

import smarthome.core.SmartHomeCoreConstantes;
import grails.validation.Validateable


/**
 * Les amis d'un utilisateur
 * 
 * @author gregory
 *
 */
@Validateable
class UserFriend implements Serializable {

	User user
	User friend
	boolean confirm
	
	
	static belongsTo = [user: User]
	
	
	static constraints = {
		friend unique: 'user'
	}

	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		user index: "UserFriend_User_Idx"
		friend index: "UserFriend_Friend_Idx"
	}

}
