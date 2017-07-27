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
class DeviceEventTrigger implements Serializable {
	static belongsTo = [deviceEvent: DeviceEvent]
	
	Scenario scenario
	
	Device device
	String actionName
	String preScript
	
	static transients = ['persist', 'status']
	
	boolean persist
	Integer status
	
	
    static constraints = {
		scenario nullable: true
		preScript nullable: true
		device nullable: true
		actionName nullable: true
		persist bindable: true
		status bindable: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		deviceEvent index: "DeviceEventTrigger_DeviceEvent_Idx"
	}
	
}
