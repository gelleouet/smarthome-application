package smarthome.automation

import java.io.Serializable;

import smarthome.core.SmartHomeCoreConstantes;
import grails.validation.Validateable;

/**
 * Déclaration des implémentations NotificationSender
 *  
 * @author gregory
 *
 */
@Validateable
class NotificationAccountSender implements Serializable {
	String libelle
	String implClass
	String role
	
	
    static constraints = {
		libelle unique: true
		role nullable: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		sort 'libelle'
	}
	
	
	/**
	 * Instance l'implémentation
	 * 
	 * @return
	 */
	def newNotificationSender() {
		Class.forName(implClass).newInstance()
	}
}
