package smarthome.application

import smarthome.automation.Device;
import smarthome.automation.DeviceEventService;
import smarthome.automation.DeviceService;
import smarthome.automation.HouseService;
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
	DeviceEventService deviceEventService
	UserService userService
	HouseService houseService
	
	/**
	 * 
	 * @return
	 */
	def index() {
		def user = authenticatedUser
		def house = houseService.calculDefaultConsoAnnuelle(user)
		def lastEvents = deviceEventService.listLastByUser(user.id, 10, 7)
		def lastDevices = deviceService.listLastByUser(user.id, 10, 7)
		def userDeviceCount = deviceService.countDevice(user)
		def sharedDeviceCount = deviceService.listSharedDeviceId(user.id).size()
		def houseSynthese = houseService.calculSynthese(house)
		
		render(view: 'tableauBord', model: [lastEvents: lastEvents, user: user, house: house,
			lastDevices: lastDevices, sharedDeviceCount: sharedDeviceCount, userDeviceCount: userDeviceCount,
			houseSynthese: houseSynthese])
	}
}
