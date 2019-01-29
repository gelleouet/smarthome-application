package smarthome.automation

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import smarthome.core.DateUtils;
import smarthome.core.JsonDataDomain;
import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Les plannings : programmation hebdomadaire
 * 
 * Utilisés en interne par certains objets 
 * Un planning peut contenir plusieurs programmations pour chaque jour de la semaine
 *  
 * @author gregory
 *
 */
@Validateable
class Planning extends JsonDataDomain implements Serializable {
	User user
	String label
	String rule
	String data

	// Propriétés utilisateurs
	Map jsonData = [:]
	
	
	static transients = ['jsonData']

	static belongsTo = [user: User]
		
    static constraints = {
		rule nullable: true
		jsonData nullable: true, bindable: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		rule type: 'text'
		data type: 'text'
	}
	
	
	/**
	 * Bind la valeur
	 * 
	 * @param values
	 * @param key
	 * @param day
	 * @param hour
	 * @return
	 */
	Map bindValue(Map values, String key, int day, int hour) {
		Map result = [:]
		
		if (!jsonData) {
			this.loadJsonData()
		}
		
		def keyBuffer = jsonData[(key)]
		
		if (keyBuffer && keyBuffer.size() == 7) {
			def dayBuffer = keyBuffer[day]
			
			if (dayBuffer.size() == 48) {
				result.value = dayBuffer[hour]
			}
		}
		
		if (result.value && values[(result.value)]) {
			result.color = values[(result.value)].color
		}
		
		return result 
	}
}
