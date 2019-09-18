package smarthome.security

import smarthome.core.SmartHomeCoreConstantes
import grails.validation.Validateable

@Validateable
class Profil {

	String libelle
	String view


	static constraints = {
		libelle unique: true
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		sort 'libelle'
	}
}
