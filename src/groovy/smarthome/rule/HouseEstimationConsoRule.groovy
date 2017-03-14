package smarthome.rule


import smarthome.automation.DeviceValue;
import smarthome.automation.House;
import smarthome.automation.HouseSynthese;
import smarthome.core.SmartHomeException;



/**
 * Calcule estimation consommation
 * 
 * @author gregory
 *
 */
class HouseEstimationConsoRule implements Rule<House, House> {

	Map parameters
	
	
	@Override
	public House execute(House house) throws SmartHomeException {
		/*Date dateFin = new Date().clearTime()
		
		// 1 seul calcul par jour si toutes les infos sont complètes
		if (house.surface && house.compteur && (!house.dateCalculConso || house.dateCalculConso < dateFin)) {
			Date dateDebut = dateFin.copyWith([date: 1, month: 0]) // 1er janvier
			def nbJour = (dateFin - 1) - dateDebut
			
			// pas de calcul les 2er jours de l'année car calcul sur J-1
			if (nbJour) {
				def firstHP = DeviceValue.firstValueByDay(house.compteur, 'hchp', dateDebut)
				def lastHP = DeviceValue.lastValueByDay(house.compteur, 'hchp', dateFin - 1)
				def firstHC = DeviceValue.firstValueByDay(house.compteur, 'hchc', dateDebut)
				def lastHC = DeviceValue.lastValueByDay(house.compteur, 'hchc', dateFin - 1)
				
				if (firstHP && lastHP && firstHC && lastHC) {
					def consoAnnuelle = (lastHP.value - firstHP.value) + (lastHC.value - firstHC.value)
					
					if (consoAnnuelle) {
						// produit en croix pour extrapoller sur une année complète
						house.consoAnnuelle = consoAnnuelle * 365 / nbJour
						house.consoAnnuelle = house.consoAnnuelle.round(0)
						house.dateCalculConso = dateFin
						house.save()
					}
				}
			}
		}*/
		
		return house
	}

}
