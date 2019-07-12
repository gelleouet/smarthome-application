package smarthome.api

import org.springframework.security.access.annotation.Secured

import grails.converters.JSON
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.security.User

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Secured("isAuthenticated()")
class DataConnectController extends AbstractController {

	DataConnectService dataConnectService


	/**
	 * URL de redirection après validation du consentement pour récupérer les
	 * params code et usage_point_id
	 * 
	 * @return
	 */
	@Secured("permitAll()")
	@ExceptionNavigationHandler(controllerName = "notificationAccount", actionName = "notificationAccounts")
	def redirect(String code, String usage_point_id) {
		User user = authenticatedUser // spring security plugin
		dataConnectService.authorization_code(user, code, usage_point_id)
		setInfo("Votre consentement sur le service Enedis DataConnect est validé avec succès !")
		forward(controller: 'notificationAccount', action: 'notificationAccounts')
	}


	/**
	 * Demande consentement depuis service
	 * 
	 * 
	 * @return
	 */
	def authorize() {
		redirect(url: dataConnectService.authorize_uri())
	}


	/**
	 *
	 * @return
	 */
	@ExceptionNavigationHandler(controllerName = "notificationAccount", actionName = "notificationAccounts")
	def refresh_token() {
		User user = authenticatedUser // spring security plugin
		def tokens = dataConnectService.refresh_token(user)
		render(contentType: "application/json") {
			tokens
		}
	}


	/**
	 * 
	 * @return
	 */
	@ExceptionNavigationHandler(controllerName = "notificationAccount", actionName = "notificationAccounts")
	def consumption_load_curve() {
		User user = authenticatedUser // spring security plugin
		def datapoints = dataConnectService.consumptionLoadCurve(user)
		render(contentType: "application/json") {
			datapoints
		}
	}


	/**
	 * 
	 * @return
	 */
	@ExceptionNavigationHandler(controllerName = "notificationAccount", actionName = "notificationAccounts")
	def daily_consumption() {
		User user = authenticatedUser // spring security plugin
		def datapoints = dataConnectService.dailyConsumption(user)
		render(contentType: "application/json") {
			datapoints
		}
	}


	/**
	 *
	 * @return
	 */
	@ExceptionNavigationHandler(controllerName = "notificationAccount", actionName = "notificationAccounts")
	def consumption_max_power() {
		User user = authenticatedUser // spring security plugin
		def datapoints = dataConnectService.consumptionMaxPower(user)
		render(contentType: "application/json") {
			datapoints
		}
	}
}
