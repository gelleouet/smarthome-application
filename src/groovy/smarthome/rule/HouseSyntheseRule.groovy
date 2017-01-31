package smarthome.rule


import smarthome.automation.House;
import smarthome.automation.HouseSynthese;
import smarthome.core.SmartHomeException;



/**
 * Calcule les interprétations d'une maison
 * 
 * @author gregory
 *
 */
class HouseSyntheseRule implements Rule<House, HouseSynthese> {

	Map parameters
	
	
	@Override
	public HouseSynthese execute(House house) throws SmartHomeException {
		HouseSynthese synthese = new HouseSynthese(house: house)
		def interpretation
		
		// température
		if (house.temperature) {
			interpretation = (synthese.interpretations[house.temperature.id] = [:])
			double temperature = house.temperature.value as Double
			
			if (temperature < 17) {
				interpretation.commentaire = "Inconfort - Froid"
				interpretation.pourcentage = 30
			} else if (temperature >= 17 && temperature < 19) {
				interpretation.commentaire = "Supportable - Légèrement froid"
				interpretation.pourcentage = 75
			} else if (temperature >= 19 && temperature <= 23) {
				interpretation.commentaire = "Confort idéal"
				interpretation.pourcentage = 100
			} else if (temperature > 23 && temperature <= 25) {
				interpretation.commentaire = "Confort - Légèrement chaud"
				interpretation.pourcentage = 75
			} else {
				interpretation.commentaire = "Inconfort - Chaud"
				interpretation.pourcentage = 30
			}
		}
		
		// humidité
		if (house.humidite) {
			interpretation = (synthese.interpretations[house.humidite.id] = [:])
			double humidite = house.humidite.value as Double
			
			if (humidite < 40) {
				interpretation.commentaire = "Inconfort - Sec"
				interpretation.pourcentage = 50
			} else if (humidite >= 40 && humidite < 65) {
				interpretation.commentaire = "Confort idéal"
				interpretation.pourcentage = 100
			} else {
				interpretation.commentaire = "Inconfort - Humide"
				interpretation.pourcentage = 50
			}
		}
		
		return synthese
	}

}
