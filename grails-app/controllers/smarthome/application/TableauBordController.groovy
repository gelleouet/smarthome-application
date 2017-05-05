package smarthome.application

import smarthome.automation.Device;
import smarthome.automation.DeviceEventService;
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
	DeviceEventService deviceEventService
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
		def lastEvents = deviceEventService.listLastByUser(user.id, 10, 7)
		def lastDevices = deviceService.listLastByUser(user.id, 10, 7)
		def userDeviceCount = deviceService.countDevice(user)
		def sharedDeviceCount = deviceService.listSharedDeviceId(user.id).size()
		def houseSynthese = houseService.calculSynthese(house)
		def modes = modeService.listModesByUser(user)
		def tableauBords = deviceService.groupByTableauBord(principal.id)
		
		render(view: 'tableauBord', model: [lastEvents: lastEvents, user: user, house: house,
			lastDevices: lastDevices, sharedDeviceCount: sharedDeviceCount, userDeviceCount: userDeviceCount,
			houseSynthese: houseSynthese, modes: modes, tableauBords: tableauBords])
	}
}
