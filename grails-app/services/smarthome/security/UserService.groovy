package smarthome.security

import org.springframework.transaction.annotation.Transactional

import smarthome.core.AbstractService
import smarthome.core.AsynchronousMessage
import smarthome.core.QueryUtils
import smarthome.core.SmartHomeException
import smarthome.security.ChangePasswordCommand
import smarthome.security.User


/**
 * 
 * @author gregory
 *
 */
class UserService extends AbstractService {

	def passwordEncoder



	/**
	 * Recherche multi-critère
	 * 
	 * @param command
	 * @param pagination
	 * 
	 * @return
	 * @throws SmartHomeException
	 */
	List<User> search(UserCommand command, Map pagination) throws SmartHomeException {
		return User.createCriteria().list(pagination) {
			if (command.search) {
				def search = QueryUtils.decorateMatchAll(command.search)

				or {
					ilike 'nom', search
					ilike 'prenom', search
					ilike 'username', search
				}
			}

			if (command.profilPublic != null) {
				eq 'profilPublic', command.profilPublic
			}

			if (command.notInIds) {
				not {
					'in' 'id', command.notInIds
				}
			}

			order "prenom"
			order "nom"
		}
	}


	/**
	 * Authentification d'une application
	 * 
	 * @param userName
	 * @param applicationId
	 * @return
	 */
	User authenticateApplication(String userName, String applicationId) throws SmartHomeException {
		if (!userName || !applicationId) {
			throw new SmartHomeException("Invalid username or applicationId !")
		}

		// recherche user
		User user = User.findByUsername(userName)

		if (!user) {
			throw new SmartHomeException("Invalid username !")
		}

		if (user.applicationKey != applicationId) {
			throw new SmartHomeException("Invalid applicationId !")
		}

		return user
	}


	/**
	 * Enregistrement du profil. 
	 * Détecte si changement de username (ie email) pour envoyer un mail de confirmation.
	 * 
	 * @param user
	 * @param saveRole
	 * 
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def save(User user, boolean saveRole) throws SmartHomeException {
		log.info "Enregistrement utilisateur ${user.username}"

		// cas spécial si création
		// mise à jour des champs que l'utilisateur ne peut pas modifier
		if (! user.id && !user.password) {
			user.lastActivation = new Date()
			user.passwordExpired = true
			user.password = UUID.randomUUID() // création d'un password fictif en attente du déblocage par l'utilisateur
		}


		if (!user.save()) {
			throw new SmartHomeException("Erreur enregistrement utilisateur", user)
		}

		// enregistrement des groupes : on efface tout et on remet les nouveaux
		if (saveRole) {
			UserRole.removeAll(user)

			if (user.roles) {
				user.roles.each {
					UserRole.create(user, Role.read(it.toLong()))
				}
			}
		}

		return user
	}


	/**
	 * Changement de mot de passe
	 * Effectue les contrôles nécessaires : mot de passe différent, ancien mot de passe correct
	 * 
	 * @param command
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousMessage(routingKey = 'smarthome.security.resetUserPassword')
	def changePassword(ChangePasswordCommand command) throws SmartHomeException {
		log.info "Changement mot de passe user ${command.username}"

		def user = User.findByUsername(command.username)

		if (!user) {
			throw new SmartHomeException("L'utilisateur $command.username n'existe pas !", command)
		}

		// test du mot de passe d'origine
		if (!passwordEncoder.isPasswordValid(user.password, command.oldPassword, null)) {
			throw new SmartHomeException("Le mot de passe actuel ne correspond pas !", command)
		}

		// les validators sont censés être passés donc à ce niveau, c'est ok pour enregistrer le nouveau mot de passe
		user.password = command.newPassword

		if (!user.save()) {
			throw new SmartHomeException("Erreur changement mot de passe", command)
		}

		return user
	}


	/**
	 * L'utilisateur est-il un administrateur de plusieurs utilisateurs
	 * 
	 * @param user
	 * @return
	 */
	boolean isAdminUsers(User user) {
		return UserAdmin.findByAdmin(user)
	}


	/**
	 * Liste les users d'un admin
	 *  
	 * @param admin
	 * @return
	 */
	List listByAdmin(User admin) {
		return UserAdmin.executeQuery("""SELECT user FROM UserAdmin userAdmin
			JOIN userAdmin.user user
			WHERE userAdmin.admin.id = :adminId
			ORDER BY user.prenom, user.nom""", [adminId: admin.id])
	}
}
