/**
 * 
 */
package smarthome.application.granddefi

import smarthome.automation.Chauffage
import smarthome.automation.ECS
import smarthome.common.Commune
import smarthome.security.Profil
import smarthome.security.SmartHomeSecurityUtils
import grails.validation.Validateable


/**
 * Formulaire pour créer un nouveau compte
 * 
 * @author gregory
 *
 */
@Validateable
class AccountCommand {
	Profil profil
	String prenom
	String nom
	String username
	String newPassword
	String confirmPassword
	boolean profilPublic
	Commune commune
	Integer surface
	Chauffage chauffage
	ECS ecs


	static constraints = {
		newPassword blank: false, validator: SmartHomeSecurityUtils.passwordValidator
		confirmPassword blank: false, validator: SmartHomeSecurityUtils.passwordConfirmValidator
	}


	static {
		grails.converters.JSON.registerObjectMarshaller(AccountCommand) {
			[prenom: it.prenom, nom: it.nom, username: it.username]
		}
	}
}
