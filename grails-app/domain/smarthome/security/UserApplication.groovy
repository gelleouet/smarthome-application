package smarthome.security

import java.io.Serializable;
import java.util.Set;

import smarthome.core.SmartHomeCoreConstantes;
import grails.validation.Validateable


/**
 * Les applications third-party de l'utilisateur (facebook, twitter, google, etc...)
 * Généralement des application OAuth
 * 
 * @author gregory
 *
 */
@Validateable
class UserApplication implements Serializable {

	static belongsTo = [user: User]
	
	String applicationId
	String token
	
	
	static transients = ['publicToken']
	
	// le token est encodé, on utilise cette propriété pour stocker la valeur non encodée
	// pour la retourner au service appelant
	String publicToken 
	
	
	static constraints = {
		user unique: 'applicationId'
		token unique: true	
	}
	

	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		user index: "UserApplication_User_Idx"
		token index: "UserApplication_Token_Idx"
	}

}
