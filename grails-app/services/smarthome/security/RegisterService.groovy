package smarthome.security

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.springframework.transaction.annotation.Transactional

import smarthome.application.granddefi.RegisterCompteurCommand
import smarthome.automation.CompteurService
import smarthome.automation.House
import smarthome.automation.HouseService
import smarthome.core.AbstractService
import smarthome.core.AsynchronousWorkflow
import smarthome.core.SmartHomeException
import smarthome.security.RegistrationCode
import smarthome.security.ResetPasswordCommand
import smarthome.security.User

class RegisterService extends AbstractService {

	LinkGenerator grailsLinkGenerator
	UserService userService
	HouseService houseService
	CompteurService compteurService
	

	/**
	 * Demande d'oubli du mot de passe
	 * 
	 * @param username
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousWorkflow("registerService.forgotPassword")
	RegistrationCode forgotPassword(String username) throws SmartHomeException {
		log.info "Demande reset mot de passe ${username}"

		// on vérifie que l'adresse est connue
		def user = User.findByUsername(username)

		if (!user) {
			throw new SmartHomeException("Adresse incorrecte !", username)
		}

		// création d'un code d'enregistrement
		RegistrationCode registrationCode = new RegistrationCode(username: username)
		registrationCode.serverUrl = grailsLinkGenerator.link(controller: 'register', action: 'resetPassword',
		params: [username: username, token: registrationCode.token], absolute: true)

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
	@AsynchronousWorkflow("registerService.createAccount")
	RegistrationCode createAccount(AccountCommand account) throws SmartHomeException {
		log.info "Demande création compte ${account.username}"

		// on vérifie que l'adresse n'est pas déjà prie
		def user = User.findByUsername(account.username)

		if (user) {
			throw new SmartHomeException("Un compte existe déjà avec cette adresse !", account)
		}

		// création d'un code d'enregistrement
		RegistrationCode registrationCode = new RegistrationCode(username: account.username)
		registrationCode.serverUrl = grailsLinkGenerator.link(controller: 'register', action: 'confirmAccount',
		params: [username: account.username, token: registrationCode.token], absolute: true)

		if (!registrationCode.save()) {
			throw new SmartHomeException("Erreur création d'un token d'activation !", account)
		}

		// création d'un user avec compte bloqué en attente déblocage
		user = new User(username: account.username, password: account.newPassword, prenom: account.prenom,
			nom: account.nom, lastActivation: new Date(), accountLocked: true,
			applicationKey: UUID.randomUUID(), profilPublic: account.profilPublic,
			acceptUseData: account.acceptUseData, acceptPublishData: account.acceptPublishData)
		
		try {
			userService.save(user, false)
		} catch (SmartHomeException ex) {
			// rethrow l'erreur en spécifiant le bon command et les bonnes erreurs
			throw new SmartHomeException(ex.message, account, user)
		}
		
		try {
			// création ou récupération d'une maison par défaut
			House defaultHouse = houseService.bindDefault(user, [nbPersonne: account.nbPersonne,
				location: account.ville, adresse: account.adresse, codePostal: account.codePostal])
		} catch (SmartHomeException ex) {
			// rethrow l'erreur en spécifiant le bon command et les bonnes erreurs
			throw new SmartHomeException(ex.message, account)
		}
		
		// création automatique d'un compteur eau non connecté. Vu que l'application n'offre que cette possibilité
		// autant le faire de suite et ne pas le demander à l'utilisateur
		compteurService.registerCompteurEau(new RegisterCompteurCommand(user: user))
		
		return registrationCode
	}


	/**
	 * Réinitialisation d'un mot de passe via un token
	 * 
	 * @param command
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void resetPassword(ResetPasswordCommand command) throws SmartHomeException {
		log.info "Reset password ${command.username}"

		// on vérifie que le token existe bien pour le user
		def registration = RegistrationCode.where( {
			token == command.token && username == command.username
		}).find()

		if (!registration) {
			throw new SmartHomeException("Le lien n'est plus valide !", command)
		}

		// on vérifie si le token n'a pas expiré, dans ce cas, il est supprimé directement
		if (registration.dateCreated < (new Date() - 1)) {
			registration.delete(flush: true) // on flush direct avant que la transaction ne soit rollbacké
			throw new SmartHomeException("Le lien a expiré !", command)
		}

		def user = User.findByUsername(command.username)

		if (!user) {
			throw new SmartHomeException("Utilisateur introuvable !", command)
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
	void confirmAccount(String username, String token) throws SmartHomeException {
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
