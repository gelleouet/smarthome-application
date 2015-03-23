package smarthome.security

import grails.plugin.springsecurity.SpringSecurityUtils;
import grails.plugin.springsecurity.annotation.Secured;
import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.security.ResetPasswordCommand;

/**
 * Action de demande de nouveau mot de passe, activation d'un compte, etc.
 * Toutes les actions sont en mode non connecté
 * 
 * @author gregory
 *
 */
@Secured("permitAll")
class RegisterController extends AbstractController {

	def registerService
	
	/**
	 * Oubli du mot de passe et demande d'un nouveau par l'utilisateur (non admin)
	 * 
	 * @return
	 */
    def forgotPassword() {
		def username = parseFlashCommand('username', '')
		[username: username]
	}
	
	
	/**
	 * Formulaire création nouveau compte
	 * 
	 * @return
	 */
	def account() {
		def account = parseFlashCommand('command', new AccountCommand())
		render(view: 'account', model: [command: account])
	}
	
	
	/**
	 * Création d'un nouveau compte
	 * 
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "account", modelName = "command")
	def createAccount(AccountCommand account) {
		checkErrors(this, account)
		registerService.createAccount(account)
		flash.info = "Votre demande a bien été prise en compte. Veuillez consulter vos mails pour activer votre compte."
		redirect(controller: 'login', action: 'auth')	
	}
	
	
	/**
	 * Confirmation de la demande d'oubli du mot de passe
	 * 
	 * @param username
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "forgotPassword", modelName = "username")
	def confirmForgotPassword(String username) {
		registerService.forgotPassword(username)
		flash.info = "Un email pour réinitialiser votre mot de passe vous a été envoyé à l'adresse suivante : $username !"
		redirect(controller: 'login', action: 'auth')	
	}
	
	
	
	/**
	 * Formulaire pour réinitialiser son mot de passe. Le token et le username
	 * sont envoyés dans les paramètres de la requete.
	 * 
	 * @return
	 */
	def resetPassword() {
		def command = parseFlashCommand('command', new ResetPasswordCommand(username: params.username, token: params.token))
		[command: command]
	}
	
	
	/**
	 * Changement du mot de passe et connexion automatique à l'application
	 *
	 * @param username
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "resetPassword", modelName = "command")
	def confirmResetPassword(ResetPasswordCommand command) {
		checkErrors(this, command)
		registerService.resetPassword(command)
		flash.info = "Votre mot de passe a été réinitialisé !"
		redirect(controller: 'login', action: 'auth')	
	}
	
	
	/**
	 * Activation d'un compte
	 * 
	 * @param username
	 * @param token
	 * @return
	 */
	def confirmAccount(String username, String token) {
		try {
			registerService.confirmAccount(username, token)
			flash.info = 'Votre compte est activé !'
		} catch (Exception e) {
			flash.error = "Erreur lors de l'activation de votre compte: ${e.message}"
		}
		redirect(controller: 'login', action: 'auth')
	}
}
