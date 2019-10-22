package smarthome.security

import smarthome.core.SmartHomeCoreConstantes
import grails.validation.Validateable

@Validateable
class Role {

	static final String ROLE_PREFIX = "ROLE_"

	static final String ROLE_ADMIN = ROLE_PREFIX + "ADMIN"
	static final String ROLE_GRAND_DEFI = ROLE_PREFIX + "GRAND_DEFI"


	String authority

	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		sort 'authority'
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
