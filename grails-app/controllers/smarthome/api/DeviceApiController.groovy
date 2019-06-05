package smarthome.api

import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.RequestMapping

import grails.converters.JSON
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler


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
	@ExceptionNavigationHandler(json = true)
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
	@ExceptionNavigationHandler(json = true)
	def fetch(FetchCommand command) {
		checkErrors(this, command)

		def result = deviceApiService.fetch(command, request.getHeader('Authorization'))

		render(contentType: "application/json") {
			result
		}
	}
}
