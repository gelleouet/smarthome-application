package smarthome.automation.notification

import smarthome.core.SmartHomeException;

/**
 * Configuration et envoi de notification
 * 
 * @author Gregory
 *
 */
interface NotificationSender {

	/**
	 * Envoi de notification
	 * 
	 * @throws SmartHomeException
	 */
	void send(Notification notification) throws SmartHomeException
	
	
	/**
	 * Nom complet du sender
	 * 
	 * @return
	 */
	String getName()
	
	
	/**
	 * Nom simple du sender
	 * 
	 * @return
	 */
	String getSimpleName()
	
	
	/**
	 * Description utilisateur
	 * 
	 * @return
	 */
	String getDescription()
}
