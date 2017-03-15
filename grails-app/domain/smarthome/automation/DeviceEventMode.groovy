package smarthome.automation

import grails.validation.Validateable;

/**
 * Modes activés sur un event
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceEventMode implements Serializable  {
	DeviceEvent deviceEvent
	Mode mode
	
	static mapping = {
		id composite: ['deviceEvent', 'mode']
		version false
	}
}
