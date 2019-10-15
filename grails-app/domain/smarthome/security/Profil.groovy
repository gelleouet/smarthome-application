package smarthome.security

import smarthome.core.SmartHomeCoreConstantes
import grails.validation.Validateable

@Validateable
class Profil {

	String libelle
	String view
	String icon


	static constraints = {
		libelle unique: true
		icon nullable: true
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		sort 'libelle'
	}
}
