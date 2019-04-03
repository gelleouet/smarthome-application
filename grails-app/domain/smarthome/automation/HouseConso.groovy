package smarthome.automation

import java.io.Serializable;
import java.util.Map;

import smarthome.automation.deviceType.AbstractDeviceType;
import smarthome.core.DateUtils;
import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Stats conso maison par année
 *  
 * @author gregory
 *
 */
@Validateable
class HouseConso implements Serializable {
	static belongsTo = [house: House]
	
	Double kwHC
	Double kwHP
	Double kwGaz
	Double kwBASE
	Date dateConso
	
	
    static constraints = {
		house unique: "dateConso"	
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		house index: "HouseConso_Idx"
		kwGaz column: 'kwgaz'
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
		return 	(kwHC + kwHP + kwGaz + kwBASE) as Integer
	}
	
	
	int consoTotaleBySurface() {
		if (house.surface) {
			return (consoTotale() / house.surface) as Integer
		}
		return 0
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
	double comparePercentTotal(HouseConso consoCompare) {
		int totalCompare = consoCompare.consoTotale()
		return (consoTotale() - totalCompare) * 100 / totalCompare
	}
	
	
	/**
	 * Comparaison en pourcentage sur la conso HP
	 * 
	 * @param conso
	 * @return
	 */
	double comparePercentHP(HouseConso consoCompare) {
		return (kwHP - consoCompare.kwHP) * 100 / consoCompare.kwHP
	}
	
	
	/**
	 * Comparaison en pourcentage sur la conso HP
	 * 
	 * @param conso
	 * @return
	 */
	double comparePercentHC(HouseConso consoCompare) {
		return (kwHC - consoCompare.kwHC) * 100 / consoCompare.kwHC
	}
	
	
	/**
	 * Comparaison en pourcentage sur la conso BASE
	 *
	 * @param conso
	 * @return
	 */
	double comparePercentBASE(HouseConso consoCompare) {
		return (kwBASE - consoCompare.kwBASE) * 100 / consoCompare.kwBASE
	}
	
	
	/**
	 * Comparaison en pourcentage sur la conso Gaz
	 *
	 * @param conso
	 * @return
	 */
	double comparePercentGaz(HouseConso consoCompare) {
		return (kwGaz - consoCompare.kwGaz) * 100 / consoCompare.kwGaz
	}
	
	
	/**
	 * Nombre de champ ayant une conso non nulle
	 * 
	 * @return
	 */
	int countFieldConso() {
		int count = 0
		
		if (kwHC) {
			count++
		}
		if (kwHP) {
			count++
		}
		if (kwGaz) {
			count++
		}
		if (kwBASE) {
			count++
		}
		
		return count
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
	
	
	/**
	 * Calcul les tarifs des consos en fonction des types de compteur
	 *
	 * @param compteurElec 
	 * @param compteurGaz (optionel)
	 * 
	 * @return
	 */
	Map calculTarif(AbstractDeviceType compteurElec, AbstractDeviceType compteurGaz) {
		def consos = [optTarifElec: compteurElec.getOptTarif()]
		consos.tarifTotal = 0
		
		if (consos.optTarifElec == 'HC') {
			consos.tarifHP = compteurElec.calculTarif('HP', kwHP, year())
			consos.tarifHC = compteurElec.calculTarif('HC', kwHC, year())
		} else if (consos.optTarifElec == 'EJP') {
			consos.tarifHP = compteurElec.calculTarif('PM', kwHP, year())
			consos.tarifHC = compteurElec.calculTarif('HN', kwHC, year())
		} else {
			consos.tarifBASE = compteurElec.calculTarif('BASE', kwBASE, year())
		}
		
		if (compteurGaz) {
			consos.tarifGaz = compteurGaz.calculTarif('BASE', kwGaz, year())
		}
		
		if (consos.tarifHP) {
			consos.tarifTotal += consos.tarifHP
		}
		if (consos.tarifHC) {
			consos.tarifTotal += consos.tarifHC
		}
		if (consos.tarifBASE) {
			consos.tarifTotal += consos.tarifBASE
		}
		if (consos.tarifGaz) {
			consos.tarifTotal += consos.tarifGaz
		}
		
		return consos
	}
}
