package smarthome.automation

import org.apache.commons.lang.StringUtils;

import java.util.Map;

import smarthome.core.DateUtils;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Les périphériques à contrôler
 *  
 * @author gregory
 *
 */
@Validateable
class Device {
	static belongsTo = [agent: Agent, user: User]
	static hasMany = [values: DeviceValue, metadatas: DeviceMetadata, metavalues: DeviceMetavalue,
		events: DeviceEvent, shares: DeviceShare]
	
	String label
	String groupe
	String mac
	String value
	Date dateValue
	DeviceType deviceType
	boolean show
	String command
	String formula
		
	static transients = ['params', 'actionName']
	
	Map params = [:]
	String actionName
	
	
    static constraints = {
		agent nullable: true
		mac unique: ['agent']
		groupe nullable: true
		value nullable: true
		dateValue nullable: true
		params bindable: true, nullable: true
		actionName bindable: true, nullable: true
		command nullable: true
		formula nullable: true
    }
	
	static mapping = {
		mac index: "Device_MacAgent_Idx"
		agent index: "Device_MacAgent_Idx"
		user index: "Device_User_Idx"
		values cascade: 'all-delete-orphan'
		metadatas cascade: 'all-delete-orphan', batchSize: 25
		metavalues cascade: 'all-delete-orphan', batchSize: 25
		events cascade: 'all-delete-orphan'
		shares cascade: 'all-delete-orphan', batchSize: 25
		formula type: 'text'
		sort 'label'
	}
	
	
	static {
		grails.converters.JSON.registerObjectMarshaller(Device) {
			it.fetchParams()
			[id: it.id, mac: it.mac, label: it.label, groupe: it.groupe, value: it.value, dateValue: it.dateValue, 
				deviceType: it.deviceType, params: it.params, command: it.command,
				actionName: it.actionName]
		}
	}
	
	
	
	def metadata(String name) {
		metadatas?.find {
			it.name == name
		}
	}

	def metavalue(String name) {
		metavalues?.find {
			it.name == name
		}
	}

	def metavalueByLabel(String label) {
		metavalues?.find {
			it.label?.toLowerCase()?.contains(label.toLowerCase())
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
			this.addToMetavalues(meta)
		}
		
		meta.value = values.value
		if (values.type) {
			meta.type = values.type
		}
		if (values.label) {
			meta.label = values.label
		}
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
		meta.type = values.type
		meta.label = values.label
		meta.help = values.help
		meta.values = values.values
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
	 * 
	 * @return
	 */
	def newDeviceImpl() {
		def deviceImpl = deviceType.newDeviceType()
		deviceImpl.device = this
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
}
