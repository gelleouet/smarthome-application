package smarthome.application

import smarthome.automation.Device;
import smarthome.automation.DeviceSearchCommand;
import smarthome.automation.DeviceService;
import smarthome.automation.DeviceType;
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
		
		if (userService.isAdminUsers(user)) {
			tableauBordAdmin(new DeviceSearchCommand())
		} else {
			def house = houseService.findDefaultByUser(user)
			def modes = modeService.listModesByUser(user)
			def tableauBords = deviceService.groupByTableauBord(principal.id)
			
			render(view: 'tableauBord', model: [user: user, house: house,
				modes: modes, tableauBords: tableauBords, secUser: user])
		}
		
	}
	
	
	/**
	 * Tableau de bord pour les profils type admin de plusieurs utilisateurs
	 * Affiche les devices de chaque utilisateur
	 * avec des états de contrôles (batterie, graphe, etc)
	 * 
	 * @param command
	 * 
	 * @return
	 */
	def tableauBordAdmin(DeviceSearchCommand command) {
		def user = authenticatedUser
		command.adminId = user.id
		command.pagination = this.getPagination([:])
		
		def devices = deviceService.listByAdmin(command)
		def tableauBords = deviceService.groupByTableauBord(principal.id)
		def users = userService.listByAdmin(user)
		def deviceImpls = DeviceType.list()
		
		render(view: 'tableauBordAdmin', model: [user: user, secUser: user,
			devices: devices, tableauBords: tableauBords, recordsTotal: devices.totalCount,
			users: users, deviceImpls: deviceImpls, command: command])
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
