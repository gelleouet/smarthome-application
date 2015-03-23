package smarthome.security

import grails.validation.Validateable


/**
 * @see resources.groovy pour la personnalisation des renderer json et xml
 * 
 * @author gregory
 *
 */
@Validateable
class User {

	transient springSecurityService

	String username	// sert aussi d'email qui sera la clé unique
	String password
	String nom
	String prenom
	Date lastActivation
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	String applicationKey
	
	// la liste des id groups (utilisé pour le binding mais n'est pas mappé en base)
	def groups = []

	static transients = ['springSecurityService', 'groups']

	static constraints = {
		username blank: false, unique: true, email: true
		password blank: false, validator: SmartHomeSecurityUtils.passwordValidator
		nom blank: false
		prenom blank: false
		groups bindable: true
	}

	static mapping = {
		table name: 'utilisateur' // conflit sur certaines bases avec "user"
		password column: '`password`'
		sort 'nom'
		cache: true
	}
	
	
	static {
		grails.converters.JSON.registerObjectMarshaller(User) {
			it.properties.findAll {k,v -> 
				k != 'password'
			}
		}
	}
	

	Set<RoleGroup> getAuthorities() {
		UserRoleGroup.findAllByUser(this).collect { it.roleGroup }
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}
}
