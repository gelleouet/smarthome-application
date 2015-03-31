package smarthome.security

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.web.mapping.LinkGenerator;
import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.SmartHomeException;
import smarthome.security.RegistrationCode;
import smarthome.security.ResetPasswordCommand;
import smarthome.security.User;

class RegisterService extends AbstractService {
	
	LinkGenerator grailsLinkGenerator
	
	/**
	 * Demande d'oubli du mot de passe
	 * 
	 * @param username
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousMessage(routingKey = 'smarthome.security.resetUserPassword')
    def forgotPassword(String username) throws SmartHomeException {
		log.info "Demande reset mot de passe ${username}"
		
		// on vérifie que l'adresse est connue
		def user = User.findByUsername(username)
		
		if (!user) {
			throw new SmartHomeException("Adresse introuvable : $username !", username)
		}
		
		// création d'un code d'enregistrement
		def registrationCode = new RegistrationCode(username: username)
		registrationCode.serverUrl = grailsLinkGenerator.serverBaseURL + '/register/resetPassword?username=${username}&token=${registrationCode.token}'
		
		if (!registrationCode.save()) {
			throw new SmartHomeException("Erreur création d'un token d'activation !", username)
		}
		
		return registrationCode
    }
	
	
	/**
	 * Création d'un compte
	 *
	 * @param username
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousMessage()
	def createAccount(AccountCommand account) throws SmartHomeException {
		log.info "Demande création compte ${account.username}"
		
		// on vérifie que l'adresse n'est pas déjà prie
		def user = User.findByUsername(account.username)
		
		if (user) {
			throw new SmartHomeException("Un compte existe déjà avec cette adresse !", account)
		}
		
		// création d'un code d'enregistrement
		def registrationCode = new RegistrationCode(username: account.username)
		registrationCode.serverUrl = grailsLinkGenerator.serverBaseURL + '/register/confirmAccount?username=${account.username}&token=${registrationCode.token}'
		
		if (!registrationCode.save()) {
			throw new SmartHomeException("Erreur création d'un token d'activation !", account)
		}
		
		// création d'un user avec compte bloqué en attente déblocage
		user = new User(username: account.username, password: account.newPassword, prenom: account.prenom, 
			nom: account.nom, lastActivation: new Date(), accountLocked: true,
			applicationKey: UUID.randomUUID())
		
		if (!user.save()) {
			throw new SmartHomeException("Erreur création nouveau compte !", account)
		}
		
		return registrationCode
	}
	
	
	/**
	 * Réinitialisation d'un mot de passe via un token
	 * 
	 * @param command
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def resetPassword(ResetPasswordCommand command) {
		log.info "Reset password ${command.username}"
		
		// on vérifie que le token existe bien pour le user
		def registration = RegistrationCode.where( {
			token == command.token && username == command.username
		}).find()
		
		if (!registration) {
			throw new SmartHomeException("Token introuvable pour cet utilisateur !", command)
		}
		
		// on vérifie si le token n'a pas expiré, dans ce cas, il est supprimé directement
		if (registration.dateCreated < (new Date() - 1)) {
			registration.delete(flush: true) // on flush direct avant que la transaction ne soit rollbacké
			throw new SmartHomeException("Token expiré !", command)
		}
		
		def user = User.findByUsername(command.username)
		
		if (!user) {
			throw new SmartHomeException("Utilisateur introuvable : $command.username !", command)
		}
		
		// on peut changer de mot de passe et on met à jour les statuts relatifs au mot de passe
		// le mot de passe est encodé dans le setter password
		user.password = command.newPassword
		user.lastActivation = new Date()
		user.passwordExpired = false
		user.save()
		
		// le token est supprimé
		registration.delete()
	}
	
	
	/**
	 * Activation d'un compte
	 * 
	 * @param username
	 * @param token
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def confirmAccount(String username, String token) {
		log.info "Activation compte ${username}"
		
		// on vérifie que l'adresse n'est pas déjà prie
		def user = User.findByUsername(username)
		
		if (!user) {
			throw new SmartHomeException("Compte introuvable !", username)
		}
		
		// on vérifie que le token existe bien pour le user
		def registration = RegistrationCode.where( {
			token == token && username == user.username
		}).find()
		
		if (!registration) {
			throw new SmartHomeException("Token introuvable pour cet utilisateur !")
		}
		
		// activation du compte
		user.accountLocked = false
		user.save()
	}
}
