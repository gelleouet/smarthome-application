package smarthome.automation

import smarthome.automation.notification.NotificationAccountEnum;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Notifications associées à un event
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceEventNotification {
	static belongsTo = [deviceEvent: DeviceEvent]
	
	String message
	NotificationAccountEnum type
	boolean script
	
	
    static constraints = {
		message nullable: true
    }
	
	static mapping = {
		deviceEvent index: "DeviceEventNotification_DeviceEvent_Idx"
		message type: 'text'
	}
	
}
