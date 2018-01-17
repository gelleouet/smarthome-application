package smarthome.automation.weather.yahoo

import grails.converters.JSON;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import smarthome.automation.HouseWeather;
import smarthome.automation.weather.AbstractWeatherProvider;
import smarthome.core.SmartHomeException;


/**
 * Provider météo Yahoo
 * 
 * @author Gregory
 *
 */
class YahooWeatherProvider extends AbstractWeatherProvider {

	/**
	 *  (non-Javadoc)
	 * @see smarthome.automation.weather.WeatherProvider#execute(smarthome.automation.HouseWeather)
	 */
	@Override
	HouseWeather execute(HouseWeather houseWeather, def params) throws SmartHomeException {
		try {
			String yql = """select * from weather.forecast where woeid in (select woeid from geo.places where text="${houseWeather.house.location}") and u="c" """
			
			URI weatherUri = new URIBuilder(params.url)
				.setParameter("q", yql)
				.setParameter("format", "json")
				.build()
				
			HttpResponse response = Request.Post(weatherUri)
				.execute()
				.returnResponse()
			 
			if (response.statusLine.statusCode  != 200) {
				throw new SmartHomeException("Yahoo Weather API return ${response.statusLine.statusCode} code !")
			}
			
			response.entity?.content.withStream {
				def json = JSON.parse(it.text)
				def channel = json.query?.results?.channel
				
				if (channel) {
					houseWeather.data = channel.toString() 
				}
			}
		} catch (Exception ex) {
			throw new SmartHomeException(ex)
		}
		
		return houseWeather
	}

}
