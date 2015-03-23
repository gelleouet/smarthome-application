package smarthome.security

import grails.validation.Validateable;

@Validateable
class RoleGroup {

	String name
	
	// une liste de id roles (sert pour le binding mais n'est pas mappé en base) 
	def roles = []

	static transients = ['roles']
	
	static mapping = {
		cache true
		sort 'name'
	}

	Set<Role> getAuthorities() {
		RoleGroupRole.findAllByRoleGroup(this).collect { it.role }
	}

	static constraints = {
		name blank: false, unique: true
		roles bindable: true
	}
}
