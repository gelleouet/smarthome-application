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
	HouseConso currentConso() {
		Date now = new Date()
		return consoByYear(now[Calendar.YEAR])
	}
	
	
	/**
	 * La conso d'une année (enregistrée au 1er janvier)
	 * 
	 * @param year
	 * @return
	 */
	HouseConso consoByYear(int year) {
		return HouseConso.createCriteria().get {
			eq "house.id", this.id
			eq "dateConso", HouseConso.dateConsoForYear(year)
		}
	}
	
	
	/**
	 * Création d'une nouvelle conso pour une année (enregistrée au 1er janvier)
	 * 
	 * @param year
	 * @return
	 */
	HouseConso newConsoForYear(int year) {
		HouseConso conso = new HouseConso(house: this,
			dateConso: HouseConso.dateConsoForYear(year))
		return conso	
	}
}
