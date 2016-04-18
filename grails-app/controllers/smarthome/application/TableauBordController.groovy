package smarthome.application

import smarthome.automation.Device;
import smarthome.automation.DeviceService;
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
	
	/**
	 * 
	 * @return
	 */
	def index() {
		def user = authenticatedUser
		def filActualite = socialService.filActualite(user, this.getPagination([:]))
		def deviceCount = deviceService.countDevice(user)
		def sharedDeviceCount = deviceService.listSharedDeviceId(user.id).size()
		render(view: 'tableauBord', model: [filActualite: filActualite, user: user,
			deviceCount: deviceCount, sharedDeviceCount: sharedDeviceCount])
	}
}
