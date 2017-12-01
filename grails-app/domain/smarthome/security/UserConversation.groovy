package smarthome.security

import java.io.Serializable;
import java.util.Set;

import smarthome.core.SmartHomeCoreConstantes;
import grails.validation.Validateable


/**
 * La conversation en cours d'un utilisateur
 * 
 * @author gregory
 *
 */
@Validateable
class UserConversation implements Serializable {

	static belongsTo = [user: User]
	
	Date dateQuery
	String conversationId
	String query
	
	
	static constraints = {
		
	}
	

	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		user index: "UserConversation_User_Idx"
	}

}
