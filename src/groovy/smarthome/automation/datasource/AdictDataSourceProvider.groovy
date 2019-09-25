package smarthome.automation.datasource

import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired

import smarthome.api.AdictService
import smarthome.automation.NotificationAccount
import smarthome.core.SmartHomeException


/**
 * Provider DataConnect d'Enedis
 * @see smarthome.api.DataConnectService
 * 
 * @author gregory.elleouet@gmail.com<Grégory Elléoouet>
 *
 */
class AdictDataSourceProvider extends AbstractDataSourceProvider {

	private static final log = LogFactory.getLog(this)

	@Autowired
	AdictService adictService


	/**
	 * Point d'entrée exécution provider
	 * 
	 * @see smarthome.automation.datasource.AbstractDataSourceProvider#execute(smarthome.automation.NotificationAccount)
	 */
	@Override
	void execute(NotificationAccount notificationAccount) throws SmartHomeException {
		// les autres appels sont indépendants. on peut tous les lancer même si un plante
		// on log tout de même les erreurs

		try {
			dataConnectService.consumptionLoadCurve(notificationAccount)
		} catch (SmartHomeException ex) {
			log.error("consumptionLoadCurve : ${ex.message}")
		}

		try {
			dataConnectService.dailyConsumption(notificationAccount)
		} catch (SmartHomeException ex) {
			log.error("dailyConsumption : ${ex.message}")
		}

		try {
			dataConnectService.consumptionMaxPower(notificationAccount)
		} catch (SmartHomeException ex) {
			log.error("consumptionMaxPower : ${ex.message}")
		}


	}
}
