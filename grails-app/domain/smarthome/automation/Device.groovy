package smarthome.automation

import org.apache.commons.lang.StringUtils

import java.io.Serializable
import java.util.List
import java.util.Map

import smarthome.automation.deviceType.AbstractDeviceType
import smarthome.core.DateUtils
import smarthome.core.ScriptUtils
import smarthome.core.SmartHomeCoreConstantes
import smarthome.security.User
import grails.converters.JSON
import grails.validation.Validateable

/**
 * Les périphériques à contrôler
 *  
 * @author gregory
 *
 */
@Validateable
class Device implements Serializable, EventTriggerPreparable {
	static belongsTo = [agent: Agent, user: User]
	static hasMany = [values: DeviceValue, metadatas: DeviceMetadata, metavalues: DeviceMetavalue,
		events: EventDevice, shares: DeviceShare, levelAlerts: DeviceLevelAlert,
		devicePlannings: DevicePlanning]

	Agent agent
	User user
	String label
	String groupe
	String mac
	String value
	Date dateValue
	DeviceType deviceType
	boolean favori
	String command	// commande à envoyer à l'agent
	String formula	// formule à appliquer sur chaque valeur reçue
	String tableauBord
	String extras	// option extra json
	String unite

	static transients = ['params', 'actionName', 'extrasJson', 'deviceImpl']

	AbstractDeviceType deviceImpl
	Map extrasJson = [:]
	Map params = [:]
	String actionName	// action en cours d'exécution sur deviceImpl


	static constraints = {
		agent nullable: true
		groupe nullable: true
		value nullable: true
		dateValue nullable: true
		params bindable: true, nullable: true
		actionName bindable: true, nullable: true
		command nullable: true
		formula nullable: true
		tableauBord nullable: true
		extras nullable: true
		unite nullable: true
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		mac index: "Device_MacAgent_Idx"
		agent index: "Device_MacAgent_Idx"
		user index: "Device_User_Idx"
		values cascade: 'all-delete-orphan'
		levelAlerts cascade: 'all-delete-orphan'
		metadatas cascade: 'all-delete-orphan', batchSize: 25
		metavalues cascade: 'all-delete-orphan', batchSize: 25
		events cascade: 'all-delete-orphan'
		devicePlannings cascade: 'all-delete-orphan'
		shares cascade: 'all-delete-orphan', batchSize: 25
		formula type: 'text'
		extras type: 'text'
		unite length: 16
		sort 'label'
	}


	static {
		grails.converters.JSON.registerObjectMarshaller(Device) {
			it.fetchParams()
			def map = [id: it.id, mac: it.mac, label: it.label, groupe: it.groupe, value: it.value, dateValue: it.dateValue,
				deviceType: [id: it.deviceType?.id], params: it.params, command: it.command,
				actionName: it.actionName, jsonDateValue: it.dateValue?.format(DateUtils.JSON_FORMAT),
				metavalues: []]

			it.metavalues.each { meta ->
				map.metavalues << [id: meta.id, value: meta.value, main: meta.main, trace: meta.trace,
					virtualDevice: meta.virtualDevice, name: meta.name]
			}

			return map
		}
	}



	DeviceMetadata metadata(String name) {
		metadatas?.find {
			it.name == name
		}
	}

	DeviceMetavalue metavalue(String name) {
		metavalues?.find {
			it.name == name
		}
	}

	DeviceMetavalue metavalueByLabel(String label) {
		metavalues?.find {
			it.label?.toLowerCase()?.contains(label.toLowerCase())
		}
	}

	DeviceMetavalue metavalueByLabelStrict(String label) {
		metavalues?.find {
			it.label?.toLowerCase() == label.toLowerCase()
		}
	}

	DeviceMetavalue metavalueByLabelStrictNotNull(String label) {
		metavalues?.find {
			it.label?.toLowerCase() == label.toLowerCase() && DeviceValue.parseDoubleValue(it.value)
		}
	}


