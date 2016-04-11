package smarthome.core

import java.util.Date;

import grails.validation.Validateable;


/**
 * Une règle métier enregistrée en base dans un script Groovy
 * 
 * @author gregory
 *
 */
@Validateable
class ScriptRule {
	String ruleName
	String description
	String script
	
	// Automatic Timestamping
	// http://grails.org/doc/latest/guide/GORM.html#eventsAutoTimestamping
	Date dateCreated
	Date lastUpdated
	
    static constraints = {
		ruleName unique: true
		dateCreated nullable: true
		lastUpdated nullable: true
    }
	
	
	static mapping = {
		sort 'ruleName'
		script type: 'text'
	}
}
