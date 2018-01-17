package smarthome.automation

import java.util.List;
import java.util.Map;

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.springframework.transaction.annotation.Transactional;

import smarthome.automation.weather.WeatherDayForecast;
import smarthome.automation.weather.WeatherHourForecast;
import smarthome.automation.weather.WeatherParser;
import smarthome.automation.weather.WeatherProvider;
import smarthome.core.AbstractService;
import smarthome.core.AsynchronousWorkflow;
import smarthome.core.ClassUtils;
import smarthome.core.DateUtils;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class HouseWeatherService extends AbstractService {

	GrailsApplication grailsApplication
	EventService eventService
	HouseService houseService
	
	
	/**
	 * Compte le nombre de maisons pour les calculs des prévisions météo
	 *
	 * @return
	 */
	long countHouseForWeather() {
		return House.createCriteria().get {
			isNotNull "location"
			projections {
				count("id")
			}
		}
	}
	
	
	/**
	 * Les ids des maisons pour les calculs des prévisions météo
	 *
	 * @return
	 */
	List<Map> listHouseIdsForWeather(Map pagination) {
		return House.createCriteria().list(pagination) {
			isNotNull "location"
			projections {
				property "id", "id"
			}
			order "id"
			// transformer pour récupérer une map au lieu d'un tableau
			resultTransformer org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP
		}
	}
	
	
	/**
	 * Calcul des prévisions météo avec un provider
	 * 
	 * @param house
	 * @param providerImpl
	 * @param providerParams
	 * @return
	 * @throws SmarthomeException
	 */
	@AsynchronousWorkflow("houseWeatherService.calculWeather")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	HouseWeather calculWeather(House house, String providerImpl, def providerParams) throws SmartHomeException {
		if (!house.attached) {
			house.attach()
		}
		
		HouseWeather weather = house.weather()
		
		if (!house.location) {
			return weather
		}
		
		// vérifie le provider
		WeatherProvider provider = ClassUtils.forNameInstance(providerImpl)
		
		if (!weather) {
			weather = new HouseWeather(house: house)
		}	
		
		weather.dateWeather = new Date().clearTime()
		weather.providerClass = providerImpl
		provider.execute(weather, providerParams)
		
		this.save(weather)
		
		return updateSunriseAndSunsetEvent(weather)
	}
	
	
	/**
	 * Calcul des prévisions météo avec le provider par défaut (le 1er)
	 *
	 * @param house
	 * @param providerImpl
	 * @param providerParams
	 * @return
	 * @throws SmarthomeException
	 */
	@AsynchronousWorkflow("houseWeatherService.calculWeather")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	HouseWeather calculWeather(House house) throws SmartHomeException {
		def providers = grailsApplication.config.weather.providers
		
		if (!providers) {
			throw new SmartHomeException("No weather provider !")
		}
		
		return calculWeather(house, providers[0].impl, providers[0].params)
	}
	
	
	/**
	 * Instancie le parser pour météo
	 * 
	 * @param house
	 * @return
	 * @throws SmartHomeException
	 */
	WeatherParser weatherParser(HouseWeather houseWeather) throws SmartHomeException {
		def providers = grailsApplication.config.weather.providers
		
		def provider = providers.find {
			it.impl == houseWeather.providerClass
		}
		
		if (!provider?.parser) {
			throw new SmartHomeException("Provider not found or empty parser !")
		}
		
		WeatherParser parser = ClassUtils.forNameInstance(provider.parser)
		parser.parse(houseWeather)
		
		return parser
	}
	
	/**
	 * Les prévisions de l'heure en cours
	 * 
	 * @param house
	 * @return
	 * @throws SmartHomeException
	 */
	WeatherHourForecast currentHourlyForecast(House house) throws SmartHomeException {
		HouseWeather weather = house.weather()
		
		if (weather) {
			return currentHourlyForecast(weather)
		}
		
		return null
	}
	
	
	/**
	 * Les prévisions de l'heure en cours
	 *
	 * @param house
	 * @return
	 * @throws SmartHomeException
	 */
	WeatherHourForecast currentHourlyForecast(HouseWeather houseWeather) throws SmartHomeException {
		WeatherParser parser = weatherParser(houseWeather)
		Date currentDate = DateUtils.truncHour(new Date())
		
		return parser.hourlyForecast().find {
			it.date == currentDate
		}
	}
	
	
	/**
	 * Les prévisions du jour en cours
	 * 
	 * @param house
	 * @return
	 * @throws SmartHomeException
	 */
	WeatherDayForecast currentDailyForecast(House house) throws SmartHomeException {
		HouseWeather weather = house?.weather()
				
		if (weather) {
			return currentDailyForecast(weather)
		}
		
		return null
	}
	
	
	/**
	 * Prévision du jour en cours
	 * 
	 * @param houseWeather
	 * @return
	 * @throws SmartHomeException
	 */
	WeatherDayForecast currentDailyForecast(HouseWeather houseWeather) throws SmartHomeException {
		WeatherParser parser = weatherParser(houseWeather)
		Date currentDate = new Date().clearTime()
		
		return parser.dailyForecast().find {
			it.date == currentDate
		}
	}
	
	
	/**
	 * Prévisions des prochaines heures
	 * 
	 * @param house
	 * @return
	 * @throws SmartHomeException
	 */
	List<WeatherHourForecast> hourlyForecast(House house) throws SmartHomeException {
		HouseWeather weather = house.weather()
		
		if (weather) {
			WeatherParser parser = weatherParser(weather)
			return parser.hourlyForecast()
		}
		
		return []
	}
	
	
	/**
	 * Prévisions des prochaines heures
	 * 
	 * @param house
	 * @return
	 * @throws SmartHomeException
	 */
	List<WeatherDayForecast> dailyForecast(House house) throws SmartHomeException {
		HouseWeather weather = house.weather()
				
		if (weather) {
			WeatherParser parser = weatherParser(weather)
			return parser.dailyForecast()
		}
		
		return []
	}
	
	
	/**
	 * Créé ou met à jour 2 events associés aux couchers et levés du soleil
	 * 
	 * @param houseWeather
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	HouseWeather updateSunriseAndSunsetEvent(HouseWeather houseWeather) throws SmartHomeException {
		WeatherDayForecast dailyForecast = currentDailyForecast(houseWeather)
		
		if (dailyForecast) {
			// ajoute ou modifie 2 events
			String sunriseCron = "0 ${dailyForecast.sunrise[Calendar.MINUTE]} ${dailyForecast.sunrise[Calendar.HOUR_OF_DAY]} * * ?"
			eventService.createOrUpdateEvent("SMARTHOME_LEVER_SOLEIL", houseWeather.house.user, sunriseCron)
			
			String sunsetCron = "0 ${dailyForecast.sunset[Calendar.MINUTE]} ${dailyForecast.sunset[Calendar.HOUR_OF_DAY]} * * ?"
			eventService.createOrUpdateEvent("SMARTHOME_COUCHER_SOLEIL", houseWeather.house.user, sunsetCron)
		}
		
		return houseWeather
	}
	
	
	/**
	 * Prévisions prochaines heures et jour de la maison par défaut du user
	 * 
	 * @param user
	 * @return
	 * @throws SmartHomeException
	 */
	Map currentDefaultForecast(User user) throws SmartHomeException {
		Map forecasts = [:]
		House house = houseService.findDefaultByUser(user)
		HouseWeather houseWeather = house?.weather()
		
		if (houseWeather) {
			forecasts.daily = currentDailyForecast(houseWeather)
			forecasts.hourly = currentHourlyForecast(houseWeather)
		}
		
		return forecasts
	}
}
