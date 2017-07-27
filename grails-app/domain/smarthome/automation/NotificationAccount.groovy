package smarthome.automation

import smarthome.automation.notification.NotificationAccountEnum;
import smarthome.automation.notification.NotificationSender;
import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Compte pour les notifications (SMS, email, ...)
 *  
 * @author gregory
 *
 */
@Validateable
class NotificationAccount {
	static belongsTo = [user: User]
	
	NotificationAccountEnum type
	String className 
	String config
	
	
	// propriétés utilisateur
	def jsonConfig = [:]
	
	static transients = ['jsonConfig']
	
	
    static constraints = {
		type unique: ['user']
		config nullable: true
		jsonConfig bindable: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		user index: "NotificationAccount_User_Idx"
	}
	
	
	/**
	 * Instancie un sender 
	 * 
	 * @return
	 */
	NotificationSender getSenderInstance() {
		try {
			return Class.forName(className).newInstance()
		} catch (Exception ex) {
			return null
		}	
	}
}
