package smarthome.automation

import java.io.Serializable

import smarthome.core.ApplicationUtils
import smarthome.core.SmartHomeCoreConstantes
import grails.validation.Validateable

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
	/**
	 * Pour les services automatiques (ex : datasource)
	 */
	String cron


	static constraints = {
		libelle unique: true
		role nullable: true
		cron nullable: true
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
		def instance = Class.forName(implClass).newInstance()
		ApplicationUtils.autowireBean(instance)
		return instance
	}
}
