package smarthome.automation.weather.weatherkit

import org.apache.http.HttpResponse
import org.apache.http.client.fluent.Request
import org.apache.http.client.utils.URIBuilder
import smarthome.automation.HouseWeather
import smarthome.automation.weather.AbstractWeatherProvider
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException

/**
 * Provider météo Apple WeatherKit
 *
 * https://weatherkit.apple.com/api/v1/availability/47.795017/-3.487062?country=FR
 * https://weatherkit.apple.com/api/v1/weather/fr_FR/47.795017/-3.487062?timezone=Europe/Paris&dataSets=currentWeather,forecastDaily,forecastHourly,weatherAlerts&hourlyEnd=2023-11-23T23:00:00
 *
 * @author Gregory
 *
 */
class AppleWeatherKitProvider extends AbstractWeatherProvider {

	/**
	 *  (non-Javadoc)
	 * @see smarthome.automation.weather.WeatherProvider#execute(smarthome.automation.HouseWeather)
	 */
	@Override
	HouseWeather execute(HouseWeather houseWeather, def params) throws SmartHomeException {
		try {
			String url = """${params.url}/${houseWeather.house.latitude}/${houseWeather.house.longitude}"""
			Date hourlyEnd = DateUtils.truncHour(new Date() + 1)

			URI weatherUri = new URIBuilder(url)
				.setParameter("timezone", "Europe/Paris")
				.setParameter("countryCode", "FR")
				.setParameter("dataSets", "currentWeather,forecastDaily,forecastHourly")
				.setParameter("hourlyEnd", DateUtils.formatDateTimeIso(hourlyEnd))
				.build()
				
			HttpResponse response = Request.Get(weatherUri)
				.addHeader("Authorization", "Bearer ${ params.apikey }")
				.execute()
				.returnResponse()
			 
			if (response.statusLine.statusCode  != 200) {
				throw new SmartHomeException("WeatherKit API return ${response.statusLine.statusCode} code !")
			}
			
			response.entity?.content.withStream {
				houseWeather.data = it.text
			}
		} catch (Exception ex) {
			throw new SmartHomeException(ex)
		}
		
		return houseWeather
	}

}
