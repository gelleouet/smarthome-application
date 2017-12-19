package smarthome.application

import smarthome.automation.Device;
import smarthome.automation.DeviceService;
import smarthome.automation.Mode;
import smarthome.automation.HouseService;
import smarthome.automation.ModeService;
import smarthome.core.AbstractController;
import smarthome.security.User;
import smarthome.security.UserFriend;
import smarthome.security.UserFriendService;
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

	UserFriendService userFriendService
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
		def modes = modeService.listModesByUser(user)
		def tableauBords = deviceService.groupByTableauBord(principal.id)
		
		render(view: 'tableauBord', model: [user: user, house: house,
			modes: modes, tableauBords: tableauBords, secUser: user])
	}
	
	
	/**
	 * Tableau de bord publique d'un ami
	 * 
	 * @param friend
	 * @return
	 */
	def tableauBordFriend(User friend) {
		def user = authenticatedUser
		userFriendService.assertFriend(user, friend)
		
		//  on s'assure que tous les nouveax objets sont bien associés
		def house = houseService.findDefaultByUser(friend)
		houseService.shareHouse(house, user)
		
		render(view: 'tableauBordFriend', model: [user: friend, house: house, viewOnly: true,
			secUser: user])
	}
}
