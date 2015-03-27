package smarthome.security

import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService
import smarthome.core.AsynchronousMessage;
import smarthome.core.SmartHomeException
import smarthome.security.ChangePasswordCommand;
import smarthome.security.RoleGroup;
import smarthome.security.User;
import smarthome.security.UserRoleGroup;


/**
 * 
 * @author gregory
 *
 */
class UserService extends AbstractService {

	def springSecurityService
	def passwordEncoder



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
		if (! user.id) {
			user.lastActivation = new Date()
			user.passwordExpired = true
			user.password = UUID.randomUUID() // création d'un password fictif en attente du déblocage par l'utilisateur
		}


		if (!user.save()) {
			throw new SmartHomeException("Erreur enregistrement utilisateur", user)
		}
		
		// enregistrement des groupes : on efface tout et on remet les nouveaux
		if (saveRole) {
			UserRoleGroup.removeAll(user)
			
			if (user.groups) {
				user.groups.each {
					def roleGroup = new RoleGroup()
					roleGroup.id = it.toInteger()
					UserRoleGroup.create(user, roleGroup)
				}
			}
			
			// si modification des roles, il faut rafraichir ses nouvelles permissions
			springSecurityService.reauthenticate(user.username)
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
}
