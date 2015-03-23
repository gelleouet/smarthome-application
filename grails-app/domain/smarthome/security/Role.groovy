package smarthome.security

import grails.validation.Validateable;

@Validateable
class Role {

	static final String ROLE_PREFIX = "ROLE_"
	
	String authority
	String workflowAcl
	boolean acl = false

	static mapping = {
		cache true
		sort 'authority'
	}

	static constraints = {
		authority blank: false, unique: true
		workflowAcl nullable: true
	}
}
