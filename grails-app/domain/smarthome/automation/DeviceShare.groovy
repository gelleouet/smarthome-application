package smarthome.automation

import java.io.Serializable;

import smarthome.automation.notification.NotificationAccountEnum;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Partage de devices entre plusieurs utilisateurs avec notion de modification / execution / consultation
 *  
 * @author gregory
 *
 */
@Validateable
class DeviceShare implements Serializable {
	static belongsTo = [device: Device]
	
	User sharedUser // si null, le device sera partagé à tous les amis
	
	
    static constraints = {
		sharedUser nullable: true
    }
	
	static mapping = {
		device index: "DeviceShare_Device_Idx"
		sharedUser index: "DeviceShare_SharedUser_Idx"
	}
	
}
