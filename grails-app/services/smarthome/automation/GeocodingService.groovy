package smarthome.automation

import grails.converters.JSON;

import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;

import smarthome.core.AbstractService;
import smarthome.core.SmartHomeException;

class GeocodingService extends AbstractService {

	/**
	 * Calcule les coordonnées GPS (latitude, longitude) d'un lieu
	 * 
	 * @param lieu
	 * @return Map avec latitude, longitude, woeid en clé
	 * 
	 * @throws SmartHomeException
	 */
	Map geocode(String lieu) throws SmartHomeException {
		Map coords
		
		try {
			String yql = """select centroid, woeid from geo.places where text="${lieu}" """
			
			URI geoUri = new URIBuilder("https://query.yahooapis.com/v1/public/yql")
				.setParameter("q", yql)
				.setParameter("format", "json")
				.build()
				
			HttpResponse response = Request.Post(geoUri)
				.execute()
				.returnResponse()
			 
			if (response.statusLine.statusCode  != 200) {
				throw new SmartHomeException("Yahoo Geocoding API return ${response.statusLine.statusCode} code !")
			}
			
			response.entity?.content.withStream {
				def json = JSON.parse(it.text)
				def place = json.query?.results?.place
				
				if (place instanceof Collection) {
					throw new SmartHomeException("Geocode ${lieu} return more than 1 result !")
				}
				
				if (place) {
					coords = [latitude: place.centroid.latitude, longitude: place.centroid.longitude,
						woeid: place.woeid]
				}
			}
		} catch (Exception ex) {
			throw new SmartHomeException(ex)
		}
		
		return coords
	}
}
