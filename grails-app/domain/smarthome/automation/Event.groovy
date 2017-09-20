package smarthome.automation

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import grails.converters.JSON;
import smarthome.core.DateUtils;
import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Evénements
 *  
 * @author gregory
 *
 */
@Validateable
class Event implements Serializable, EventTriggerPreparable {
	public static final String FORMAT_HEURE_DECALAGE = "HH:mm"
	
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
	
	
	static belongsTo = [user: User]
	User user
	
	static hasMany = [triggers: EventTrigger, modes: EventMode, devices: EventDevice]
	Set triggers = []
	Set modes = []
	Set devices = []
	
	
	static transients = ['modeList', 'deviceList']
	List<Mode> modeList = []
	List<Device> deviceList = []
	
	
    static constraints = {
		condition nullable: true
		lastEvent nullable: true
		cron nullable: true
		heureDecalage nullable: true
		lastHeureDecalage nullable: true
		solstice nullable: true
		modeList bindable: true
		deviceList bindable: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		user index: "Event_User_Idx"
		condition type: 'text'
		triggers cascade: 'all-delete-orphan'
		modes cascade: 'all-delete-orphan', batchSize: 25
		devices cascade: 'all-delete-orphan'
		heureDecalage length: 8
		lastHeureDecalage length: 8
		solstice length: 8
		cron length: 128
	}
	
	
	static {
		grails.converters.JSON.registerObjectMarshaller(Event) {
			[id: it.id, libelle: it.libelle, actif: it.actif, cron: it.cron]
		}
	}
	
	
	def clearNotPersistTriggers() {
		triggers?.removeAll {
			it.status == null
		}
	}
	
	
	void triggerParameterToJson() {
		triggers?.each { trigger ->
			trigger.jsonParameters = trigger.parameters ? JSON.parse(trigger.parameters) : [:]
		}
	}
	
	
	void triggerParameterFromJson() {
		triggers?.each { trigger ->
			trigger.parameters = trigger.jsonParameters as JSON
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


	@Override
	List domainList(EventTrigger event) {
		return null;
	}


	@Override
	List actionList(EventTrigger event) {
		return null;
	}


	@Override
	List parameterList(EventTrigger event) {
		return null;
	}

	@Override
	String domainValue() {
		return "libelle"
	}
}
