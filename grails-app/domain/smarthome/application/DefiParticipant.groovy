package smarthome.application

import smarthome.core.SmartHomeCoreConstantes
import smarthome.security.User
import grails.validation.Validateable

/**
 * Un participant spécifique à un défi et une équipe
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Validateable
class DefiParticipant implements Serializable {

	DefiEquipe defiEquipe
	User user
	Double reference_elec
	Double reference_gaz
	Double action_elec
	Double action_gaz


	static belongsTo = [defiEquipe: DefiEquipe, user: User]

	static constraints = {
		defiEquipe unique: 'user'
		reference_elec nullable: true
		reference_gaz nullable: true
		action_elec nullable: true
		action_gaz nullable: true
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.APPLICATION_SCHEMA
		defiEquipe index: 'DefiParticipant_Idx'
		user index: 'DefiParticipant_User_Idx'
	}
}
