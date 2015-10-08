package smarthome.automation

import org.apache.commons.lang.StringUtils;
import java.util.Map;
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
		events: DeviceEvent]
	static transients = ['params']
	
	String label
	String groupe
	String mac
	String value
	Date dateValue
	DeviceType deviceType
	boolean show
	Map params = [:]
	
	
    static constraints = {
		agent nullable: true
		mac unique: ['agent']
		groupe nullable: true
		value nullable: true
		dateValue nullable: true
		params bindable: true, nullable: true
    }
	
	static mapping = {
		mac index: "Device_MacAgent_Idx"
		agent index: "Device_MacAgent_Idx"
		user index: "Device_User_Idx"
		values cascade: 'all-delete-orphan'
		metadatas cascade: 'all-delete-orphan'
		metavalues cascade: 'all-delete-orphan'
		events cascade: 'all-delete-orphan'
		sort 'label'
	}
	
	
	static {
		grails.converters.JSON.registerObjectMarshaller(Device) {
			it.fetchParams()
			[id: it.id, mac: it.mac, label: it.label, groupe: it.groupe, value: it.value, dateValue: it.dateValue, deviceType: it.deviceType, params: it.params]
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
	
	
	/**
	 * Ajout d'une métadata
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	def addMetavalue(String name, String value) {
		def meta = metavalue(name)
		
		if (!meta) {
			meta = new DeviceMetavalue(name: name)
			this.addToMetavalues(meta)
		}
		
		meta.value = value
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

	
	def clearNotPersistEvents() {
		events?.removeAll {
			!it.persist
		}
	}	
}
