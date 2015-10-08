package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Les scénarios
 *  
 * @author gregory
 *
 */
@Validateable
class Workflow {
	static belongsTo = [user: User]
	
	String label
	String description
	String script
	
	
    static constraints = {
		description nullable: true
    }
	
	static mapping = {
		user index: "Workflow_User_Idx"
		script type: 'text'
		sort 'label'
	}
	
}
