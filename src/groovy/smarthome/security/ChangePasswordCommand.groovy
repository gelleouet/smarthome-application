/**
 * 
 */
package smarthome.security

import smarthome.security.SmartHomeSecurityUtils;
import grails.validation.Validateable


/**
 * Formulaire pour changer un mot de passe par son propre utilisateur
 * 
 * @author gregory
 *
 */
@Validateable
class ChangePasswordCommand {
	String prenom
	String nom
	String username
	String oldPassword
	String newPassword
	String confirmPassword

	static constraints = {
		oldPassword blank: false
		newPassword blank: false, validator: SmartHomeSecurityUtils.passwordValidator
		confirmPassword blank: false, validator: SmartHomeSecurityUtils.passwordConfirmValidator
	}
	
	
	static {
		grails.converters.JSON.registerObjectMarshaller(ChangePasswordCommand) {
			[prenom: it.prenom, nom: it.nom, username: it.username]
		}
	}
}
