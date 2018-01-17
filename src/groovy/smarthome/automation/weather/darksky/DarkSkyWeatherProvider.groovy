package smarthome.automation.weather.darksky

import grails.converters.JSON;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import smarthome.automation.HouseWeather;
import smarthome.automation.weather.AbstractWeatherProvider;
import smarthome.core.SmartHomeException;


/**
 * Provider météo DarkSky (ie Forecast.io)
 * 
 * @author Gregory
 *
 */
class DarkSkyWeatherProvider extends AbstractWeatherProvider {

	/**
	 *  (non-Javadoc)
	 * @see smarthome.automation.weather.WeatherProvider#execute(smarthome.automation.HouseWeather)
	 */
	@Override
	HouseWeather execute(HouseWeather houseWeather, def params) throws SmartHomeException {
		if (!houseWeather.house.latitude || !houseWeather.house.longitude) {
			return houseWeather
		}
		
		try {
			String url = """${params.url}/${params.apikey}/${houseWeather.house.latitude},${houseWeather.house.longitude}"""
			
			URI weatherUri = new URIBuilder(url)
				.setParameter("lang", "fr")
				.setParameter("units", "si")
				.setParameter("format", "json")
				.setParameter("exclude", "currently,minutely,flags")
				.build()
				
			HttpResponse response = Request.Get(weatherUri)
				.execute()
				.returnResponse()
			 
			if (response.statusLine.statusCode  != 200) {
				throw new SmartHomeException("DarkSky API return ${response.statusLine.statusCode} code !")
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
