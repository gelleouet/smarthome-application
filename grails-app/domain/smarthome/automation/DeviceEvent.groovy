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
class DeviceEvent {
	static belongsTo = [device: Device]
	static transients = ['status', 'persist']
	
	String libelle
	String condition // script conditionnel
	Workflow triggeredWorkflow
	Device triggeredDevice
	String triggeredAction
	
	Integer status
	boolean persist
	
	
    static constraints = {
		libelle nullable: true
		condition nullable: true
		triggeredWorkflow nullable: true
		triggeredDevice nullable: true
		triggeredAction nullable: true
		status bindable: true, nullable: true
		persist bindable: true
    }
	
	static mapping = {
		device index: "DeviceEvent_Device_Idx"
		condition type: 'text'
	}
	
}
