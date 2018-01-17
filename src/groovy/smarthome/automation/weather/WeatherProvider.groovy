package smarthome.automation.weather

import smarthome.automation.HouseWeather;
import smarthome.core.SmartHomeException;

/**
 * Interface commune à tous les provider météo pour construire les objets HouseWeather
 * 
 * @author Gregory
 *
 */
interface WeatherProvider {

	HouseWeather execute(HouseWeather houseWeather, def params) throws SmartHomeException
}
