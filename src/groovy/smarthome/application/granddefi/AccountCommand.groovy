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
	boolean autorise_user_data = true
	boolean autorise_conso_data = true
	boolean autorise_share_data
	boolean engage_enedis_account = true
	String adresse
	String codePostal
	String telephone
	Commune commune
	Integer surface
	Chauffage chauffage
	Chauffage chauffageSecondaire
	ECS ecs
	Integer nbPersonne


	static constraints = {
		newPassword blank: false, validator: SmartHomeSecurityUtils.passwordValidator
		confirmPassword blank: false, validator: SmartHomeSecurityUtils.passwordConfirmValidator
		surface nullable: true
		nbPersonne nullable: true
		chauffageSecondaire nullable: true
		ecs nullable: true
	}


	static {
		grails.converters.JSON.registerObjectMarshaller(AccountCommand) {
			[prenom: it.prenom, nom: it.nom, username: it.username]
		}
	}
}
