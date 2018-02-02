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
class EventMode implements Serializable  {
	static belongsTo = [event: Event]
	
	Event event
	Mode mode
	
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		event index: "EventMode_Event_Idx"
		version false
	}
}
