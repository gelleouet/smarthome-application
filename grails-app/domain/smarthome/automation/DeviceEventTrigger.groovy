package smarthome.automation

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
class DeviceEventTrigger {
	static belongsTo = [deviceEvent: DeviceEvent]
	
	Workflow workflow
	
	Device device
	String actionName
	String preScript
	
	static transients = ['persist', 'status']
	
	boolean persist
	Integer status
	
	
    static constraints = {
		workflow nullable: true
		preScript nullable: true
		device nullable: true
		actionName nullable: true
		persist bindable: true
		status bindable: true
    }
	
	static mapping = {
		deviceEvent index: "DeviceEventTrigger_DeviceEvent_Idx"
	}
	
}
