package smarthome.automation


import grails.async.PromiseList
import grails.converters.JSON
import grails.plugin.cache.CachePut
import grails.plugin.cache.Cacheable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional
import smarthome.automation.deviceType.AbstractDeviceType
import smarthome.core.AbstractService
import smarthome.core.ApplicationUtils
import smarthome.core.AsynchronousMessage
import smarthome.core.Chronometre
import smarthome.core.ClassUtils
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException
import smarthome.core.chart.GoogleChart
import smarthome.security.User


class DeviceValueService extends AbstractService {

	public static final int MAX_DAY_WITHOUT_PROJECTION = 1
	public static final int MAX_DAY_PROJECTION_DAY = 31


	
	/**
	 * Enregistrement de plusieurs valeurs en batch
	 * 
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Collection<DeviceValue> saveAll(Collection<DeviceValue> values) {
		values?.each { value -> 
			save(value)
		}
		
		return values
	}
	
	
	/**
	 * Ajoute une nouvelle valeur. Si c'est la valeur la plus récente, met à jour le device
	 * 
	 * @param deviceValue
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DeviceValue addValue(DeviceValue deviceValue) throws SmartHomeException {
		this.save(deviceValue)

		if (deviceValue.dateValue > deviceValue.device.dateValue) {
			deviceValue.device.dateValue = deviceValue.dateValue
			deviceValue.device.value = deviceValue.value.toString()
			super.save(deviceValue.device)
		}

		return deviceValue
	}


	/**
	 * Suppression d'une valeur. Si dernière valeur, il faut ajuster la date du device 
	 * avec l'avant dernière valeur
	 * 
	 * @param deviceValue
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DeviceValue delete(DeviceValue deviceValue) throws SmartHomeException {
		// recherche d'une valeur plus récente
		DeviceValue findValue = DeviceValue.createCriteria().get {
			eq 'device', deviceValue.device
			gt 'dateValue', deviceValue.dateValue
			isNull 'name'
			maxResults 1
		}

		// c'était la valeur la plus récente, donc on doit rafraichir les infos générales de la sonde
		// avec la valeur précédente
		if (!findValue) {
			findValue = DeviceValue.createCriteria().get {
				eq 'device', deviceValue.device
				lt 'dateValue', deviceValue.dateValue
				isNull 'name'
				order 'dateValue', 'desc'
				maxResults 1
			}

			if (findValue) {
				deviceValue.device.dateValue = findValue.dateValue
				deviceValue.device.value = findValue.value.toString()
				super.save(deviceValue.device)
			}
		}

		deviceValue.delete()

		return deviceValue
	}


	/**
	 * Les dernières valeurs des devices
	 * 
	 * @param devices
	 * @param pagination
	 * @return
	 */
	List<DeviceValue> lastValuesByDevices(List<Device> devices, Map pagination) {
		def values
		Chronometre chrono = new Chronometre()

		if (devices) {
			def deviceIds = devices.collect { it.id }

			values = DeviceValue.createCriteria().list(pagination) {
				device {
					'in' 'id', deviceIds
				}
				join 'device'
				join 'device.deviceType'
				join 'device.user'
				order 'dateValue', 'desc'
			}
		}

		log.info "List ${devices?.size()} devices values : ${chrono.stop()}ms"

		return values
	}


	/**
	 * Charge les valeurs du device sur une période
	 *
	 * @param command
	 * @return List ou Map
	 * 
	 * @throws SmartHomeException
	 */
	def values(DeviceChartCommand command) throws SmartHomeException {
		def values = command.deviceImpl.values(command)
		log.info "Load values ${command.device.mac} (${command.viewMode}) : ${values?.size()}"
		return values
	}


