package smarthome.automation

import smarthome.core.SmartHomeCoreConstantes;
import grails.validation.Validateable;

/**
 * Modes activés sur une maison
 *  
 * @author gregory
 *
 */
@Validateable
class HouseMode implements Serializable  {
	House house
	Mode mode
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		id composite: ['house', 'mode']
		version false
	}
}
