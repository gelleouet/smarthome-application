package smarthome.application

import smarthome.core.SmartHomeCoreConstantes
import grails.validation.Validateable

/**
 * Résultats du défi pour une équipe
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Validateable
class DefiEquipe extends AbstractDefiResultat {

	Defi defi
	String libelle
	Set participants = []
	Set profils = []


	static hasMany = [participants: DefiEquipeParticipant, profils: DefiEquipeProfil]

	static belongsTo = [defi: Defi]

	static constraints = {
		libelle unique: 'defi'
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.APPLICATION_SCHEMA
		defi index: 'DefiEquipe_Idx'
	}
}
