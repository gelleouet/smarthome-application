package smarthome.automation

import org.springframework.security.access.annotation.Secured;
import smarthome.core.AbstractController;


@Secured("isAuthenticated()")
class HouseWeatherController extends AbstractController {

	HouseWeatherService houseWeatherService
	DeviceService deviceService
	
	
	/**
	 * 
	 * @param house
	 * @return
	 */
	def widgetWeather(House house) {
		def forecast = houseWeatherService.currentHourlyForecast(house)
		def dailyForecast = houseWeatherService.currentDailyForecast(house)
		render (template: 'widgetWeather', model: [house: house, forecast: forecast,
			dailyForecast: dailyForecast])	
	}
	
	
	/**
	 * Rendu de la synthese confort de la maison
	 * 
	 * @return
	 */
	@Secured("hasRole('ROLE_ADMIN')")
	def calculWeather(House house) {
		houseWeatherService.calculWeather(house)
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
