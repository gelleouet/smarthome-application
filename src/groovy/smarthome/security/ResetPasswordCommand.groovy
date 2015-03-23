package smarthome.security

import smarthome.security.SmartHomeSecurityUtils;
import grails.validation.Validateable;

/**
 * Formulaire pour réinitialiser un mot de passe perdu
 * 
 * @author gregory
 *
 */
@Validateable
class ResetPasswordCommand {
	String username
	String newPassword
	String confirmPassword
	String token
	
	
	static constraints = {
		username blank: false
		token blank: false
		newPassword blank: false, validator: SmartHomeSecurityUtils.passwordValidator
		confirmPassword blank: false, validator: SmartHomeSecurityUtils.passwordConfirmValidator
	}
	
	
	static {
		grails.converters.JSON.registerObjectMarshaller(ResetPasswordCommand) {
			[username: it.username, token: it.token]
		}
	}
}
