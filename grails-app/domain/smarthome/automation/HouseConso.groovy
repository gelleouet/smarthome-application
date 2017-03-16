package smarthome.automation

import smarthome.core.DateUtils;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Stats conso maison par année
 *  
 * @author gregory
 *
 */
@Validateable
class HouseConso {
	static belongsTo = [house: House]
	
	Double kwHC
	Double kwHP
	Date dateConso
	
	
    static constraints = {
		house unique: "dateConso"	
    }
	
	static mapping = {
		house index: "HouseConso_Idx"
	}
	
	
	/**
	 * Retourne la conso juste avant la conso courante
	 * 
	 * @return
	 */
	HouseConso before() {
		house.consoByYear(year() - 1)	
	}
	
	
	/**
	 * Retourne la conso juste avant la conso courante
	 * 
	 * @return
	 */
	HouseConso after() {
		house.consoByYear(year() + 1)	
	}
	
	
	/**
	 * Conso totale
	 * 
	 * @return
	 */
	int consoTotale() {
		return 	(kwHC + kwHP) as Integer
	}
	
	
	/**
	 * L'année de la conso
	 * 
	 * @return
	 */
	int year() {
		return dateConso[Calendar.YEAR]	
	}
	
	
	/**
	 * Comparaison en pourcentage sur le total de la conso
	 * 
	 * @param conso
	 * @return
	 */
	int comparePercentTotal(HouseConso consoCompare) {
		int totalCompare = consoCompare.consoTotale()
		return (consoTotale() - totalCompare) * 100 / totalCompare
	}
	
	
	/**
	 * Comparaison en pourcentage sur la conso HP
	 * 
	 * @param conso
	 * @return
	 */
	int comparePercentHP(HouseConso consoCompare) {
		return (kwHP - consoCompare.kwHP) * 100 / consoCompare.kwHP
	}
	
	
	/**
	 * Comparaison en pourcentage sur la conso HP
	 * 
	 * @param conso
	 * @return
	 */
	int comparePercentHC(HouseConso consoCompare) {
		return (kwHC - consoCompare.kwHC) * 100 / consoCompare.kwHC
	}
	
	
	/**
	 * Créé la date de conso pour une année (1er janvier)
	 * 
	 * @param year
	 * @return
	 */
	static Date dateConsoForYear(int year) {
		return DateUtils.firstDayInYear(year)
	}
}
