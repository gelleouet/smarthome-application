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
	private static final Map RAPPORT_CONSO_MOIS_ELEC = [
		(Calendar.JANUARY): 1, (Calendar.FEBRUARY): 1, (Calendar.MARCH): 0.75,
		(Calendar.APRIL): 0.5, (Calendar.MAY): 0.25, (Calendar.JUNE): 0.25,
		(Calendar.JULY): 0.25, (Calendar.AUGUST): 0.25, (Calendar.SEPTEMBER): 0.25,
		(Calendar.OCTOBER): 0.5, (Calendar.NOVEMBER): 1, (Calendar.DECEMBER): 1,
	]
	private static final Map RAPPORT_CONSO_MOIS_POMPE = [
		(Calendar.JANUARY): 1, (Calendar.FEBRUARY): 1, (Calendar.MARCH): 0.75,
		(Calendar.APRIL): 0.5, (Calendar.MAY): 0.5, (Calendar.JUNE): 0.5,
		(Calendar.JULY): 0.5, (Calendar.AUGUST): 0.5, (Calendar.SEPTEMBER): 0.5,
		(Calendar.OCTOBER): 0.75, (Calendar.NOVEMBER): 1, (Calendar.DECEMBER): 1,
	]
	private static final Map RAPPORT_CONSO_MOIS_AUTRE = [
		(Calendar.JANUARY): 1, (Calendar.FEBRUARY): 1, (Calendar.MARCH): 1,
		(Calendar.APRIL): 1, (Calendar.MAY): 1, (Calendar.JUNE): 1,
		(Calendar.JULY): 1, (Calendar.AUGUST): 1, (Calendar.SEPTEMBER): 1,
		(Calendar.OCTOBER): 1, (Calendar.NOVEMBER): 1, (Calendar.DECEMBER): 1,
	]
	
	Map parameters
	
	
	@Override
	public House execute(House house) throws SmartHomeException {
		def rapportConsoMois = RAPPORT_CONSO_MOIS_ELEC
		
		if (house.chauffage) {
			if (house.chauffage.libelle == "Pompe à chaleur") {
				rapportConsoMois = RAPPORT_CONSO_MOIS_POMPE
			} else {
				rapportConsoMois = RAPPORT_CONSO_MOIS_AUTRE
			}
		} 		
		
		int year = parameters.year
		Date debutYear = DateUtils.firstDayInYear(year)
		def compteur = house.compteurElectriqueImpl()
		
		// calcul d'un tableau avec les consos réelles et estimées de chaque mois
		// si un mois n'est pas encore commencé, on se base sur les mois passés pour
		// estimer la conso avec les rapports
		
		Map consoMois = [:]
		
		// 1ere passe avec les mois déjà commencés
		for (int mois = Calendar.JANUARY; mois <= Calendar.DECEMBER; mois++) {
			// recherche des 1eres et dernières conso sur mois
			Date debutMois = debutYear.copyWith([month: mois])
			Date finMois = DateUtils.lastDayInMonth(debutMois)
			def maxJour = finMois[Calendar.DAY_OF_MONTH]
			
			DeviceValue firstHP 
			DeviceValue lastHP 
			DeviceValue firstHC
			DeviceValue lastHC
			DeviceValue firstBASE
			DeviceValue lastBASE
			
			// rajouter +1 sur date fin car param exclusif
			if (compteur.optTarif in ['HC', 'EJP']) {
				firstHP = DeviceValue.firstValueInPeriod(house.compteur, debutMois, finMois + 1, 'hchp')
				lastHP = DeviceValue.lastValueInPeriod(house.compteur, debutMois, finMois + 1, 'hchp')
				firstHC = DeviceValue.firstValueInPeriod(house.compteur, debutMois, finMois + 1, 'hchc')
				lastHC = DeviceValue.lastValueInPeriod(house.compteur, debutMois, finMois + 1, 'hchc')
			} else {
				firstBASE = DeviceValue.firstValueInPeriod(house.compteur, debutMois, finMois + 1, 'base')
				lastBASE = DeviceValue.lastValueInPeriod(house.compteur, debutMois, finMois + 1, 'base')
			}
			
			// on ne continue le calcul que si les 4 values sont trouvées
			if (firstHP && lastHP && firstHC && lastHC) {
				def nbJour = (lastHP.dateValue - firstHP.dateValue) + 1
				
				// pour extrapoller sur le mois, il nous faut au moins 1 jour complet
				if (nbJour > 2) {
					// produit en croix pour extrapoller sur le mois complet
					consoMois[mois] = [:]
					consoMois[mois].kwhp = (((lastHP.value - firstHP.value) / 1000) * maxJour / nbJour) as Integer
					consoMois[mois].kwhc = (((lastHC.value - firstHC.value) / 1000) * maxJour / nbJour) as Integer
				}
			} else if (firstBASE && lastBASE) {
				def nbJour = (lastBASE.dateValue - firstBASE.dateValue) + 1
				
				// pour extrapoller sur le mois, il nous faut au moins 1 jour complet
				if (nbJour > 2) {
					// produit en croix pour extrapoller sur le mois complet
					consoMois[mois] = [:]
					consoMois[mois].kwbase = (((lastBASE.value - firstBASE.value) / 1000) * maxJour / nbJour) as Integer
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
				def rapportMois = rapportConsoMois[mois]
				
				// calcul des consos en ramenant la conso du mois référence sur un rapport de 1
				// pour ensuite appliquer le rapport du mois
				consoMois[mois] = [:]
				
				if (consoReference.value.kwhp != null && consoReference.value.kwhc != null) {
					consoMois[mois].kwhp = ((consoReference.value.kwhp / rapportConsoMois[consoReference.key]) * rapportMois) as Integer
					consoMois[mois].kwhc = ((consoReference.value.kwhc / rapportConsoMois[consoReference.key]) * rapportMois) as Integer
				} else if (consoReference.value.kwbase != null) {
					consoMois[mois].kwbase = ((consoReference.value.kwbase / rapportConsoMois[consoReference.key]) * rapportMois) as Integer
				}
			}
		}
		
		// A ce stade, on a toutes les consos par mois
		// on peut faire les sommes et sauvegarder le résultat dans l'objet conso de l'année
		HouseConso conso = house.consoByYear(year)
		
		if (!conso) {
			conso = house.newConsoForYear(year)
		}
		
		def allConsos = consoMois.collect { it.value }
		
		conso.kwHP = allConsos.sum { it.kwhp ?: 0d }
		conso.kwHC = allConsos.sum { it.kwhc ?: 0d }
		conso.kwBASE = allConsos.sum { it.kwbase ?: 0d }
		conso.kwGaz = 0
		conso.save()
		
		return house
	}

}