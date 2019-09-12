package smarthome.automation.datasource

import org.apache.commons.logging.LogFactory;
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

	private static final log = LogFactory.getLog(this)
	
	@Autowired
	DataConnectService dataConnectService


	/**
	 * Point d'entrée exécution provider
	 * 
	 * @see smarthome.automation.datasource.AbstractDataSourceProvider#execute(smarthome.automation.NotificationAccount)
	 */
	@Override
	void execute(NotificationAccount notificationAccount) throws SmartHomeException {
		// une erreur sur le refersh_token est bloquant car les autres appels
		// vont forcément échouer si les tokens ne sont pas à jour
		try {
			dataConnectService.refresh_token(notificationAccount)
		} catch (SmartHomeException ex) {
			throw new SmartHomeException("refresh_token : ${ex.message}")
		}
		
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
