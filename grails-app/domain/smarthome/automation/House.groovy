package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Description des maisons d'un user
 *  
 * @author gregory
 *
 */
@Validateable
class House {
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
	 * Classement DPE (A ... G)
	 * @param conso
	 * @return
	 */
	static String classementDPE(int conso) {
		if (conso <= 50) {
			return "A"
		} else if (conso > 50 && conso <= 90) {
			return "B"
		} else if (conso > 90 && conso <= 150) {
			return "C"
		} else if (conso > 150 && conso <= 230) {
			return "D"
		} else if (conso > 230 && conso <= 330) {
			return "E"
		} else if (conso > 330 && conso <= 450) {
			return "F"
		} else {
			return "G"
		}
	}
}
