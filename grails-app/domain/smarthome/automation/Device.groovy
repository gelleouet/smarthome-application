package smarthome.automation

import org.apache.commons.lang.StringUtils;

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
	static hasMany = [values: DeviceValue, metadatas: DeviceMetadata]
	static transients = ['params']
	
	String label
	String groupe
	String mac
	String value
	Date dateValue
	DeviceType deviceType
	
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
		sort 'label'
	}
	
	
	static {
		grails.converters.JSON.registerObjectMarshaller(Device) {
			it.fetchParams()
			[id: it.id, mac: it.mac, label: it.label, groupe: it.groupe, value: it.value, dateValue: it.dateValue, deviceType: it.deviceType, params: it.params]
		}
	}
	
	
	String viewGrid() {
		deviceType.viewGrid(this)
	}
	
	
	String viewForm() {
		deviceType.viewForm(this)
	}
	
	
	String icon() {
		deviceType.icon(this)
	}

	String defaultChartType() {
		deviceType.defaultChartType(this)
	}
	
	
	def metadata(String name) {
		metadatas?.find {
			it.name == name
		}
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
}
