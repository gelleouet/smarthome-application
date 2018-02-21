package smarthome.rule


import java.util.Map.Entry;

import smarthome.automation.DeviceValue;
import smarthome.automation.House;
import smarthome.automation.HouseConso;
import smarthome.core.DateUtils;
import smarthome.core.SmartHomeException;



/**
 * Calcule estimation consommation
 * Pré-requis : le compteur doit être associé à la maison
 * 
 * @author gregory
 *
 */
class HouseEstimationConsoRule implements Rule<House, House> {

	// Rapport des consos pour chaque mois
	private static final Map RAPPORT_CONSO_MOIS = [
		(Calendar.JANUARY): 1, (Calendar.FEBRUARY): 1, (Calendar.MARCH): 0.75,
		(Calendar.APRIL): 0.5, (Calendar.MAY): 0.25, (Calendar.JUNE): 0.25,
		(Calendar.JULY): 0.25, (Calendar.AUGUST): 0.25, (Calendar.SEPTEMBER): 0.25,
		(Calendar.OCTOBER): 0.3, (Calendar.NOVEMBER): 1, (Calendar.DECEMBER): 1,
	]
	
	Map parameters
	
	
	@Override
	public House execute(House house) throws SmartHomeException {
		int year = parameters.year
		Date debutYear = DateUtils.firstDayInYear(year)
		
		// calcul d'un tableau avec les consos réelles et estimées de chaque mois
		// si un mois n'est pas encore commencé, on se base sur les mois passés pour
		// estimer la conso avec les rapports
		
		Map consoMois = [:]
		
		// 1ere passe avec les mois déjà commencés
		for (int mois = Calendar.JANUARY; mois <= Calendar.DECEMBER; mois++) {
			// recherche des 1eres et dernières conso sur mois
			Date debutMois = debutYear.copyWith([month: mois])
			Date finMois = DateUtils.lastDayInMonth(debutMois)
			
			// rajouter +1 sur date fin car param exclusif
			DeviceValue firstHP = DeviceValue.firstValueInPeriod(house.compteur, debutMois, finMois + 1, 'hchp')
			DeviceValue lastHP = DeviceValue.lastValueInPeriod(house.compteur, debutMois, finMois + 1, 'hchp')
			DeviceValue firstHC = DeviceValue.firstValueInPeriod(house.compteur, debutMois, finMois + 1, 'hchc')
			DeviceValue lastHC = DeviceValue.lastValueInPeriod(house.compteur, debutMois, finMois + 1, 'hchc')
			
			// on ne continue le calcul que si les 4 values sont trouvées
			if (firstHP && lastHP) {
				def nbJour = (lastHP.dateValue - firstHP.dateValue) + 1
				
				// pour extrapoller sur le mois, il nous faut au moins 1 jour complet
				if (nbJour > 2) {
					def maxJour = finMois[Calendar.DAY_OF_MONTH]
					
					// produit en croix pour extrapoller sur le mois complet
					consoMois[mois] = [:]
					consoMois[mois].kwhp = (((lastHP.value - firstHP.value) / 1000) * maxJour / nbJour) as Integer
					
					if (firstHC && lastHC) {
						consoMois[mois].kwhc = (((lastHC.value - firstHC.value) / 1000) * maxJour / nbJour) as Integer
					} else {
						consoMois[mois].kwhc = 0
					}
				}
			}
		}
		
		// si aucune conso trouvée, pas la peine de continuer car on ne pourra pas déduire les consos
		if (!consoMois) {
			return house
		}
		
		// 2e passe pour compléter les mois qui ne sont pas calculés en se basant sur la conso
		// du 1er mois calculé et en appliquant les rapports entre mois
		Entry consoReference =  consoMois.find { true }
		
		for (int mois = Calendar.JANUARY; mois <= Calendar.DECEMBER; mois++) {
			if (!consoMois[mois]) {
				// récupère le rapport du mois
				def rapportMois = RAPPORT_CONSO_MOIS[mois]
				
				// calcul des consos en ramenant la conso du mois référence sur un rapport de 1
				// pour ensuite appliquer le rapport du mois
				consoMois[mois] = [:]
				consoMois[mois].kwhp = ((consoReference.value.kwhp / RAPPORT_CONSO_MOIS[consoReference.key]) * rapportMois) as Integer
				consoMois[mois].kwhc = ((consoReference.value.kwhc / RAPPORT_CONSO_MOIS[consoReference.key]) * rapportMois) as Integer
			}
		}
		
		// A ce stade, on a toutes les consos par mois
		// on peut faire les sommes et sauvegarder le résultat dans l'objet conso de l'année
		HouseConso conso = house.consoByYear(year)
		
		if (!conso) {
			conso = house.newConsoForYear(year)
		}
		
		def allConsos = consoMois.collect { it.value }
		
		conso.kwHP = allConsos.sum { it.kwhp }
		conso.kwHC = allConsos.sum { it.kwhc }
		conso.save()
		
		return house
	}

}
