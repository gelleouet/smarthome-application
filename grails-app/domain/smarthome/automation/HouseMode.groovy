package smarthome.automation

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
		id composite: ['house', 'mode']
		version false
	}
}
