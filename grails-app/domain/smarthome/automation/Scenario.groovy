package smarthome.automation

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
class Scenario {
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
		user index: "Workflow_User_Idx"
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
}
