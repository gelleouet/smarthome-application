package smarthome.application

import smarthome.automation.Device;
import smarthome.automation.DeviceService;
import smarthome.automation.HouseService;
import smarthome.core.AbstractController;
import smarthome.security.UserFriend;
import smarthome.security.UserService;
import smarthome.social.SocialService;
import grails.plugin.springsecurity.annotation.Secured


/**
 * Tableau de bord central
 * 
 * @author gregory
 *
 */
@Secured("isAuthenticated()")
class TableauBordController extends AbstractController {

	SocialService socialService
	DeviceService deviceService
	UserService userService
	HouseService houseService
	
	/**
	 * 
	 * @return
	 */
	def index() {
		def user = authenticatedUser
		def filActualite = socialService.filActualite(user, this.getPagination([:]))
		def house = houseService.calculDefaultConsoAnnuelle(user)

		render(view: 'tableauBord', model: [filActualite: filActualite, user: user, house: house])
	}
}
