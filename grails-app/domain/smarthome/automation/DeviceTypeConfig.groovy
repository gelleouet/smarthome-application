package smarthome.automation

import java.io.Serializable;

import smarthome.core.JsonDataDomain;
import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Un type device
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceTypeConfig extends JsonDataDomain implements Serializable {
	DeviceType deviceType
	String data
	
	/**
	 * Transient properties
	 */
	Map jsonData = [:]
	
	
	static transients = ['jsonData']
	
	
	static belongsTo = [deviceType: DeviceType]
	
	
    static constraints = {
		
    }
	
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		id generator: 'foreign', column: 'device_type_id', params: [property: 'deviceType']
		deviceType insertable: false, updateable: false // car la propriété est déjà mappée dans id
		data type: 'text'
	}
	
	
}
