package smarthome.automation

import java.io.Serializable;
import java.util.Date;

import smarthome.core.DateUtils;
import smarthome.core.SmartHomeCoreConstantes;
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
class DeviceValue implements Serializable {
	
	static final String DEFAULT_NAME = "_main"
	static final String DEFAULT_LABEL = "Principale"
	
	
	Device device
	Double value
	Date dateValue
	LevelAlertEnum alertLevel
	// permet d'avoir plusieurs types de valeur pour un device
	// si un seul type, ne rien mettre
	String name 
	
	
	static belongsTo = [device: Device]
	
	
    static constraints = {
		name nullable: true
		alertLevel nullable: true
    }
	
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		name index: "DeviceValue_DeviceName_Idx", length: 64
		device index: "DeviceValue_DeviceName_Idx"
		dateValue index: "DeviceValue_DeviceName_Idx"
		alertLevel length: 16
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
	 * La 1ere valeur du mois
	 *
	 * @param device
	 * @param metaName
	 * @param last
	 */
	static def firstValueByMonth(Device device, String metaName = null) {
		Date now = new Date().clearTime()
		return firstValueInPeriod(device, DateUtils.firstDayInMonth(now),
			DateUtils.lastDayInMonth(now)+1, metaName)
	}
	
	
	/**
	 * La dernière valeur du mois
	 *
	 * @param device
	 * @param metaName
	 * @param last
	 */
	static def lastValueByMonth(Device device, String metaName = null) {
		Date now = new Date().clearTime()
		return lastValueInPeriod(device, DateUtils.firstDayInMonth(now),
			DateUtils.lastDayInMonth(now)+1, metaName)
	}
	
	
	/**
	 * La 1ere valeur de l'année
	 *
	 * @param device
	 * @param metaName
	 * @param last
	 */
	static def firstValueByYear(Device device, String metaName = null) {
		Date now = new Date().clearTime()
		return firstValueInPeriod(device, DateUtils.firstDayInYear(now),
			DateUtils.lastDayInYear(now)+1, metaName)
	}
	
	
	/**
	 * La dernière valeur de l'année
	 *
	 * @param device
	 * @param metaName
	 * @param last
	 */
	static def lastValueByYear(Device device, String metaName = null) {
		Date now = new Date().clearTime()
		return lastValueInPeriod(device, DateUtils.firstDayInYear(now),
			DateUtils.lastDayInYear(now)+1, metaName)
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
	
	
	static def values(Device device, Date dateDebut, Date dateFin, String metaName = null) {
		return DeviceValue.createCriteria().list {
			eq 'device', device
			between 'dateValue', dateDebut, dateFin
			
			if (metaName) {
				// * active toutes les metaNames
				if (metaName != "*") {
					or {
						for (String token : metaName.split(",")) {
							if (token == "null" || !token) {
								isNull "name"	
							} else {
								eq "name", token
							}
						}
					}
				}
			} else {
				// la valeur principale
				isNull "name"
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
			if (value == 'true' || value == 'on') {
				doubleValue = 1
			} else if (value == 'false' || value == 'off') {
				doubleValue = 0
			} else {
				try {
					doubleValue = value.toDouble()
				} catch (Exception ex) {}
			}
		}
		
		return doubleValue
	}
	
	
	static String toStringValue(def value) {
		if (value instanceof Boolean) {
			return value ? "1" : "0"
		} 
		
		return value?.toString()
	}
}
