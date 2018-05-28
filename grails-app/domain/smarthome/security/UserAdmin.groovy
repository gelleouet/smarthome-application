package smarthome.security

import java.io.Serializable;
import java.util.Set;

import smarthome.core.SmartHomeCoreConstantes;
import grails.validation.Validateable


/**
 * Un administrateur de plusieurs utilisateurs
 * 
 * @author gregory
 *
 */
@Validateable
class UserAdmin implements Serializable {

	User user
	User admin
	
	
	static constraints = {
		
	}

	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		admin index: "UserAdmin_Admin_Idx"
		id composite: ['user', 'admin']
		version false
	}

}
