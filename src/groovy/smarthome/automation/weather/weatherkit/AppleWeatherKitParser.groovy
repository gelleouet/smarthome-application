package smarthome.automation.weather.weatherkit

import grails.converters.JSON
import smarthome.automation.HouseWeather
import smarthome.automation.weather.AbstractWeatherParser
import smarthome.automation.weather.WeatherDayForecast
import smarthome.automation.weather.WeatherHourForecast
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException

/**
 * Parser météo Weather Kit
 *
 * @see https://developer.apple.com/documentation/weatherkitrestapi
 * 
 * @author Gregory
 *
 */
class AppleWeatherKitParser extends AbstractWeatherParser {

	private static final TimeZone _defaultTimeZone = TimeZone.getTimeZone("Europe/Paris")

	Map data = [:]
	Map icons = [
		'clear-day': [style: 'wi-day-sunny', text: 'Dégagé'],
		'clear-night': [style: 'wi-night-clear', text: 'Dégagé'],
		'rain': [style: 'wi-rain', text: 'Pluie'],
		'snow': [style: 'wi-snow', text: 'Neige'],
		'sleet': [style: 'wi-sleet', text: 'Grêle'],
		'wind': [style: 'wi-strong-wind', text: 'Venté'],
		'fog': [style: 'wi-fog', text: 'Brouillard'],
		'cloudy': [style: 'wi-cloudy', text: 'Nuageux'],
		'partly-cloudy-day': [style: 'wi-day-cloudy', text: 'Nuage partiel'],
		'partly-cloudy-night': [style: 'wi-night-partly-cloudy', text: 'Nuage partiel'],
		'hail': [style: 'wi-hail', text: ''],
		'thunderstorm': [style: 'wi-thunderstorm', text: 'Orage'],
		'tornado': [style: 'wi-tornado', text: 'Tornade']
	]
	
	
	
	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.weather.WeatherParser#parse(smarthome.automation.HouseWeather)
	 */
	@Override
	void parse(HouseWeather houseWeather) throws SmartHomeException {
		data = JSON.parse(houseWeather.data)
	}

	
	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.weather.WeatherParser#hourlyForecast()
	 */
	@Override
	List<WeatherHourForecast> hourlyForecast() {
		List<WeatherHourForecast> forecasts = []

		data.forecastHourly?.hours?.each {
			WeatherHourForecast forecast = new WeatherHourForecast()
			forecasts << forecast
			
			forecast.date = toDate(it.forecastStart)
			forecast.icon = it.conditionCode
			forecast.style = icons[it.conditionCode]?.style
			forecast.text = icons[it.conditionCode]?.text
			forecast.precipProbability = toPourcent(it.precipitationChance)
			forecast.precipType = icons[it.precipitationType]?.text
			forecast.temperature = toInteger(it.temperature)
			forecast.apparentTemperature = toInteger(it.temperatureApparent)
			forecast.humidity = toPourcent(it.humidity)
			forecast.pressure = toInteger(it.pressure)
			forecast.windSpeed = it.windSpeed
			forecast.windGust = it.windGust
			forecast.windBearing = toInteger(it.windBearing)
			forecast.cloudCover = toPourcent(it.cloudCover)
			forecast.uvIndex = toInteger(it.uvIndex)
		}
		
		return forecasts
	}

	
	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.weather.WeatherParser#dailyForecast()
	 */
	@Override
	List<WeatherDayForecast> dailyForecast() {
		List<WeatherDayForecast> forecasts = []

		data.forecastDaily?.days?.each {
			WeatherDayForecast forecast = new WeatherDayForecast()
			forecasts << forecast
			
			forecast.date = toDate(it.forecastStart)?.clearTime()
			forecast.icon = it.icon
			forecast.style = icons[it.conditionCode]?.style
			forecast.text = icons[it.conditionCode]?.text
			forecast.sunrise = toDate(it.sunrise)
			forecast.sunset = toDate(it.sunset)
			forecast.precipProbability = toPourcent(it.precipitationChance)
			forecast.precipType = icons[it.precipitationType]?.text
			forecast.temperatureHigh = toInteger(it.temperatureMax)
			forecast.apparentTemperatureHigh = toInteger(it.temperatureMax)
			forecast.temperatureLow = toInteger(it.temperatureMin)
			forecast.apparentTemperatureLow = toInteger(it.temperatureMin)
			forecast.humidity = toPourcent(it.humidity)
			forecast.windSpeed = toInteger(it.windSpeed)
			forecast.windGust = toInteger(it.windGust)
			forecast.windBearing = toInteger(it.windDirection)
			forecast.cloudCover = toPourcent(it.cloudCover)
			forecast.uvIndex = toInteger(it.uvIndex)
		}
		
		return forecasts
	}
	
	
	/**
	 * Conversion des dates
	 * 
	 * @param value
	 * @return
	 */
	Date toDate(def value) {
		Calendar calendar = Calendar.getInstance(_defaultTimeZone)
		Date date = DateUtils.parseDateTimeIso(value)
		calendar.setTimeInMillis(date.time + _defaultTimeZone.getOffset(date.time))
		return calendar.time
	}
}
