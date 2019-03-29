package smarthome.core

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Association widget-user
 * 
 *  
 * @author gregory
 *
 */
@Validateable
class WidgetUser extends JsonDataDomain {
	User user
	Widget widget
	int row
	int col
	String data
	/**
	 * Rajoute un id dans la requête du widget pour la personnaliser
	 */
	Long paramId
	
	// Propriétés utilisateur
	Map jsonData
	
	
	static transients = ['jsonData']
	
	
	static belongsTo = [user: User, widget: Widget]
	
	
    static constraints = {
		data nullable: true	
		paramId nullable: true	
    }
	
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		user index: 'WidgetUser_Idx'
	}
}
