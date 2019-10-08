package smarthome.api

import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.ExceptionHandler

import grails.converters.JSON
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.SmartHomeException
import smarthome.security.User

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Secured("isAuthenticated()")
class AdictController extends AbstractController {

	AdictService adictService


	/**
	 * Consentement ADICT
	 *
	 * @return
	 */
	def adict() {
		def account = adictService.notificationAccount(authenticatedUser)
		def newCommand = new AdictAccreditation(account?.jsonConfig ?: [:])
		def accreditation = parseFlashCommand("accreditation", newCommand)
		render(view: '/compteur/adict', model: [accreditation: accreditation])
	}

	/**
	 * 
	 * @param accreditation
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "adict", modelName = "accreditation")
	def accreditation(AdictAccreditation accreditation) {
		checkErrors(this, accreditation)
		User user = authenticatedUser // spring security plugin

		try {
			adictService.accreditation(user, accreditation)
		} catch (SmartHomeException ex) {
			throw new SmartHomeException(ex.message, accreditation)
		}

		setInfo("Votre consentement sur le service GRDF ADICT est validé avec succès !")
		forward action: 'compteur', controller: 'compteur'
	}


	/**
	 * API consommationInformative
	 * 
	 * @return
	 */
	def consommationInformative() {
		User user = authenticatedUser // spring security plugin
		def datapoints = adictService.consommationInformative(user)

		render(contentType: "application/json") {
			datapoints
		}
	}

}
