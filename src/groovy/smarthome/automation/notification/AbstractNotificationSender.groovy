package smarthome.automation.notification

import org.apache.commons.lang.StringUtils

import smarthome.core.SmartHomeException

abstract class AbstractNotificationSender implements NotificationSender {

	@Override
	final String getName() {
		return this.getClass().name
	}


	@Override
	final String getSimpleName() {
		return StringUtils.uncapitalize(this.getClass().simpleName.replace("NotificationSender", "")
				.replace("DataSourceProvider", ""))
	}
}
