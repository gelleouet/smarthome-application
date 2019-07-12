package smarthome.automation.datasource

import org.springframework.beans.factory.annotation.Autowired

import smarthome.api.DataConnectService
import smarthome.automation.NotificationAccount
import smarthome.core.SmartHomeException


/**
 * Provider DataConnect d'Enedis
 * @see smarthome.api.DataConnectService
 * 
 * @author gregory.elleouet@gmail.com<Grégory Elléoouet>
 *
 */
class DataConnectDataSourceProvider extends AbstractDataSourceProvider {

	@Autowired
	DataConnectService dataConnectService


	/**
	 * Point d'entrée exécution provider
	 * 
	 * @see smarthome.automation.datasource.AbstractDataSourceProvider#execute(smarthome.automation.NotificationAccount)
	 */
	@Override
	void execute(NotificationAccount notificationAccount) throws SmartHomeException {
		dataConnectService.refresh_token(notificationAccount)
		dataConnectService.consumptionLoadCurve(notificationAccount)
		dataConnectService.dailyConsumption(notificationAccount)
		dataConnectService.consumptionMaxPower(notificationAccount)
	}
}
