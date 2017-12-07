package smarthome.security.aws

import org.springframework.security.authentication.AuthenticationManager;

import grails.plugin.springsecurity.annotation.Secured;
import smarthome.core.AbstractController;


/**
 * Amazon Alexa Controller
 * 
 * @author Gregory
 *
 */
@Secured("permitAll()")
class AmazonAlexaController extends AbstractController {
	AuthenticationManager authenticationManager
	
	/**
	 * Auth page
	 * La demande est faite systématiquement pour éviter toute tentative d'intrusion
	 */
	def auth(AmazonAlexaAuthCommand command) {
		render view: '/login/aws/alexa/oauth', model: [command: command,
			linkApp: grailsApplication.config.aws.alexa.appName]
	}
}
