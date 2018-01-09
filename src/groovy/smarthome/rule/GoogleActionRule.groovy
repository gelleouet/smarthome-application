package smarthome.rule

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import smarthome.automation.Device;
import smarthome.automation.DeviceService;
import smarthome.core.SmartHomeException;
import smarthome.security.User;
import smarthome.security.google.action.GoogleActionRequest;
import smarthome.security.google.action.GoogleActionResponse;



/**
 * Gestion d'une conversation
 * 
 * @author gregory
 *
 */
class GoogleActionRule implements Rule<GoogleActionRequest, GoogleActionResponse> {

	@Autowired
	DeviceService deviceService
	
	@Autowired
	MessageSource messageSource
	
	
	Map parameters
	Locale locale
	
	
	@Override
	GoogleActionResponse execute(GoogleActionRequest request) throws SmartHomeException {
		User user = parameters.user
		locale = Locale.getDefault()
		
		GoogleActionResponse response = new GoogleActionResponse()
		response.requestId = request.requestId
		
		return response
	}
	
	
	/**
	 * Simplifie l'appel au traducteur à cause du tableau des params et locale
	 * 
	 * @param code
	 * @param params
	 * @param locale
	 * @return
	 */
	String i18nBundle(code, params = []) {
		return messageSource.getMessage(code, params as Object[], locale)
	}
}
