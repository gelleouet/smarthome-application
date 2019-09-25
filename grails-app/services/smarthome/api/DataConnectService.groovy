package smarthome.api

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.json.JSONElement
import org.springframework.transaction.annotation.Transactional

import groovy.time.TimeCategory
import smarthome.automation.Device
import smarthome.automation.DeviceService
import smarthome.automation.DeviceType
import smarthome.automation.DeviceValue
import smarthome.automation.DeviceValueDay
import smarthome.automation.DeviceValueService
import smarthome.automation.HouseService
import smarthome.automation.NotificationAccount
import smarthome.automation.NotificationAccountSender
import smarthome.automation.NotificationAccountSenderService
import smarthome.automation.NotificationAccountService
import smarthome.automation.deviceType.TeleInformation
import smarthome.core.AbstractService
import smarthome.core.DateUtils
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
	HouseService houseService


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
	JSONElement authorization_code(User user, String code, String usage_point_id) throws SmartHomeException {
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

		// création du device et association avec le compteur de la maison principale
		Device dataDevice = this.findOrCreateDevice(notificationAccount)
		deviceService.saveWithoutAuthorize(dataDevice)
		houseService.bindDefault(user, [compteur: dataDevice])

		return result
	}


	/**
	 *
	 * @param user
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	JSONElement refresh_token(User user) throws SmartHomeException {
		NotificationAccountSender accountSender = notificationAccountSenderService.findByLibelle(grailsApplication.config.enedis.appName)
		NotificationAccount notificationAccount = notificationAccountService.findByUserAndSender(user, accountSender)

		if (!notificationAccount) {
			throw new SmartHomeException("Config introuvable pour l'utilisateur !")
		}

		return refresh_token(notificationAccount)
	}


	/**
	 * 
	 * @param notificationAccount
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	JSONElement refresh_token(NotificationAccount notificationAccount) throws SmartHomeException {
		notificationAccount.configToJson()

		if (!notificationAccount.jsonConfig.refresh_token) {
			throw new SmartHomeException("refresh_token is required !")
		}

		JSONElement result = dataConnectApi.refresh_token(notificationAccount.jsonConfig.refresh_token)


		notificationAccount.jsonConfig.access_token = result.access_token
		notificationAccount.jsonConfig.refresh_token = result.refresh_token
		notificationAccount.configFromJson()
		notificationAccountService.save(notificationAccount)

		return result
	}


	/**
	 * Exécution de l'API consumptionLoadCurve
	 * Récupère les infos d'un user à partir de la config
	 * 
	 * @param notificationAccount
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	List<JSONElement> consumptionLoadCurve(User user) throws SmartHomeException {
		NotificationAccountSender accountSender = notificationAccountSenderService.findByLibelle(grailsApplication.config.enedis.appName)
		NotificationAccount notificationAccount = notificationAccountService.findByUserAndSender(user, accountSender)

		if (!notificationAccount) {
			throw new SmartHomeException("Config introuvable pour l'utilisateur !")
		}

		return consumptionLoadCurve(notificationAccount)
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
		Device dataDevice = this.findOrCreateDevice(notificationAccount)
		Date start
		Date end = new Date().clearTime()// le champ end est exclusif, donc récupère les données de la veille

		use(TimeCategory) {
			if (notificationAccount.jsonConfig.last_consumption_load_curve) {
				// si un appel a déjà été fait, il correspond à 23h30 du jour concerné
				// donc on doit récupéré les données du jour suivant
				start = new Date(notificationAccount.jsonConfig.last_consumption_load_curve as Long)
				start = (start + 1.day).clearTime()

				// un appel ne peut porter que sur 7 jours
				if ((end - start).days > 7) {
					end = start + 7.days
				}
			} else {
				// un appel ne peut porter que sur 7 jours consécutifs
				start = end - 7.days
			}
		}

		List<JSONElement> datapoints = dataConnectApi.consumption_load_curve(
				start, end,
				notificationAccount.jsonConfig.usage_point_id,
				notificationAccount.jsonConfig.access_token)

		if (!datapoints) {
			throw new SmartHomeException("DataConnect#consumptionLoadCurve : no values !")
		}

		dataDevice.value = datapoints.last().value
		dataDevice.dateValue = datapoints.last().timestamp
		dataDevice.metavalue('baseinst').value =  datapoints.last().wh.toString()
		deviceService.saveWithoutAuthorize(dataDevice)

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

		notificationAccount.jsonConfig.last_consumption_load_curve = dataDevice.dateValue.time
		notificationAccount.configFromJson()
		notificationAccountService.save(notificationAccount)

		return datapoints
	}


	/**
	 * Exécution de l'API dailyConsumption
	 * Récupère les infos d'un user à partir de la config
	 *
	 * @param notificationAccount
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	List<JSONElement> dailyConsumption(User user) throws SmartHomeException {
		NotificationAccountSender accountSender = notificationAccountSenderService.findByLibelle(grailsApplication.config.enedis.appName)
		NotificationAccount notificationAccount = notificationAccountService.findByUserAndSender(user, accountSender)

		if (!notificationAccount) {
			throw new SmartHomeException("Config introuvable pour l'utilisateur !")
		}

		return dailyConsumption(notificationAccount)
	}


	/**
	 * Exécution de l'API dailyConsumption direct sur une config
	 *
	 * @param user
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	List<JSONElement> dailyConsumption(NotificationAccount notificationAccount) throws SmartHomeException {
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
		Device dataDevice = this.findOrCreateDevice(notificationAccount)
		deviceService.saveWithoutAuthorize(dataDevice)
		Date start
		Date end = new Date().clearTime()// le champ end est exclusif (donnée de la veille)

		use(TimeCategory) {
			if (notificationAccount.jsonConfig.last_daily_consumption) {
				// si un appel a déjà été fait, il correspond au jour concerné
				// donc on doit récupéré les données du jour suivant
				start = new Date(notificationAccount.jsonConfig.last_daily_consumption as Long)
				start = (start + 1.day).clearTime()

				// un appel ne peut porter que sur 365 jours
				if ((end - start).days > 365) {
					end = start + 365.days
				}
			} else {
				// un appel ne peut porter que sur 365 jours consécutifs
				start = end - 365.days
			}
		}

		List<JSONElement> datapoints = dataConnectApi.daily_consumption(
				start, end,
				notificationAccount.jsonConfig.usage_point_id,
				notificationAccount.jsonConfig.access_token)

		if (!datapoints) {
			throw new SmartHomeException("DataConnect#dailyConsumption : no values !")
		}

		// insère les données sur les valeurs aggrégées jour du device
		for (JSONElement datapoint : datapoints) {
			deviceValueService.save(new DeviceValueDay(
					name: 'basesum',
					device: dataDevice,
					value: DeviceValue.parseDoubleValue(datapoint.value),
					dateValue: datapoint.timestamp))
		}

		Date dateFirstValue = datapoints.first().timestamp
		Date dateLastValue = datapoints.last().timestamp
		deviceValueService.sumValueMonthFromValueDay(dataDevice, 'basesum',
				DateUtils.firstDayInMonth(dateFirstValue), dateLastValue)

		notificationAccount.jsonConfig.last_daily_consumption = dateLastValue.time
		notificationAccount.configFromJson()
		notificationAccountService.save(notificationAccount)

		return datapoints
	}


	/**
	 * Exécution de l'API consumptionMaxPower
	 * Récupère les infos d'un user à partir de la config
	 *
	 * @param notificationAccount
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	List<JSONElement> consumptionMaxPower(User user) throws SmartHomeException {
		NotificationAccountSender accountSender = notificationAccountSenderService.findByLibelle(grailsApplication.config.enedis.appName)
		NotificationAccount notificationAccount = notificationAccountService.findByUserAndSender(user, accountSender)

		if (!notificationAccount) {
			throw new SmartHomeException("Config introuvable pour l'utilisateur !")
		}

		return consumptionMaxPower(notificationAccount)
	}

	/**
	 * Exécution de l'API consumptionMaxPower direct sur une config
	 *
	 * @param user
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	List<JSONElement> consumptionMaxPower(NotificationAccount notificationAccount) throws SmartHomeException {
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
		Device dataDevice = this.findOrCreateDevice(notificationAccount)
		deviceService.saveWithoutAuthorize(dataDevice)
		Date start
		Date end = new Date().clearTime()// le champ end est exclusif (donnée de la veille)

		use(TimeCategory) {
			if (notificationAccount.jsonConfig.last_consumption_max_power) {
				// si un appel a déjà été fait, il correspond au jour concerné
				// donc on doit récupéré les données du jour suivant
				start = new Date(notificationAccount.jsonConfig.last_consumption_max_power as Long)
				start = (start + 1.day).clearTime()

				// un appel ne peut porter que sur 365 jours
				if ((end - start).days > 365) {
					end = start + 365.days
				}
			} else {
				// un appel ne peut porter que sur 365 jours consécutifs
				start = end - 365.days
			}
		}

		List<JSONElement> datapoints = dataConnectApi.consumption_max_power(
				start, end,
				notificationAccount.jsonConfig.usage_point_id,
				notificationAccount.jsonConfig.access_token)

		if (!datapoints) {
			throw new SmartHomeException("DataConnect#consumptionMaxPower : no values !")
		}

		// insère les données sur les valeurs aggrégées jour du device
		for (JSONElement datapoint : datapoints) {
			deviceValueService.save(new DeviceValueDay(
					name: 'max',
					device: dataDevice,
					value: DeviceValue.parseDoubleValue(datapoint.value),
					dateValue: datapoint.timestamp))
		}

		Date dateFirstValue = datapoints.first().timestamp
		Date dateLastValue = datapoints.last().timestamp
		deviceValueService.maxValueMonthFromValueDay(dataDevice, 'max',
				DateUtils.firstDayInMonth(dateFirstValue), dateLastValue)

		notificationAccount.jsonConfig.last_consumption_max_power = dateLastValue.time
		notificationAccount.configFromJson()
		notificationAccountService.save(notificationAccount)

		return datapoints
	}


	private Device findOrCreateDevice(NotificationAccount notificationAccount) throws SmartHomeException {
		String defaultCompteurLabel = grailsApplication.config.enedis.compteurLabel

		// recherche du device associé au compte dataconnect
		// (il y a déjà eu un appel réussi avec récupération des données)
		Device dataDevice = getDeviceFromConfig(notificationAccount)

		// vérifie quand même que le device n'a pas été modifiée entre temps
		if (!dataDevice) {
			dataDevice = new Device(
					user: notificationAccount.user,
					unite: 'W',
					mac: notificationAccount.jsonConfig.usage_point_id,
					label: defaultCompteurLabel,
					deviceType: DeviceType.findByImplClass(TeleInformation.name))
		}

		// ajout ou update config device
		dataDevice.addMetadata('modele', [label: 'Modèle', value: 'Linky'])
		dataDevice.addMetavalue('baseinst', [unite: 'Wh', label: 'Consommation moyenne sur 30 minutes', trace: true])
		dataDevice.addMetavalue('opttarif', [label: 'Option tarifaire', value: 'BASE'])

		return dataDevice
	}


	/**
	 * Retourne le device associé à la config
	 * 
	 * @param notificationAccount
	 * @return
	 */
	Device getDeviceFromConfig(NotificationAccount notificationAccount) {
		Device device

		if (!notificationAccount.jsonConfig) {
			notificationAccount.configToJson()
		}

		if (notificationAccount.jsonConfig.usage_point_id) {
			device = deviceService.findByMac(notificationAccount.user,
					notificationAccount.jsonConfig.usage_point_id)
		}

		return device
	}
}