package smarthome.automation.weather

abstract class AbstractWeatherParser implements WeatherParser {

	/**
	 * Convertit vers Integer sans erreur
	 * 
	 * @param value
	 * @return
	 */
	Integer toInteger(def value) {
		if (value instanceof Number) {
			return value.toInteger()
		}	
		
		return value.toInteger()
	}
	
	
	/**
	 * Convertit en pourcentage une valeur comprise entre 0 et 1
	 * 
	 * @param value
	 * @return
	 */
	Integer toPourcent(def value) {
		if (value instanceof Number) {
			return (value * 100) as Integer
		}	
		
		return null
	}
}
