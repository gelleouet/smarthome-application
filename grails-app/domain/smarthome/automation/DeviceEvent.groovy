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
	static belongsTo = [device: Device, user: User]
	static hasMany = [triggers: DeviceEventTrigger]
	
	Set triggers = []
	
	String libelle
	String condition // script groovy conditionnel
	boolean actif
	Date lastEvent
	
	String cron
	String solstice
	boolean synchroSoleil
	Integer decalageMinute
	Integer lastDecalage
	
	
    static constraints = {
		condition nullable: true
		lastEvent nullable: true
		cron nullable: true
		decalageMinute nullable: true
		lastDecalage nullable: true
		solstice nullable: true
    }
	
	static mapping = {
		device index: "DeviceEvent_Device_Idx"
		condition type: 'text'
		triggers cascade: 'all-delete-orphan'
	}
	
	
	static {
		grails.converters.JSON.registerObjectMarshaller(DeviceEvent) {
			[id: it.id, libelle: it.libelle, actif: it.actif, cron: it.cron]
		}
	}
	
	
	def clearNotPersistTriggers() {
		triggers?.removeAll {
			! it.persist
		}
	}
}
