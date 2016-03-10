package smarthome.security

import grails.validation.Validateable;

@Validateable
class Role {

	static final String ROLE_PREFIX = "ROLE_"
	
	String authority

	static mapping = {
		sort 'authority'
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
