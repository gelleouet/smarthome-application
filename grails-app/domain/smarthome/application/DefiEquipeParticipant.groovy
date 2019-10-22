package smarthome.application

import smarthome.automation.House
import smarthome.core.SmartHomeCoreConstantes
import smarthome.security.User
import grails.validation.Validateable

/**
 * Résultats du défi pour un participant d'une équipe
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Validateable
class DefiEquipeParticipant extends AbstractDefiResultat {

	DefiEquipe defiEquipe
	User user

	// propriétés utilisateur
	House house

	
	static transients = ['house']
	
	static belongsTo = [defiEquipe: DefiEquipe, user: User]

	static constraints = {
		defiEquipe unique: 'user'
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.APPLICATION_SCHEMA
		defiEquipe index: 'DefiEquipeParticipant_Idx'
		user index: 'DefiEquipeParticipant_User_Idx'
	}
}
