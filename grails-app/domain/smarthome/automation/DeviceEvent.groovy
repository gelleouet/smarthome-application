package smarthome.automation

import smarthome.core.DateUtils;
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
	public static final String FORMAT_HEURE_DECALAGE = "HH:mm"
	
	
	static belongsTo = [device: Device, user: User]
	static hasMany = [triggers: DeviceEventTrigger, notifications: DeviceEventNotification]
	
	Set triggers = []
	Set notifications = []
	
	String libelle
	String condition // script groovy conditionnel
	boolean actif
	Date lastEvent
	
	String cron
	// planif solticialle
	String solstice
	boolean synchroSoleil
	boolean heureEte
	String heureDecalage // heure du décalage max au format HH:mm
	String lastHeureDecalage // heure du dernier décalage au format HH:mm
	
	
	// propriétés utilisateur
	boolean notificationSms
	boolean notificationMail
	
	
	static transients = ['notificationSms', 'notificationMail']
	
	
    static constraints = {
		condition nullable: true
		lastEvent nullable: true
		cron nullable: true
		heureDecalage nullable: true
		lastHeureDecalage nullable: true
		solstice nullable: true
		notificationSms bindable: true
		notificationMail bindable: true
    }
	
	static mapping = {
		device index: "DeviceEvent_Device_Idx"
		condition type: 'text'
		triggers cascade: 'all-delete-orphan'
		notifications cascade: 'all-delete-orphan'
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
	
	
	/**
	 * Temps mort depuis la dernière mise à jour ?
	 *
	 * @param dateField
	 * @return
	 */
	boolean isBlindTime(int dateField) {
		return DateUtils.isBlindTime(lastEvent, dateField)
	}
}
