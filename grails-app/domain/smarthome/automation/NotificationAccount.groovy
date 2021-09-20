package smarthome.automation

import java.io.Serializable

import smarthome.automation.notification.NotificationSender
import smarthome.core.SmartHomeCoreConstantes
import smarthome.core.SmartHomeException
import smarthome.security.User
import grails.converters.JSON
import grails.validation.Validateable

/**
 * Compte pour les notifications (SMS, email, ...)
 *  
 * @author gregory
 *
 */
@Validateable
class NotificationAccount implements Serializable {

	NotificationAccountSender notificationAccountSender
	String config
	User user
	Date lastExecution
	String error
	

	// transients properties
	def jsonConfig = [:]


	static belongsTo = [user: User]

	static transients = ['jsonConfig']

	
	static constraints = {
		notificationAccountSender unique: ['user']
		config nullable: true
		lastExecution nullable: true
		error nullable: true
		jsonConfig bindable: true
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		user index: "NotificationAccount_User_Idx"
		error type: 'text'
	}


	void configToJson() {
		jsonConfig = config ? JSON.parse(config) : [:]
	}

	void configFromJson() {
		config = jsonConfig as JSON
	}

	/**
	 * Vérifie si autorisation pour utiliser le le service
	 *
	 */
	void assertAutorisation() throws SmartHomeException {
		if (notificationAccountSender.role) {
			if (!user.hasRole(notificationAccountSender.role)) {
				throw new SmartHomeException("Autorisation insuffisante pour utiliser le service ${notificationAccountSender.libelle} !")
			}
		}
	}
}
