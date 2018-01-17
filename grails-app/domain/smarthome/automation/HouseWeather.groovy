package smarthome.automation

import java.io.Serializable;
import smarthome.core.JsonDataDomain;
import smarthome.core.SmartHomeCoreConstantes;
import grails.validation.Validateable;

/**
 * Infos météos d'une maison
 *  
 * @author gregory
 *
 */
@Validateable
class HouseWeather implements Serializable {
	House house
	Date dateWeather
	String data
	String providerClass
	
	
    static constraints = {
		
    }
	
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		id generator: 'foreign', column: 'house_id', params: [property: 'house']
		house insertable: false, updateable: false // car la propriété est déjà mappée dans id
		data type: 'text'
		version false
	}
}
