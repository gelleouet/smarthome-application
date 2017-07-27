package smarthome.automation

import org.apache.commons.lang.StringUtils;

import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Les graphiques personnalisés
 *  
 * @author gregory
 *
 */
@Validateable
class Chart {
	static belongsTo = [user: User]
	static hasMany = [devices: ChartDevice]
	
	String label
	String chartType
	String groupe
	
	
    static constraints = {
		
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		user index: "Chart_User_Idx"
		devices cascade: 'all-delete-orphan'
		sort 'label'
	}
	
}
