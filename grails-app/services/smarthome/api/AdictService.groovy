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
		notificationAccount.configFromJson()
		notificationAccountService.save(notificationAccount)

		// création du device et association avec le compteur de la maison principale
		Device dataDevice = this.findOrCreateDevice(notificationAccount)
		deviceService.saveWithoutAuthorize(dataDevice)
		houseService.bindDefault(user, [compteurGaz: dataDevice])
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