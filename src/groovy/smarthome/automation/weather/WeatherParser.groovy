package smarthome.automation.weather

import smarthome.automation.HouseWeather;
import smarthome.core.SmartHomeException;

/**
 * Interface commune à tous les parser météo pour la lire les datas
 * d'un objet HouseWeather
 * 
 * @author Gregory
 *
 */
interface WeatherParser {
	/**
	 * Parse l'objet contenant les datas météo
	 * 
	 * @param houseWeather
	 * @throws SmartHomeException
	 */
	void parse(HouseWeather houseWeather) throws SmartHomeException
	
	
	/**
	 * Les prévisions des prochaines heures
	 * 
	 * @return
	 */
	List<WeatherHourForecast> hourlyForecast()
	
	
	/**
	 * Les prévisions des prochains jours
	 *
	 * @return
	 */
	List<WeatherDayForecast> dailyForecast()
}
