package smarthome.application

import smarthome.core.SmartHomeCoreConstantes
import smarthome.security.Profil
import smarthome.security.User
import grails.validation.Validateable

/**
 * Résultats du défi par profil d'une équipe
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Validateable
class DefiEquipeProfil extends AbstractDefiResultat {

	DefiEquipe defiEquipe
	Profil profil


	static belongsTo = [defiEquipe: DefiEquipe, profil: Profil]

	static constraints = {
		defiEquipe unique: 'profil'
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.APPLICATION_SCHEMA
		defiEquipe index: 'DefiEquipeProfil_Idx'
	}
}
