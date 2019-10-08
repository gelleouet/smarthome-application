package smarthome.api

import grails.validation.Validateable

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Validateable
class AdictAccreditation {
	String pce
	String role
	String codePostal
	String titulaireType
	String titulaireValeur
	String emailTitulaire
	String last_consommationInformative


	static constraints = {
		last_consommationInformative nullable: true
	}
}
