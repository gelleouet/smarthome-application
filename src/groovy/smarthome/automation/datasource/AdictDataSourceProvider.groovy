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
		try {
			adictService.consommationInformative(notificationAccount)
		} catch (SmartHomeException ex) {
			log.error("ADICT.consommationInformative : ${ex.message}")
		}
	}
}
