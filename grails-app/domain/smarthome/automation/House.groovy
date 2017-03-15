package smarthome.automation

import java.io.Serializable;

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Description des maisons d'un user
 *  
 * @author gregory
 *
 */
@Validateable
class House implements Serializable {
	static belongsTo = [user: User]
	
	static hasMany = [modes: HouseMode, consos: HouseConso]
	
	String name
	boolean defaut
	Double surface
	Device compteur
	Device temperature
	Device humidite
	
	
    static constraints = {
		surface nullable: true
		compteur nullable: true
		temperature nullable: true
		humidite nullable: true
    }
	
	static mapping = {
		user index: "House_User_Idx"
		modes cascade: 'all-delete-orphan'
		consos cascade: 'all-delete-orphan'
	}
	
	
	/**
	 * Recherche de la dernière conso
	 * 
	 * @return
	 */
	HouseConso lastConso() {
		def house = this
		
		return HouseConso.createCriteria().get {
			eq "house.id", house.id
			order "dateConso", "desc"
			maxResults 1
		}
	}
}
