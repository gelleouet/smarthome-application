package smarthome.application

import smarthome.automation.Device;
import smarthome.automation.DeviceService;
import smarthome.automation.Mode;
import smarthome.automation.HouseService;
import smarthome.automation.ModeService;
import smarthome.core.AbstractController;
import smarthome.security.UserFriend;
import smarthome.security.UserService;
import grails.plugin.springsecurity.annotation.Secured


/**
 * Tableau de bord central
 * 
 * @author gregory
 *
 */
@Secured("isAuthenticated()")
class TableauBordController extends AbstractController {

	DeviceService deviceService
	UserService userService
	HouseService houseService
	ModeService modeService
	
	/**
	 * 
	 * @return
	 */
	def index() {
		def user = authenticatedUser
		def house = houseService.findDefaultByUser(user)
		def userDeviceCount = deviceService.countDevice(user)
		def sharedDeviceCount = deviceService.listSharedDeviceId(user.id).size()
		def modes = modeService.listModesByUser(user)
		def tableauBords = deviceService.groupByTableauBord(principal.id)
		
		render(view: 'tableauBord', model: [user: user, house: house,
			sharedDeviceCount: sharedDeviceCount, userDeviceCount: userDeviceCount,
			modes: modes, tableauBords: tableauBords])
	}
}
