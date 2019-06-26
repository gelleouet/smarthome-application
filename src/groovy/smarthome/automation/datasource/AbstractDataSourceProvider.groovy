package smarthome.automation.datasource

import smarthome.automation.Notification
import smarthome.automation.NotificationAccount
import smarthome.automation.notification.AbstractNotificationSender
import smarthome.core.SmartHomeException

/**
 * @author gregory.elleouet@gmail.com <Grégory Elléoouet>
 *
 */
abstract class AbstractDataSourceProvider extends AbstractNotificationSender {

	abstract void execute(NotificationAccount notificationAccount) throws SmartHomeException

	@Override
	void send(Notification notification, Map context) throws SmartHomeException {
		// TODO Auto-generated method stub
	}
}
