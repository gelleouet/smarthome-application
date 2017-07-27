package smarthome.automation

import java.io.Serializable;

import smarthome.automation.notification.NotificationAccountEnum;
import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Notifications associées à un event
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceEventNotification implements Serializable {
	static belongsTo = [deviceEvent: DeviceEvent]
	
	String message
	NotificationAccountEnum type
	boolean script
	
	
    static constraints = {
		message nullable: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		deviceEvent index: "DeviceEventNotification_DeviceEvent_Idx"
		message type: 'text'
	}
	
}
