package smarthome.application

import smarthome.automation.HouseService;
import smarthome.core.AbstractController;
import grails.plugin.springsecurity.annotation.Secured


/**
 * Application Grand Défi
 * 
 * @author gregory
 *
 */
@Secured("isAuthenticated()")
class GrandDefiController extends AbstractController {

	HouseService houseService
	
	
	/**
	 * Affichage des consommations élec et gaz
	 * @return
	 */
	def consommation() {
		def user = authenticatedUser
		def house = houseService.findDefaultByUser(user)
		render(view: 'consommation', model: [secUser: user, house: house])
	}
}