	/**
	 * Ajout d'une métavalue
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	def addMetavalue(String name, Map values) {
		def meta = metavalue(name)

		if (!meta) {
			meta = new DeviceMetavalue(name: name)

			if (values.trace) {
				meta.trace = values.trace
			}

			if (values.main) {
				meta.main = values.main
			}
			if (values.virtualDevice) {
				meta.virtualDevice = values.virtualDevice
			}
			if (values.unite) {
				meta.unite = values.unite
			}

			this.addToMetavalues(meta)
		}

		meta.value = values.value

		if (values.type) {
			meta.type = values.type
		}
		if (values.label) {
			meta.label = values.label
		}

		return meta
	}


	/**
	 * Ajout d'une métadata
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	def addMetadata(String name, Map values) {
		def meta = metadata(name)

		if (!meta) {
			meta = new DeviceMetadata(name: name)
			this.addToMetadatas(meta)
		}

		meta.value = values.value

		if (values.type) {
			meta.type = values.type
		}

		if (values.label) {
			meta.label = values.label
		}

		if (values.values instanceof Iterable) {
			meta.values = values.values.join("##")
		} else if (values.values) {
			meta.values = values.values.toString()
		}

		return meta
	}


	/**
	 * charge les métadatas dans les params
	 * 
	 * @return
	 */
	def fetchParams() {
		params = [:]
		metadatas?.each {
			params[(it.name)] = it.value
		}
	}


	/**
	 * Construit une instance du device type
	 * !! Ne le fait qu'une fois
	 * 
	 * @return
	 */
	AbstractDeviceType newDeviceImpl() {
		if (!deviceImpl) {
			deviceImpl = deviceType.newDeviceType()
			deviceImpl.device = this
		}
		return deviceImpl
	}


	/**
	 * Temps mort depuis la dernière mise à jour ?
	 * 
	 * @param dateField
	 * @return
	 */
	boolean isBlindTime(int dateField) {
		return DateUtils.isBlindTime(dateValue, dateField)
	}


	/**
	 * Temps mort depuis la dernière mise à jour ?
	 * 
	 * @param dateField
	 * @return
	 */
	boolean isBlindTime(int dateField, int delta) {
		return DateUtils.isBlindTime(dateValue, dateField, delta)
	}


	/**
	 * Les dernières valeurs du jour
	 * 
	 * @param metaName
	 * @param nbData
	 * @return
	 */
	List lastValues(String metaName, int nbData) {
		return DeviceValue.lastValuesByDay(this, metaName, new Date().clearTime(), nbData)
	}


	/**
	 * Recherche d'une meta value main et synchro de sa valeur
	 * avec la valeur du device
	 * Lance la préparation des metavalues depuis l'implémentation du device
	 * Applique la formule si présente
	 * 
	 * @param datas original datas
	 * 
	 * @return this
	 */
	Device processValue(def datas) {
		def deviceImpl = newDeviceImpl()
		deviceImpl.prepareMetaValuesForSave(datas)

		def metaValue = metavalues?.find {
			it.main
		}

		if (metaValue) {
			this.value = metaValue.value
		}

		// insère nouvelle valeur
		// transforme les datas si formule présente sur le device
		if (formula) {
			ScriptUtils.runScript(formula, [device: this])
		}

		return this
	}


	/**
	 * Supprime les levels alerts non bindés depuis formulaire enregisrement
	 * 
	 * @return
	 */
	Device clearNotBindingLevelAlert() {
		levelAlerts?.removeAll {
			it.status == null
		}

		return this
	}


	/**
	 * Supprime plannings non bindés
	 *
	 * @return
	 */
	Device clearNotBindingPlanning() {
		devicePlannings?.removeAll {
			it.status == null
		}

		return this
	}


	/**
	 * Retourne la dernière alerte du device
	 * 
	 * @return
	 */
	DeviceAlert lastDeviceAlert() {
		return DeviceAlert.createCriteria().get {
			eq 'device', this
			order 'dateDebut', 'desc'
			maxResults 1
		}
	}


	@Override
	List domainList(EventTrigger eventTrigger) {
		return Device.createCriteria().list {
			eq 'user', eventTrigger.event.user
			order 'label'
		}
	}

	@Override
	List actionList(EventTrigger eventTrigger) {
		List actions = [EventTrigger.HERITED_ACTION_NAME]
		Device device = Device.read(eventTrigger.domainId)
		actions.addAll(device.newDeviceImpl().events())
		return actions
	}

	@Override
	List parameterList(EventTrigger eventTrigger) {
		Device device = Device.read(eventTrigger.domainId)
		return device.newDeviceImpl().eventParameters(eventTrigger.actionName)
	}

	@Override
	String domainValue() {
		return "label"
	}


	/**
	 * Convertit les options extras au format json dans la propriété associée	
	 */
	void extrasToJson() {
		extrasJson = extras ? JSON.parse(extras) : [:]
	}
}
