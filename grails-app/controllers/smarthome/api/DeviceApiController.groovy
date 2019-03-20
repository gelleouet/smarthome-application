package smarthome.api

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;

import smarthome.core.AbstractController;


/**
 * API device
 * 
 * @author gregory.elleouet@gmail.com<Grégory Elléoouet>
 *
 */
@Secured("permitAll()")
class DeviceApiController extends AbstractController {

	DeviceApiService deviceApiService
	
	
	/**
	 * Envoi de données sur le serveur
	 * 
	 * @return
	 */
    def push(PushCommand command) { 
		checkErrors(this, command)
		deviceApiService.push(command, request.getHeader('Authorization'))
		nop()
	}
	
	
	/**
	 * Récupération de données depuis le serveur
	 * 
	 * @return
	 */
	def fetch() {
		
	}
}
