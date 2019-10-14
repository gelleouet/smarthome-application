package smarthome.application

import smarthome.core.SmartHomeCoreConstantes
import smarthome.security.Profil
import smarthome.security.User
import grails.validation.Validateable

/**
 * Résultats du défi par profil de participant
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Validateable
class DefiProfil extends AbstractDefiResultat {

	Defi defi
	Profil profil


	static belongsTo = [defi: Defi, profil: Profil]

	static constraints = {
		defi unique: 'profil'
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.APPLICATION_SCHEMA
		defi index: 'DefiProfil_Idx'
	}
}