	/**
	 * Trace le changement de valeur pour garder un historique
	 *
	 * @param deviceDatas les données device au format json au moment où il a changé. ne pas passer
	 * 	le device en domain, sinon il a peut-être été modifié entre temps
	 * 
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DeviceValue traceValue(def deviceDatas) throws SmartHomeException {
		if (! (deviceDatas?.id instanceof Number)) {
			log.error "Cannot trace value : no device id"
			return null
		}

		DeviceValue value, defaultValue
		Double doubleValue
		Device device = Device.read(deviceDatas.id as Long)
		AbstractDeviceType deviceType = device.newDeviceImpl()

		// On récupère la valeur du device depuis les données brutes
		doubleValue = DeviceValue.parseDoubleValue(deviceDatas.value)
		Date dateDeviceValue = DateUtils.parseJson(deviceDatas.jsonDateValue, 0)

		// trace la valeur principale du device
		if (deviceType.isTraceValue(doubleValue)) {
			defaultValue = new DeviceValue(device: device, value: doubleValue, dateValue: dateDeviceValue)

			if (!defaultValue.save()) {
				throw new SmartHomeException("Erreur trace valeur !", defaultValue)
			}

			// trace les metavalues depuis encore les données brutes
			// sinon si on récupère depuis le device attaché à la session, les données
			// ont pu changées par une autre action asynchrone et l'on va historiser les nouvelles valeurs
			// à la place des anciennes
			deviceDatas.metavalues?.each {
				if (it.value) {
					// si la meta est principale, pas besoin de tracer car déjà fait au niveau device
					// si meta virtuelle, pas besoin non car ca sera fait au niveau du device virtuel
					// sinon on regarde si activée au niveau meta
					if (it.trace && !it.main && !it.virtualDevice) {
						doubleValue = DeviceValue.parseDoubleValue(it.value)

						if (doubleValue != null) {
							value = new DeviceValue(device: device, name: it.name, value: doubleValue,
							dateValue: dateDeviceValue)

							if (!value.save()) {
								throw new SmartHomeException("Erreur trace meta valeur !", value)
							}
						}
					}
				}
			}
		}

		return defaultValue
	}


	/**
	 * Création d'un chart
	 * 
	 * @param command
	 * @return
	 * @throws SmartHomeException
	 */
	GoogleChart createChart(DeviceChartCommand command) throws SmartHomeException {
		command.navigation()
		command.deviceImpl = command.device.newDeviceImpl()
		def datas = []

		// on va threader le chargement des values. utile si plusieurs devices
		def promiseList = new PromiseList()
		promiseList << { this.values(command) }
		command.compareDevices.each {
			def compareCommand = command.clone()
			compareCommand.device = it
			promiseList << { this.values(compareCommand) }
		}

		promiseList.get().eachWithIndex { result, index ->
			if (index == 0) {
				datas = result
			} else {
				command.compareValues << result
			}
		}

		GoogleChart chart = command.deviceImpl.googleChart(command, datas)

		return chart
	}


