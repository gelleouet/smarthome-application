package smarthome.security

import smarthome.security.UserService;
import grails.plugin.springsecurity.annotation.Secured;
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum
import smarthome.security.ChangePasswordCommand;
import smarthome.security.User;
/**
 * Controller gestion utilisateur
 * 
 * @author gregory
 *
 */
@Secured("isAuthenticated()")
class PasswordController extends AbstractController {

	UserService userService


	/**
	 * Demande changement mot de passe
	 * 
	 * @return
	 */
	@NavigableAction(label = "Mot de passe", navigation = NavigationEnum.configuration, header = "Compte")
	def password() {
		// plugin spring security add authenticatedUser property
		def command = parseFlashCommand("command", new ChangePasswordCommand(username: authenticatedUser.username,
		prenom: authenticatedUser.prenom, nom: authenticatedUser.nom))
		render(view: 'password', model: [command: command])
	}


	/**
	 * Confirmation changement mot de passe
	 * 
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "password", modelName = "command")
	def changePassword(ChangePasswordCommand command) {
		checkErrors(this, command)

		userService.changePassword(command)
		setInfo "Changement de votre mot de passe effectué avec succès"
		forward(action: 'password')
	}
}
