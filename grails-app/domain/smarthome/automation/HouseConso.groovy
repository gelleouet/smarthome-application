package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Stats conso maison par année
 *  
 * @author gregory
 *
 */
@Validateable
class HouseConso {
	static belongsTo = [house: House]
	
	Double value
	Date dateConso
	
	
    static constraints = {
		
    }
	
	static mapping = {
		house index: "HouseConso_Idx"
	}
}
