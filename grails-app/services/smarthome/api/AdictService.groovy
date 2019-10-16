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
import smarthome.automation.deviceType.CompteurGaz
import smarthome.core.AbstractService
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException
import smarthome.security.User


/**
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class AdictService extends AbstractService {

	GrailsApplication grailsApplication
	NotificationAccountService notificationAccountService
	NotificationAccountSenderService notificationAccountSenderService
	AdictApi adictApi
	DeviceService deviceService
	DeviceValueService deviceValueService
	HouseService houseService


	/**
	 * Création d'un token
	 * 
	 * @param user
	 * @param code
	 * @param usage_point_id
	 * @throws SmartHomeException
	 */
	String token() throws SmartHomeException {
		JSONElement result = adictApi.token()
		return result.access_token
	}



	/**
	 * Création d'une accreditation sur un PCE
	 * 
	 * @param user
	 * @param accreditation
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void accreditation(User user, AdictAccreditation accreditation) throws SmartHomeException {
		String token = this.token()

		// au 1er appel des consos, on va essayer de remonter 6 mois de données.
		// donc l'autorisation doit démarrer au moins à cette date
		// on veut que cette accréditation dure 1 an à partir de maintenant
		use(TimeCategory) {
			accreditation.debutAutorisation = new Date() - 6.months
			accreditation.finAutorisation = new Date() + 1.year
		}

		JSONElement result = adictApi.accreditation(token, accreditation)

		NotificationAccountSender accountSender = notificationAccountSenderService.findByLibelle(grailsApplication.config.grdf.appName)
		NotificationAccount notificationAccount = notificationAccountService.findByUserAndSender(user, accountSender)

		if (!notificationAccount) {
			notificationAccount = new NotificationAccount(user: user, notificationAccountSender: accountSender)
		}

		notificationAccount.jsonConfig.pce = accreditation.pce
		notificationAccount.jsonConfig.role = accreditation.role
		notificationAccount.jsonConfig.codePostal = accreditation.codePostal
		notificationAccount.jsonConfig.titulaireType = accreditation.titulaireType
		notificationAccount.jsonConfig.titulaireValeur = accreditation.titulaireValeur
		notificationAccount.jsonConfig.emailTitulaire = accreditation.emailTitulaire
		notificationAccount.jsonConfig.debutAutorisation = DateUtils.formatDateUser(accreditation.debutAutorisation)
		notificationAccount.jsonConfig.finAutorisation = DateUtils.formatDateUser(accreditation.finAutorisation)
		notificationAccount.configFromJson()
		notificationAccountService.save(notificationAccount)

		// création du device et association avec le compteur de la maison principale
		Device dataDevice = this.findOrCreateDevice(notificationAccount)
		deviceService.saveWithoutAuthorize(dataDevice)
		houseService.bindDefault(user, [compteurGaz: dataDevice])
	}


	/**
	 * API consommation informative pour appel user
	 * 
	 * @param user
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	List<JSONElement> consommationInformative(User user) throws SmartHomeException {
		NotificationAccount notificationAccount = notificationAccount(user)

		if (!notificationAccount) {
			throw new SmartHomeException("Config introuvable pour l'utilisateur !")
		}

		return consommationInformative(notificationAccount)
	}


	/**
	 * API consommation informative
	 * 
	 * @param notificationAccount
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	List<JSONElement> consommationInformative(NotificationAccount notificationAccount) throws SmartHomeException {
		notificationAccount.configToJson()

		if (!notificationAccount.jsonConfig.pce) {
			throw new SmartHomeException("pce is required !")
		}

		if (!notificationAccount.jsonConfig.role) {
			throw new SmartHomeException("role is required !")
		}

		Device dataDevice = this.findOrCreateDevice(notificationAccount)
		deviceService.saveWithoutAuthorize(dataDevice)
		Date start
		// on récupère les données de la veille max
		Date end = new Date().clearTime() - 1

		use(TimeCategory) {
			if (notificationAccount.jsonConfig.last_consommationInformative) {
				// les données remontent tous les jours. on se place sur le jour suivant
				start = new Date(notificationAccount.jsonConfig.last_consommationInformative as Long)
				start = (start + 1.day).clearTime()

				// un appel ne peut porter que sur 6 mois max
				if ((end - start).months >= 6) {
					end = start + 6.months
				}
			} else {
				// un appel ne peut porter que sur 6mois max, on borne à 6 mois
				start = end - 6.months
			}
		}

		// récupère un token valide avant d'appeler l'api consommation
		String token = this.token()

		List<JSONElement> datapoints = adictApi.consommationInformative(token,
				notificationAccount.jsonConfig.pce,
				notificationAccount.jsonConfig.role,
				start, end)

		if (!datapoints) {
			throw new SmartHomeException("Adict#consommationInformative : no values !")
		}

		dataDevice.value = datapoints.last().value.toString()
		dataDevice.dateValue = datapoints.last().timestamp
		dataDevice.metavalue('conso').value =  datapoints.last().conso.toString()
		// met à jour le coef de conversion avec le dernier renvoyé (juste pour information)
		dataDevice.metadata('coefConversion').value =  datapoints.last().coeffConversion
		deviceService.saveWithoutAuthorize(dataDevice)

		// insère les données sur le device et historise les valeur
		for (JSONElement datapoint : datapoints) {
			// enregistre les consos sur les données aggrégées par jour
			deviceValueService.addDeviceValueDay(dataDevice, datapoint.timestamp,
					'consosum', datapoint.conso)
		}

		// aggrège les données sur le mois
		deviceValueService.sumValueMonthFromValueDay(dataDevice, 'consosum',
				DateUtils.firstDayInMonth(datapoints.first().timestamp),
				datapoints.last().timestamp)

		notificationAccount.jsonConfig.last_consommationInformative = dataDevice.dateValue.time
		notificationAccount.configFromJson()
		notificationAccountService.save(notificationAccount)

		return datapoints
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

		if (notificationAccount.jsonConfig.pce) {
			device = deviceService.findByMac(notificationAccount.user,
					notificationAccount.jsonConfig.pce)
		}

		return device
	}


	/**
	 * Recherche la config associée
	 * 
	 * @param user
	 * @return
	 */
	NotificationAccount notificationAccount(User user) {
		NotificationAccount account = notificationAccountService.findByUserAndLibelleSender(user,
				grailsApplication.config.grdf.appName)

		if (account) {
			account.configToJson()
		}

		return account
	}


	/**
	 * Recherche et/ou création du device associé au compte
	 * 
	 * @param notificationAccount
	 * @return
	 * @throws SmartHomeException
	 */
	private Device findOrCreateDevice(NotificationAccount notificationAccount) throws SmartHomeException {
		String defaultCompteurLabel = grailsApplication.config.grdf.compteurLabel

		// recherche du device associé au compte adict
		// (il y a déjà eu un appel réussi avec récupération des données)
		Device dataDevice = getDeviceFromConfig(notificationAccount)

		// vérifie quand même que le device n'a pas été modifiée entre temps
		if (!dataDevice) {
			dataDevice = new Device(
					user: notificationAccount.user,
					mac: notificationAccount.jsonConfig.pce,
					label: defaultCompteurLabel,
					deviceType: DeviceType.findByImplClass(CompteurGaz.name))
		}

		// ajout ou update config device
		dataDevice.addMetadata('modele', [label: 'Modèle', value: 'Gazpar'])
		dataDevice.addMetadata('coefConversion', [label: 'Coefficient conversion'])
		dataDevice.addMetavalue('conso', [unite: 'Wh', label: 'Période consommation', trace: true])

		return dataDevice
	}
}