package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Toutes les valeurs historisées d'un device
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceValue {
	static belongsTo = [device: Device]
	
	String value
	Date dateValue
	
	// permet d'avoir plusieurs types de valeur pour un device
	// si un seul type, ne rien mettre
	String name 
	
	
    static constraints = {
		name nullable: true
    }
	
	static mapping = {
		name index: "DeviceValue_DeviceName_Idx"
		device index: "DeviceValue_DeviceName_Idx"
		dateValue index: "DeviceValue_DeviceName_Idx"
	}
	
	
	/**
	 * Valeur numérique min par jour
	 *
	 * @param device
	 * @param metaName
	 */
	static def doubleValueMinByDay(Device device, String metaName = null) {
		def values = DeviceValue.valuesByDay(device, metaName)
		
		values?.min {
			it?.toDouble()
		}
	}
	
	
	/**
	 * Valeur numérique max par jour
	 *
	 * @param device
	 * @param metaName
	 */
	static def doubleValueMaxByDay(Device device, String metaName = null) {
		def values = DeviceValue.valuesByDay(device, metaName)
		
		values?.max {
			it?.toDouble()
		}
	}

	
	/**
	 * Valeur numérique min, max par jour
	 *
	 * @param device
	 * @param metaName
	 */
	static def doubleValueAggregategByDay(Device device, String metaName = null) {
		def values = DeviceValue.valuesByDay(device, metaName)
		def result = [:]
			
		result.min = values?.min {
			it?.toDouble()
		}
		result.max = values?.max {
			it?.toDouble()
		}
		result.count = values?.size()
		
		return result
	}
	
	
	/**
	 * Renvoit les valeurs du jour d'un device
	 * 
	 * @param device
	 * @param metaName
	 */
	static def valuesByDay(Device device, String metaName = null) {
		DeviceValue.createCriteria().list {
			eq "device", device
			
			if (metaName) {
				eq "name", metaName
			} else {
				isNull "name"
			}
			
			ge "dateValue", new Date().clearTime()
			
			projections {
				property "value"
			}
		}
	}
	
	
	/**
	 * La 1ere valeur du jour
	 * 
	 * @param device
	 * @param metaName
	 * @param last
	 */
	static def firstValueByDay(Device device, String metaName = null) {
		DeviceValue.createCriteria().list(max: 1) {
			eq "device", device
			
			if (metaName) {
				eq "name", metaName
			} else {
				isNull "name"
			}
			
			ge "dateValue", new Date().clearTime()
			
			order "dateValue", "asc"
		}
	}

	
	/**
	 * La dernière valeur du jour
	 * 
	 * @param device
	 * @param metaName
	 * @param last
	 */
	static def lastValueByDay(Device device, String metaName = null) {
		DeviceValue.createCriteria().list(max: 1) {
			eq "device", device
			
			if (metaName) {
				eq "name", metaName
			} else {
				isNull "name"
			}
			
			ge "dateValue", new Date().clearTime()
			
			order "dateValue", "desc"
		}
	}
	
}
