package smarthome.automation

import smarthome.core.SmartHomeCoreConstantes;
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
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		id composite: ['deviceEvent', 'mode']
		version false
	}
}
