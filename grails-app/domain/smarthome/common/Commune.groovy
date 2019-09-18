package smarthome.common

import smarthome.core.SmartHomeCoreConstantes
import grails.validation.Validateable

@Validateable
class Commune {

	String libelle
	String codePostal


	static constraints = {
		libelle unique: true
		codePostal unique: true
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		codePostal length: 8
		sort 'libelle'
	}
}
