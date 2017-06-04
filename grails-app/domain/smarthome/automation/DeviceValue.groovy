package smarthome.automation

import java.util.Date;

import smarthome.security.User;
import grails.validation.Validateable;
import groovy.time.TimeCategory;

/**
 * Toutes les valeurs historisées d'un device
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceValue {
	static belongsTo = [device: Device]
	
	Double value
	Date dateValue
	
	// permet d'avoir plusieurs types de valeur pour un device
	// si un seul type, ne rien mettre
	String name 
	
	// Infos sur la date pour effectuer des regroupempents en base
	Integer monthOfYear
	Integer hourOfDay
	Integer year
	Date day
	
	
    static constraints = {
		name nullable: true
    }
	
	static mapping = {
		name index: "DeviceValue_DeviceName_Idx", length: 64
		device index: "DeviceValue_DeviceName_Idx"
		dateValue index: "DeviceValue_DeviceName_Idx"
		
		hourOfDay formula: 'extract(hour from date_value)'
		monthOfYear formula: 'extract(month from date_value)'
		year formula: 'extract(year from date_value)'
		day formula: "date_trunc('day', date_value)"
	}
	
	
	/**
	 * Valeur numérique min par jour
	 *
	 * @param device
	 * @param metaName
	 */
	static def doubleValueMinByDay(Device device, String metaName = null) {
		def values = DeviceValue.valuesByDay(device, metaName)
		
		values?.min()
	}
	
	
	/**
	 * Valeur numérique max par jour
	 *
	 * @param device
	 * @param metaName
	 */
	static def doubleValueMaxByDay(Device device, String metaName = null) {
		def values = DeviceValue.valuesByDay(device, metaName)
		
		values?.max()
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
			
		result.min = values?.min()
		result.max = values?.max()
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
		def dateDebut = new Date().clearTime()
		def dateFin
		
		use(TimeCategory) {
			dateFin = dateDebut + 23.hours + 59.minutes + 59.seconds
		}
		
		DeviceValue.createCriteria().list {
			eq "device", device
			
			if (metaName) {
				eq "name", metaName
			} else {
				isNull "name"
			}
			
			between "dateValue", dateDebut, dateFin 
			
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
		return firstValueByDay(device, metaName, new Date().clearTime())
	}
	
	
	/**
	 * La 1ere valeur d'un jour
	 * 
	 * @param device
	 * @param metaName
	 * @param date
	 * @return
	 */
	static def firstValueByDay(Device device, String metaName, Date date) {
		def values = DeviceValue.createCriteria().list(max: 1) {
			eq "device", device
			
			if (metaName) {
				eq "name", metaName
			} else {
				isNull "name"
			}
			
			ge "dateValue", date
			
			order "dateValue", "asc"
		}
		
		return values ? values[0] : null
	}
	
	
	/**
	 * Retrouve la 1er valeur sur une période
	 * 
	 * @param device
	 * @param dateDebut inclusive
	 * @param dateFin exclusive
	 * @return
	 */
	static DeviceValue firstValueInPeriod(Device device, Date dateDebut, Date dateFin, String metaName = null) {
		return DeviceValue.createCriteria().get {
			eq "device", device
			
			if (metaName) {
				eq "name", metaName
			} else {
				isNull "name"
			}
			
			ge "dateValue", dateDebut
			lt "dateValue", dateFin
			
			order "dateValue", "asc"
			maxResults 1
		}
	}
	
	
	/**
	 * Retrouve la dernière valeur sur une période
	 * 
	 * @param device
	 * @param dateDebut inclusive
	 * @param dateFin exclusive
	 * @return
	 */
	static DeviceValue lastValueInPeriod(Device device, Date dateDebut, Date dateFin, String metaName = null) {
		return DeviceValue.createCriteria().get {
			eq "device", device
			
			if (metaName) {
				eq "name", metaName
			} else {
				isNull "name"
			}
			
			ge "dateValue", dateDebut
			lt "dateValue", dateFin
			
			order "dateValue", "desc"
			maxResults 1
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
		return lastValueByDay(device, metaName, new Date().clearTime())
	}
	
	
	/**
	 * La dernière valeur d'un jour
	 *
	 * @param device
	 * @param metaName
	 * @param last
	 */
	static def lastValueByDay(Device device, String metaName, Date date) {
		def values = DeviceValue.createCriteria().list(max: 1) {
			eq "device", device
			
			if (metaName) {
				eq "name", metaName
			} else {
				isNull "name"
			}
			
			ge "dateValue", date
			
			order "dateValue", "desc"
		}
		
		return values ? values[0] : null
	}
	
	
	/**
	 * Renvoit les dernières valeurs d'un device
	 * 
	 * @param device
	 * @param metaName
	 * @param date
	 * @param max
	 * @return
	 */
	static def lastValuesByDay(Device device, String metaName, Date date, int nbData) {
		return DeviceValue.createCriteria().list(max: nbData) {
			eq "device", device
			
			if (metaName) {
				eq "name", metaName
			} else {
				isNull "name"
			}
			
			ge "dateValue", date
			
			order "dateValue", "desc"
		}
	}
	
	
	static def values(Device device, Date dateDebut, Date dateFin, String name = null) {
		return DeviceValue.createCriteria().list {
			eq 'device', device
			between 'dateValue', dateDebut, dateFin
			
			if (name) {
				if (name == '') {
					isNull 'name'
				} else {
					eq 'name', name
				}
			}
			
			order 'dateValue', 'name'
		}
	}
	
	
	/**
	 * convertit une valeur texte en numérique
	 * 
	 * @param value
	 * @return
	 */
	static parseDoubleValue(String value) {
		def doubleValue = null
		
		if (value != null) {
			if (value == 'true') {
				doubleValue = 1
			} else if (value == 'false') {
				doubleValue = 0
			} else {
				try {
					doubleValue = value.toDouble()
				} catch (Exception ex) {}
			}
		}
		
		return doubleValue
	}
	
}
