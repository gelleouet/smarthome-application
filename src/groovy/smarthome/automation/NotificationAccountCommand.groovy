package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

@Validateable
class NotificationAccountCommand {
	String libelle
	User user
	
	
	static constraints = {
		
	}
}
