package smarthome.automation

import java.io.Serializable;

import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Description des maisons d'un user
 *  
 * @author gregory
 *
 */
@Validateable
class Mode implements Serializable {
	static belongsTo = [user: User]
	
	String name
	Integer status
	
	
	static transients = ['status']
	
	
    static constraints = {
		status bindable: true, nullable: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		user index: "Mode_User_Idx"
		name length:32
	}
}
