package smarthome.api

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.json.JSONElement
import org.springframework.transaction.annotation.Transactional

import groovy.time.TimeCategory
import smarthome.automation.Device
import smarthome.automation.DeviceService
import smarthome.automation.DeviceType
import smarthome.automation.DeviceValue
import smarthome.automation.DeviceValueService
import smarthome.automation.NotificationAccount
import smarthome.automation.NotificationAccountSender
import smarthome.automation.NotificationAccountSenderService
import smarthome.automation.NotificationAccountService
import smarthome.automation.deviceType.TeleInformation
import smarthome.core.AbstractService
import smarthome.core.SmartHomeException
import smarthome.security.User


/**
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class DataConnectService extends AbstractService {

	GrailsApplication grailsApplication
	NotificationAccountService notificationAccountService
	NotificationAccountSenderService notificationAccountSenderService
	DataConnectApi dataConnectApi
	DeviceService deviceService
	DeviceValueService deviceValueService


	/**
	 * URL pour le consentement
	 *
	 * @return
	 */
	String authorize_uri() {
		return dataConnectApi.authorize_uri()
	}


	/**
	 * Appel de l'API Token suite au redirect d'Enedis pour récupérer les tokens
	 * de connexion
	 * 
	 * @param user
	 * @param code
	 * @param usage_point_id
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void authorization_code(User user, String code, String usage_point_id) throws SmartHomeException {
		JSONElement result = dataConnectApi.authorization_code(code)

		NotificationAccountSender accountSender = notificationAccountSenderService.findByLibelle(grailsApplication.config.enedis.appName)
		NotificationAccount notificationAccount = notificationAccountService.findByUserAndSender(user, accountSender)

		if (!notificationAccount) {
			notificationAccount = new NotificationAccount(user: user, notificationAccountSender: accountSender)
		}

		notificationAccount.jsonConfig.access_token = result.access_token
		notificationAccount.jsonConfig.refresh_token = result.refresh_token
		notificationAccount.jsonConfig.usage_point_id = usage_point_id
		notificationAccount.configFromJson()
		notificationAccountService.save(notificationAccount)
	}


	/**
	 *
	 * @param user
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void refresh_token(User user) throws SmartHomeException {
		NotificationAccountSender accountSender = notificationAccountSenderService.findByLibelle(grailsApplication.config.enedis.appName)
		NotificationAccount notificationAccount = notificationAccountService.findByUserAndSender(user, accountSender)

		if (!notificationAccount) {
			throw new SmartHomeException("Config introuvable pour l'utilisateur !")
		}

		refresh_token(notificationAccount)
	}


	/**
	 * 
	 * @param notificationAccount
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void refresh_token(NotificationAccount notificationAccount) throws SmartHomeException {
		notificationAccount.configToJson()

		if (!notificationAccount.jsonConfig.refresh_token) {
			throw new SmartHomeException("refresh_token is required !")
		}

		JSONElement result = dataConnectApi.refresh_token(notificationAccount.jsonConfig.refresh_token)


		notificationAccount.jsonConfig.access_token = result.access_token
		notificationAccount.jsonConfig.refresh_token = result.refresh_token
		notificationAccount.configFromJson()

		notificationAccountService.save(notificationAccount)
	}


	/**
	 * Exécution de l'API consumptionLoadCurve
	 * Récupère les infos d'un user à partir de la config
	 * 
	 * @param notificationAccount
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void consumptionLoadCurve(User user) throws SmartHomeException {
		NotificationAccountSender accountSender = notificationAccountSenderService.findByLibelle(grailsApplication.config.enedis.appName)
		NotificationAccount notificationAccount = notificationAccountService.findByUserAndSender(user, accountSender)

		if (!notificationAccount) {
			throw new SmartHomeException("Config introuvable pour l'utilisateur !")
		}

		consumptionLoadCurve(notificationAccount)
	}


	/**
	 * Exécution de l'API consumptionLoadCurve direct sur une config
	 * 
	 * @param user
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	List<JSONElement> consumptionLoadCurve(NotificationAccount notificationAccount) throws SmartHomeException {
		notificationAccount.configToJson()

		if (!notificationAccount.jsonConfig.access_token) {
			throw new SmartHomeException("access_token is required !")
		}

		if (!notificationAccount.jsonConfig.usage_point_id) {
			throw new SmartHomeException("usage_point_id is required !")
		}

		// avec le token on peut attaquer l'api metering data
		// il faut savoir si des données sont déjà remontées sur un device
		// cela permet de déterminer la plage de date pour le chargement des données
		Device dataDevice

		if (notificationAccount.jsonConfig.device_id) {
			dataDevice = deviceService.findById(notificationAccount.jsonConfig.device_id)
		} else {
			// création d'un device auto
			dataDevice = new Device(
					user: notificationAccount.user,
					mac: notificationAccount.jsonConfig.usage_point_id,
					label: notificationAccount.jsonConfig.usage_point_id,
					deviceType: DeviceType.findByImplClass(TeleInformation))
		}

		Date start
		Date end = new Date().clearTime() - 1 // le champ end est exclusif, donc récupère les données de la veille

		use(TimeCategory) {
			if (dataDevice.dateValue) {
				// si un appel a déjà été fait, il correspond à 23h30 du jour concerné
				// donc on doit récupéré les données du jour suivant
				start = (dataDevice.dateValue + 1.day).clearTime()
			} else {
				// un appel ne peut porter que sur 7 jours consécutifs
				start = end - 7.days
			}
		}

		List<JSONElement> datapoints = dataConnectApi.consumptionLoadCurve(
				start, end,
				notificationAccount.jsonConfig.usage_point_id,
				notificationAccount.jsonConfig.access_token)

		if (!datapoints) {
			throw new SmartHomeException("consumptionLoadCurve : no values !")
		}


		dataDevice.value = datapoints.last().value
		dataDevice.dateValue = datapoints.last().timestamp
		dataDevice.addMetavalue('baseinst', [unite: 'Wh', label: 'Consommation moyenne sur 30 minutes'])
		dataDevice.metavalue('baseinst').value =  datapoints.last().wh.toString()
		deviceService.save(dataDevice)

		// insère les données sur le device et historise les valeur
		for (JSONElement datapoint : datapoints) {
			deviceValueService.save(new DeviceValue(
					device: dataDevice,
					value: DeviceValue.parseDoubleValue(datapoint.value),
					dateValue: datapoint.timestamp))

			deviceValueService.save(new DeviceValue(
					device: dataDevice,
					name: 'baseinst', value: datapoint.wh,
					dateValue: datapoint.timestamp))
		}

		notificationAccount.jsonConfig.device_id = dataDevice.id
		notificationAccount.configFromJson()
		notificationAccountService.save(notificationAccount)

		return datapoints
	}
}