package smarthome.automation.notification

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionDefinition;

import smarthome.automation.Notification;
import smarthome.automation.NotificationService;
import smarthome.core.ScriptUtils;
import smarthome.core.SmartHomeException


/**
 * Envoi des mails asynchrone.
 * Le mail est préparé par le sender puis un message amq est envoyé au gestionnaire de mail
 * 
 * @author Gregory
 *
 */
class EmailNotificationSender extends AbstractNotificationSender {

	@Autowired
	NotificationService notificationService
	
	
	@Override
	void send(Notification notification, Map context) throws SmartHomeException {
		Map email = [title: context.event.libelle]
		
		if (notification.convertMessage) {
			email.message = notification.convertMessage.replaceAll("[\r|\n]", "<br/>")
			//email.message = StringEscapeUtils.escapeHtml(email.message)
		}
		
		// l'objet est en script
		if (notification.jsonParameters.subject) {
			Notification.withTransaction([propagationBehavior: TransactionDefinition.PROPAGATION_REQUIRES_NEW, readOnly: true]) {
				email.subject = ScriptUtils.runScript(notification.jsonParameters.subject, context)?.toString()
			}
		}
		
		if (!email.subject) {
			email.subject = notification.description
		}
		
		if (notification.jsonParameters.to) {
			email.to = notification.jsonParameters.to
		} else {
			email.to = notification.user.username
		}
		
		email.cc = notification.jsonParameters.cc
		email.bcc = notification.jsonParameters.bcc
		
		notificationService.sendEmail(email)
	}

}
