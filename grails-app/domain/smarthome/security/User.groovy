package smarthome.security

import java.util.Set

import smarthome.core.SmartHomeCoreConstantes
import grails.validation.Validateable


/**
 * @see resources.groovy pour la personnalisation des renderer json et xml
 * 
 * @author gregory
 *
 */
@Validateable
class User implements Serializable {

	transient springSecurityService

	String username	// sert aussi d'email qui sera la clé unique
	String password
	String nom
	String prenom
	String applicationKey
	String telephoneMobile
	Profil profil

	Date lastActivation
	Date lastConnexion
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	boolean profilPublic
	boolean autorise_user_data
	boolean autorise_conso_data
	boolean autorise_share_data
	boolean engage_enedis_account
	

	// la liste des roles (utilisé pour le binding mais n'est pas mappé en base)
	def roles = []


	static hasMany = [friends: UserFriend]

	static transients = ['springSecurityService', 'roles']

	static constraints = {
		// ne pas déclarer la contrainte unique dans le mapping car hibernate fait des controles à chaque chargement
		// ce qui déclenche des multiples requetes par exemple get(id) ethibernate fait une seconde requete sur le username
		// !! garder la contrainte en base !!
		//username unique: true
		username blank: false, validator: SmartHomeSecurityUtils.emailValidator
		password blank: false, validator: SmartHomeSecurityUtils.passwordValidator
		nom blank: false
		prenom blank: false
		roles bindable: true
		telephoneMobile nullable: true
		lastConnexion nullable: true
		profil nullable: true
	}

	static mapping = {
		table name: 'utilisateur', schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA // conflit sur certaines bases avec "user"
		password column: '`password`'
		friends cascade: 'all-delete-orphan'
		username index: 'UserApplication_Username_Idx'
		autorise_user_data defaultValue: true
		autorise_conso_data defaultValue: true
		engage_enedis_account defaultValue: true
		autorise_share_data defaultValue: false
		profilPublic defaultValue: false
		sort 'nom'
	}


	static {
		grails.converters.JSON.registerObjectMarshaller(User) {
			//			it.properties.findAll {k,v ->
			//				!(k in ['password', 'friends'])
			//			}
			[id: it.id, username: it.username, nom: it.nom, prenom: it.prenom]
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


	boolean hasRole(String role) {
		this.getAuthorities().find {
			it.authority == role
		}
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

	String getInitiale() {
		String initiale = prenom[0].toUpperCase()
		String[] tokens = nom.split(" ")

		if (tokens.length > 1) {
			initiale += tokens[0][0].toUpperCase() + tokens[1][0].toUpperCase()
		} else if (tokens) {
			initiale += tokens[0][0].toUpperCase()
		}

		return initiale
	}
}
