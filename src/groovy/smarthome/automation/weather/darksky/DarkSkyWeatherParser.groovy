package smarthome.automation.weather.darksky

import grails.converters.JSON;

import java.util.List;

import smarthome.automation.HouseWeather;
import smarthome.automation.weather.AbstractWeatherParser;
import smarthome.automation.weather.WeatherDayForecast;
import smarthome.automation.weather.WeatherHourForecast;
import smarthome.core.SmartHomeException;

/**
 * Parser météo Dark Sky
 * 
 * @author Gregory
 *
 */
class DarkSkyWeatherParser extends AbstractWeatherParser {

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
		def timeZone = data.timezone
		
		data.hourly?.data?.each {
			WeatherHourForecast forecast = new WeatherHourForecast()
			forecasts << forecast
			
			forecast.date = toDate(it.time, timeZone)
			forecast.icon = it.icon
			forecast.style = icons[it.icon]?.style
			forecast.text = icons[it.icon]?.text
			forecast.precipProbability = toPourcent(it.precipProbability)
			forecast.precipType = icons[it.precipType]?.text
			forecast.temperature = toInteger(it.temperature)
			forecast.apparentTemperature = toInteger(it.apparentTemperature)
			forecast.humidity = toPourcent(it.humidity)
			forecast.pressure = toInteger(it.pressure)
			forecast.windSpeed = toKmh(it.windSpeed)
			forecast.windGust = toKmh(it.windGust)
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
		def timeZone = data.timezone
		
		data.daily?.data?.each {
			WeatherDayForecast forecast = new WeatherDayForecast()
			forecasts << forecast
			
			forecast.date = toDate(it.time, timeZone)
			forecast.icon = it.icon
			forecast.style = icons[it.icon]?.style
			forecast.text = icons[it.icon]?.text
			forecast.sunrise = toDate(it.sunriseTime, timeZone)
			forecast.sunset = toDate(it.sunsetTime, timeZone)
			forecast.moonPhase = it.moonPhase
			forecast.precipProbability = toPourcent(it.precipProbability)
			forecast.precipType = icons[it.precipType]?.text
			forecast.precipIntensityMaxTime = toDate(it.precipIntensityMaxTime, timeZone)
			forecast.temperatureHigh = toInteger(it.temperatureHigh)
			forecast.apparentTemperatureHigh = toInteger(it.apparentTemperatureHigh)
			forecast.temperatureHighTime = toDate(it.temperatureHighTime, timeZone)
			forecast.apparentTemperatureHighTime = toDate(it.apparentTemperatureHighTime, timeZone)
			forecast.temperatureLow = toInteger(it.temperatureLow)
			forecast.apparentTemperatureLow = toInteger(it.apparentTemperatureLow)
			forecast.temperatureLowTime = toDate(it.temperatureLowTime, timeZone)
			forecast.apparentTemperatureLowTime = toDate(it.apparentTemperatureLowTime, timeZone)
			forecast.humidity = toPourcent(it.humidity)
			forecast.pressure = toInteger(it.pressure)
			forecast.windSpeed = toKmh(it.windSpeed)
			forecast.windGust = toKmh(it.windGust)
			forecast.windGustTime = toDate(it.windGustTime, timeZone)
			forecast.windBearing = toInteger(it.windBearing)
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
	Date toDate(def value, String timeZone) {
		if (value instanceof Number) {
			Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZone))
			calendar.setTimeInMillis(value * 1000L) // IMPORTANT : caster en long sinon débordement valeur
			return calendar.getTime()
		}	
		
		return null
	}
	
	
	/**
	 * Transforme en km/h des m/s
	 * 
	 * @param value
	 * @return
	 */
	Integer toKmh(def value) {
		if (value instanceof Number) {
			return (value * 3.6) as Integer
		}
		
		return null
	}
}
