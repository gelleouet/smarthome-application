package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Description des maisons d'un user
 *  
 * @author gregory
 *
 */
@Validateable
class HouseMode {
	static belongsTo = [user: User]
	
	String name
	int status
	
	
	static transients = ['status']
	
	
    static constraints = {
		status bindable: true
    }
	
	static mapping = {
		user index: "HouseMode_User_Idx"
	}
}
