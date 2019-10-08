package smarthome.application

import smarthome.core.SmartHomeCoreConstantes
import grails.validation.Validateable

/**
 * Une équipe spécifique à un défi
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Validateable
class DefiEquipe implements Serializable {

	Defi defi
	String libelle
	Double reference_elec
	Double reference_gaz
	Double action_elec
	Double action_gaz
	Set participants = []


	static hasMany = [participants: DefiParticipant]

	static belongsTo = [defi: Defi]

	static constraints = {
		libelle unique: 'defi'
		reference_elec nullable: true
		reference_gaz nullable: true
		action_elec nullable: true
		action_gaz nullable: true
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.APPLICATION_SCHEMA
		defi index: 'DefiEquipe_Idx'
	}
}
