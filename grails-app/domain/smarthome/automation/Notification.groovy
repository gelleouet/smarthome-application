package smarthome.automation

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.converters.JSON;
import grails.validation.Validateable;

/**
 * Notification personnalisée pouvant être utilisée directement sur les événements
 *  
 * @author gregory
 *
 */
@Validateable
class Notification implements Serializable, EventTriggerPreparable {
	static belongsTo = [user: User]
	User user
	
	String description
	NotificationAccount notificationAccount
	String message
	String parameters
	
	static transients = ['jsonParameters', 'convertMessage']
	Map jsonParameters = [:]
	String convertMessage
	
	
    static constraints = {
		parameters nullable: true	
		jsonParameters bindable: true
    }

		
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		user index: "Notification_User_Idx"
		message type: 'text'
		parameters type: 'text'
	}

	void parametersToJson() {
		jsonParameters = parameters ? JSON.parse(parameters) : [:]
	}
	
	void parametersFromJson() {
		parameters = jsonParameters as JSON
	}
	
	
	@Override
	List domainList(EventTrigger eventTrigger) {
		return Notification.createCriteria().list {
			eq 'user', eventTrigger.event.user
			order 'description'
		}
	}


	@Override
	List actionList(EventTrigger eventTrigger) {
		return ['execute']
	}


	@Override
	List parameterList(EventTrigger eventTrigger) {
		return []
	}

	@Override
	String domainValue() {
		return "description"
	}
}
