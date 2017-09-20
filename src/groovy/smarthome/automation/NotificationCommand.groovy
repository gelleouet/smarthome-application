package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

@Validateable
class NotificationCommand {
	String description
	User user
	
	
	static constraints = {
		
	}
}
