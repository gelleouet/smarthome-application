package smarthome.security

import java.util.Date;

import smarthome.security.SmartHomeSecurityConstantes;


/**
 * Token pour la validation création d'un compte
 * ou pour retrouver un mot de passe
 * 
 * @author gregory
 *
 */
class RegistrationCode {
	String username
	String token = UUID.randomUUID().toString().replaceAll('-', '')
	Date dateCreated
	String serverUrl

	static mapping = {
		version false
	}
	
}
