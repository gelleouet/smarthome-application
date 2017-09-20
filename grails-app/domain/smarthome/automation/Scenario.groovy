package smarthome.automation

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import smarthome.core.DateUtils;
import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Les scénarios
 *  
 * @author gregory
 *
 */
@Validateable
class Scenario implements Serializable, EventTriggerPreparable {
	static belongsTo = [user: User]
	
	String label
	String description
	String script
	Date lastExecution
	
	
    static constraints = {
		description nullable: true
		lastExecution nullable: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		user index: "Scenario_User_Idx"
		script type: 'text'
		sort 'label'
	}
	
	
	/**
	 * Temps mort depuis la dernière mise à jour ?
	 *
	 * @param dateField
	 * @return
	 */
	boolean isBlindTime(int dateField) {
		return DateUtils.isBlindTime(lastExecution, dateField)
	}
	
	
	/**
	 * Temps mort depuis la dernière mise à jour ?
	 *
	 * @param dateField
	 * @return
	 */
	boolean isBlindTime(int dateField, int delta) {
		return DateUtils.isBlindTime(lastExecution, dateField, delta)
	}


	@Override
	List domainList(EventTrigger eventTrigger) {
		return Scenario.createCriteria().list {
			eq 'user', eventTrigger.event.user
			order 'label'
		}
	}


	@Override
	List actionList(EventTrigger eventTrigger) {
		return ['execute'];
	}


	@Override
	List parameterList(EventTrigger eventTrigger) {
		return [];
	}
	
	@Override
	String domainValue() {
		return "label"
	}
}
