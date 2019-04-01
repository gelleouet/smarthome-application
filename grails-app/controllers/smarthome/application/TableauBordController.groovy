package smarthome.application

import smarthome.automation.HouseService;
import smarthome.core.AbstractController;
import smarthome.core.WidgetService;
import smarthome.security.User;
import smarthome.security.UserFriend;
import smarthome.security.UserFriendService;
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
	WidgetService widgetService
	HouseService houseService
	
	
	/**
	 * 
	 * @return
	 */
	def index() {
		def user = authenticatedUser
		def widgetUsers = widgetService.findAllByUserId(principal.id)
		render(view: 'tableauBord', model: [user: user, widgetUsers: widgetUsers, secUser: user])
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
