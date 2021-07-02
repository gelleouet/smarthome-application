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
class UserExport implements Serializable {

	User user
	String exportImpl
	
	
	static belongsTo = [user: User]
	
	
	static constraints = {
		user unique: 'exportImpl'
	}

	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
	}

}
