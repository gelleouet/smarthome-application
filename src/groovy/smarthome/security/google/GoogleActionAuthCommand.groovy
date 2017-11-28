/**
 * 
 */
package smarthome.security.google

import org.apache.commons.lang.StringUtils;

import smarthome.core.SmartHomeException;
import grails.validation.Validateable


/**
 * OAuth request params for google actions
 * 
 * @author gregory
 *
 */
@Validateable
class GoogleActionAuthCommand {
	String applicationId
	String client_id
	String redirect_uri
	String state
	String response_type
	String j_username
	String j_password
	String error
	String scope
	
	
	/**
	 * L'url de redirection complétée
	 * 
	 * @return
	 */
	String redirectUrl(String token) {
		return "${redirect_uri}#access_token=${token}&token_type=bearer&state=${state}"	
	}
}
