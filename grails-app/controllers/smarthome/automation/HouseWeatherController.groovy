package smarthome.automation

import org.springframework.security.access.annotation.Secured;

import smarthome.core.AbstractController;
import smarthome.security.User;


@Secured("isAuthenticated()")
class HouseWeatherController extends AbstractController {

	HouseWeatherService houseWeatherService
	HouseService houseService
	DeviceService deviceService
	
	
	/**
	 * 
	 * @param house
	 * @return
	 */
	def widgetWeather(House house) {
		def forecast
		def dailyForecast
		
		if (house) {
			forecast = houseWeatherService.currentHourlyForecast(house)
			dailyForecast = houseWeatherService.currentDailyForecast(house)
		}
		
		render (template: 'widgetWeather', model: [house: house, forecast: forecast,
			dailyForecast: dailyForecast])	
	}
	
	
	/**
	 * Calcul info météo
	 * 
	 * @return
	 */
	@Secured("hasRole('ROLE_ADMIN')")
	def calculWeather(House house) {
		houseWeatherService.calculWeather(house)
		nop()
	}
	
	
	/**
	 * Calcul info météo maison principale
	 * 
	 * @return
	 */
	@Secured("hasRole('ROLE_ADMIN')")
	def calculDefaultWeather(User user) {
		House house = houseService.findDefaultByUser(user)
		if (house) {
			houseWeatherService.calculWeather(house)
		}
		nop()
	}
	
	
	/**
	 * Prévisions météo des prochaines heures
	 * 
	 * @param house
	 * @return
	 */
	def hourlyForecast(House house) {
		def tableauBords = deviceService.groupByTableauBord(house.user.id)
		def hourlyForecasts = houseWeatherService.hourlyForecast(house)
		render(view: 'hourlyForecast', model: [tableauBords: tableauBords, house: house,
			hourlyForecasts: hourlyForecasts])
	}
	
	
	/**
	 * Prévisions météo des prochains jours
	 * 
	 * @param house
	 * @return
	 */
	def dailyForecast(House house) {
		def tableauBords = deviceService.groupByTableauBord(house.user.id)
		def dailyForecasts = houseWeatherService.dailyForecast(house)
		render(view: 'dailyForecast', model: [tableauBords: tableauBords, house: house,
			dailyForecasts: dailyForecasts])
	}
	
	
}