	/**
	 * Aggrège les données du device par jour sur la période de référence
	 * 
	 * @param device
	 * @param dateReference
	 * 
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void aggregateValueDay(Device device, Date dateReference) throws SmartHomeException {
		AbstractDeviceType deviceImpl = device.deviceImpl

		// Suppression des anciennes valeurs calculées car si des données ont 
		// été supprimées, les valeurs actuelles ne seront pas mises à jour
		DeviceValueDay.where { device == device && dateValue == dateReference }.deleteAll()
		
		// calcul des données par jour
		deviceImpl.aggregateValueDay(dateReference).each { mapValue ->
			for (key in mapValue.keySet()) {
				if (! (key in ['dateValue', 'name'])) {
					addDeviceValueDay(device, mapValue.dateValue, "${mapValue.name?:''}${key}", mapValue[key])
				}
			}
		}
	}


	/**
	 * Aggrège les données du device par mois sur la période de référence
	 *
	 * @param device
	 * @param dateReference
	 *
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void aggregateValueMonth(Device device, Date dateReference) throws SmartHomeException {
		AbstractDeviceType deviceImpl = device.deviceImpl

		// Suppression des anciennes valeurs calculées car si des données ont
		// été supprimées, les valeurs actuelles ne seront pas mises à jour
		DeviceValueMonth.where { device == device && dateValue == dateReference }.deleteAll()
		
		// calcul des données par mois
		deviceImpl.aggregateValueMonth(dateReference).each { mapValue ->
			for (key in mapValue.keySet()) {
				if (! (key in ['dateValue', 'name'])) {
					addDeviceValueMonth(device, mapValue.dateValue, "${mapValue.name?:''}${key}", mapValue[key])
				}
			}
		}
	}


	/**
	 * Somme les valeurs du device par mois sur la période de référence
	 *
	 * @param device
	 * @param dateReference
	 *
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void sumValueMonthFromValueDay(Device device, String name, Date start, Date end) throws SmartHomeException {
		List<DeviceValueDay> values = DeviceValueDay.createCriteria().list {
			between 'dateValue', start, end
			eq 'device', device
			eq 'name', name
		}

		// regroupement des valeurs sur le 1er jour du mois
		values.groupBy {
			DateUtils.firstDayInMonth(it.dateValue)
		}.each { entry ->
			addDeviceValueMonth(device, entry.key, name, entry.value.sum { it.value })
		}
	}


	/**
	 * Calcule le max des valeurs du device par mois sur la période de référence
	 *
	 * @param device
	 * @param dateReference
	 *
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void maxValueMonthFromValueDay(Device device, String name, Date start, Date end) throws SmartHomeException {
		List<DeviceValueDay> values = DeviceValueDay.createCriteria().list {
			between 'dateValue', start, end
			eq 'device', device
			eq 'name', name
		}

		// regroupement des valeurs sur le 1er jour du mois
		values.groupBy {
			DateUtils.firstDayInMonth(it.dateValue)
		}.each { entry ->
			addDeviceValueMonth(device, entry.key, name, entry.value.max { it.value }.value )
		}
	}


	/**
	 * Ajoute une nouvelle valeur en vérifiant si n'existe pas déjà pour la même
	 * clé et même date
	 *
	 * @param device
	 * @param dateValue
	 * @param name
	 * @param value
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DeviceValue addDeviceValue(Device device, Date dateValue, String name, double value) throws SmartHomeException {
		DeviceValue deviceValue

		if (name) {
			deviceValue = DeviceValue.findByDeviceAndDateValueAndName(device, dateValue, name)
		} else {
			deviceValue = DeviceValue.findByDeviceAndDateValueAndNameIsNull(device, dateValue)
		}

		if (!deviceValue) {
			deviceValue = new DeviceValue(device: device, dateValue: dateValue, name: name)
		}

		deviceValue.value = value.round(2)

		return super.save(deviceValue)
	}


	/**
	 * Ajoute une nouvelle valeur aggrégée par jour
	 * 
	 * @param device
	 * @param dateValue
	 * @param name
	 * @param value
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DeviceValueDay addDeviceValueDay(Device device, Date dateValue, String name, double value) throws SmartHomeException {
		DeviceValueDay dayValue = DeviceValueDay.findByDeviceAndDateValueAndName(device, dateValue, name)

		if (!dayValue) {
			dayValue = new DeviceValueDay(device: device, dateValue: dateValue, name: name)
		}

		dayValue.value = value.round(2)

		return super.save(dayValue)
	}


	/**
	 * Ajoute une nouvelle valeur aggrégée par mois
	 * 
	 * @param device
	 * @param dateValue
	 * @param name
	 * @param value
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DeviceValueMonth addDeviceValueMonth(Device device, Date dateValue, String name, double value) throws SmartHomeException {
		DeviceValueMonth monthValue = DeviceValueMonth.findByDeviceAndDateValueAndName(device, dateValue, name)

		if (!monthValue) {
			monthValue = new DeviceValueMonth(device: device, dateValue: dateValue, name: name)
		}

		monthValue.value = value.round(2)

		return super.save(monthValue)
	}

}
