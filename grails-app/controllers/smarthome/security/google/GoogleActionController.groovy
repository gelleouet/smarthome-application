package smarthome.security.google

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import smarthome.security.User;
import smarthome.security.UserApplication;
import smarthome.security.UserService;
import smarthome.security.google.GoogleActionService;
import smarthome.security.google.GoogleActionAuthCommand;
import smarthome.security.google.action.GoogleActionRequest;
import grails.converters.JSON;
import grails.plugin.springsecurity.SpringSecurityUtils;
import grails.plugin.springsecurity.annotation.Secured;
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler


/**
 * Google Actions Controller
 * 
 * @author gregory
 *
 */
@Secured("permitAll()")
class GoogleActionController extends AbstractController {

	def springSecurityService
	GoogleActionService googleActionService
	AuthenticationManager authenticationManager


	/**
	 * Auth page
	 * La demande est faite systématiquement pour éviter toute tentative d'intrusion 
	 */
	def auth(GoogleActionAuthCommand command) {
		render view: '/login/google/action/oauth', model: [command: command,
			linkApp: grailsApplication.config.google.action.appName]
	}
	
	
	/**
	 * Authentification pour redirection google action
	 * 
	 * @param command
	 * @return
	 */
	def authenticate(GoogleActionAuthCommand command) {
		def model = [command: command]
		
		try {
			Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(command.j_username, command.j_password))
			
			if (authenticate.isAuthenticated()) {
				UserApplication userApp = googleActionService.auth(command, User.read(authenticate.principal.id))
				redirect url: command.redirectUrl(userApp.publicToken)
				return
			} else {
				command.error = "L'authentification a échoué !"
			}
		} catch (AuthenticationException ex) {
			command.error = "L'authentification a échoué ! [$ex.message]"
		}
		
		render status: HttpServletResponse.SC_FORBIDDEN, view: '/login/google/action/oauth', model: [command: command,
			linkApp: grailsApplication.config.google.action.appName]
	}
	
	
	/**
	 * Action pour la conversion
	 * 
	 * @return
	 */
	def conversation() {
		try {
			def gactionResponse = googleActionService.conversation(new GoogleActionRequest(request.JSON),
				request.getHeader('Authorization'))
			render gactionResponse as JSON
			return
		} catch (Exception ex) {
			log.error("Google Action Conversation : ${ex.message}")
			render(status: HttpServletResponse.SC_FORBIDDEN, text: ex.message)
		}
	}
}
