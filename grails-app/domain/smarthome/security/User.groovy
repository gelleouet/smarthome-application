package smarthome.security

import java.util.Set;

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
	String applicationKey
	String telephoneMobile
	
	Date lastActivation
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	
	
	// la liste des roles (utilisé pour le binding mais n'est pas mappé en base)
	def roles = []

	static transients = ['springSecurityService', 'roles']

	static constraints = {
		username blank: false, unique: true, email: true
		password blank: false, validator: SmartHomeSecurityUtils.passwordValidator
		nom blank: false
		prenom blank: false
		roles bindable: true
		telephoneMobile nullable: true
	}

	static mapping = {
		table name: 'utilisateur' // conflit sur certaines bases avec "user"
		password column: '`password`'
		sort 'nom'
	}
	
	
	static {
		grails.converters.JSON.registerObjectMarshaller(User) {
			it.properties.findAll {k,v -> 
				k != 'password'
			}
		}
	}
	

	Set<Role> getAuthorities() {
		def roles = UserRole.createCriteria().list {
			eq 'user', this
			join 'role'
		}
		
		return roles.collect { it.role }
		//UserRole.findAllByPersonne(this).collect { it.role }
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
	
	String getPrenomNom() {
		return "$prenom $nom"
	}
	
	String getNomPrenom() {
		return "$nom $prenom"
	}
}
