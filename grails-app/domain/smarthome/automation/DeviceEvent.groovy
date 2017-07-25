package smarthome.automation

import java.io.Serializable;

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
class DeviceEvent implements Serializable {
	public static final String FORMAT_HEURE_DECALAGE = "HH:mm"
	
	
	static belongsTo = [device: Device, user: User]
	static hasMany = [triggers: DeviceEventTrigger, notifications: DeviceEventNotification,
		modes: DeviceEventMode]
	
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
	List<Mode> modeList = []
	
	
	static transients = ['notificationSms', 'notificationMail', 'modeList']
	
	
    static constraints = {
		condition nullable: true
		lastEvent nullable: true
		cron nullable: true
		heureDecalage nullable: true
		lastHeureDecalage nullable: true
		solstice nullable: true
		notificationSms bindable: true
		notificationMail bindable: true
		modeList bindable: true
    }
	
	static mapping = {
		device index: "DeviceEvent_Device_Idx"
		condition type: 'text'
		triggers cascade: 'all-delete-orphan'
		notifications cascade: 'all-delete-orphan'
		modes cascade: 'all-delete-orphan'
		heureDecalage length: 8
		lastHeureDecalage length: 8
		solstice length: 8
		cron length: 128
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
	
	
	/**
	 * Temps mort depuis la dernière mise à jour ?
	 *
	 * @param dateField
	 * @return
	 */
	boolean isBlindTime(int dateField, int delta) {
		return DateUtils.isBlindTime(lastEvent, dateField, delta)
	}
}
