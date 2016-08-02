package smarthome.automation.notification

import org.apache.commons.lang.StringEscapeUtils;

import smarthome.core.ExchangeType;
import smarthome.core.SmartHomeException

class EmailNotificationSender extends AbstractNotificationSender {

	@Override
	void send(Notification notification) throws SmartHomeException {
		// transformation du texte en HTML
		notification.message = notification.message.replaceAll("[\r|\n]", "<br/>")
		notification.message = StringEscapeUtils.escapeHtml(notification.message)
		
		// envoi d'un message simple AMQP
		config.notificationAccountService.sendAsynchronousMessage("amq.direct",
			"smarthome.automation.notificationService.sendEmail",
			[notification: notification, email: notification.user.username], ExchangeType.DIRECT)
	}


	@Override
	String getDescription() {
		return "Smarthome Email Notification";
	}

}
