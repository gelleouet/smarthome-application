package smarthome.automation

import java.io.Serializable;

import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Evénements associés à un device
 * Permet de déclencher un autre device ou worfklow en fonction d'une condition
 *  
 * @author gregory
 *
 */
@Validateable
class EventTrigger implements Serializable, Comparable<EventTrigger> {
	static final String HERITED_ACTION_NAME = "[herited]"
	
	
	String domainClassName
	Long domainId
	String actionName
	String parameters
	
	static belongsTo = [event: Event]
	Event event
	
	static transients = ['status', 'jsonParameters', 'domainList', 'actionList', 'parameterList',
		'domainValue']
	Integer status
	Map jsonParameters = [:]
	List domainList = []
	List actionList = []
	List parameterList = []
	String domainValue
	
	
    static constraints = {
		parameters nullable: true
		status bindable: true
		jsonParameters bindable: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		event index: "EventTrigger_Event_Idx"
		parameters type: 'text'
		version false
	}

	@Override
	int compareTo(EventTrigger o) {
		OrderBy orderBy = new OrderBy([
			{ it.domainClassName},
			{ it.status }
		])
		
		return orderBy.compare(this, o)
	}
}
