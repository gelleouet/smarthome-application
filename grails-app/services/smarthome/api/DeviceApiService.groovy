package smarthome.api

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.hibernate.FetchMode
import org.springframework.transaction.annotation.Transactional

import smarthome.automation.Device
import smarthome.automation.DeviceMetavalue
import smarthome.automation.DeviceService
import smarthome.automation.DeviceType
import smarthome.automation.DeviceValue
import smarthome.automation.DeviceValueService
import smarthome.automation.deviceType.Capteur
import smarthome.core.AbstractService
import smarthome.core.AsynchronousWorkflow
import smarthome.core.SmartHomeException
import smarthome.security.UserApplication
import smarthome.security.UserApplicationService


/**
 * Définition des URLS API : @see UrlMappings
 * @author gregory
 *
 */
class DeviceApiService extends AbstractService {

	UserApplicationService userApplicationService
	DeviceService deviceService
	DeviceValueService deviceValueService
	GrailsApplication grailsApplication


	/**
	 * Réception de données
	 * 
	 * @param command
	 * @return device
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousWorkflow("deviceApiService.push")
	Device push(PushCommand command, String token) throws SmartHomeException {
		if (!command.datas) {
			throw new SmartHomeException("datas are required !")
		}

		UserApplication userApplication = userApplicationService.authenticateToken(token, command.application)

		// recherche du device
		Device device = Device.findByMacAndUser(command.name, userApplication.user, [lock: true])
		DeviceMetavalue metavalue

		// création d'un nouveau device "à la volée"
		if (!device) {
			device = new Device(user: userApplication.user, mac: command.name, label: command.name,
			deviceType: DeviceType.findByImplClass(Capteur.name))
		}

		// init la date du device avec la date la plus récente des datas
		def lastData = command.datas.max { it.timestamp as Long }
		device.dateValue = new Date(lastData.timestamp as Long)

		// gestion de la metavalue si demandé avec la valeur la plus récente
		if (command.metaname) {
			metavalue = device.addMetavalue(command.metaname, [trace: true, unite: command.unite,
				label: command.metaname, value: lastData.value.toString()])
		}

		// init la value du device avec la value la plus récente des datas si pas de metavalue ou
		// si celle-ci est synchro sur le device (ie main)
		if (!metavalue || metavalue?.main) {
			device.value = lastData.value.toString()
		}

		// enregistrement avec les values nécessaires si nouveau device
		deviceService.save(device)

		// ajoute toutes les valeurs
		for (def data : command.datas) {
			def convertValue = DeviceValue.parseDoubleValue(data.value.toString())

			if (convertValue != null) {
				deviceValueService.save(new DeviceValue(device: device, name: metavalue?.name,
				value: convertValue, dateValue: new Date(data.timestamp as Long)))
			}
		}

		return device
	}


	/**
	 * Récupération de données depuis le serveur
	 * 
	 * @param command
	 * @param token
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	FetchResult fetch(FetchCommand command, String token) throws SmartHomeException {
		UserApplication userApplication = userApplicationService.authenticateToken(token, command.application)

		if (!command.limit || command.limit > grailsApplication.config.smarthome.pagination.maxBackend) {
			command.limit = grailsApplication.config.smarthome.pagination.maxBackend
		}

		// recherche du device pour le user
		Device device = Device.findByMacAndUser(command.name, userApplication.user)

		if (!device) {
			throw new SmartHomeException("Device ${command.name} not found !")
		}

		// recherche des values
		def values = DeviceValue.createCriteria().list([offset: command.offset, max: command.limit]) {
			eq "device", device

			if (command.metaname == "null") {
				isNull "name"
			} else {
				eq "name", command.metaname
			}

			if (command.start) {
				ge "dateValue", command.start
			}

			if (command.end) {
				le "dateValue", command.end
			}

			order "dateValue", command.order
		}

		FetchResult result = new FetchResult()
		result.count = values.totalCount
		result.size = values.size()
		result.offset = command.offset

		values.each {
			result.datas << [value: it.value, timestamp: it.dateValue.time]
		}

		return result
	}
}