package smarthome.security

import grails.plugin.springsecurity.annotation.Secured;
import smarthome.automation.HouseService;
import smarthome.core.AbstractController
import smarthome.core.DateUtils;
import smarthome.core.ExceptionNavigationHandler
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum


/**
 * Controller gestion utilisateur
 * 
 * @author gregory
 *
 */
@Secured("isAuthenticated()")
class UserFriendController extends AbstractController {

	UserFriendService userFriendService
	UserService userService
	HouseService houseService


	/**
	 * Liste des amis
	 * 
	 * @return
	 */
	//@NavigableAction(label = "Mes amis", navigation = NavigationEnum.navbarPrimary)
	def userFriendFollowing(UserFriendCommand command) {
		def user = authenticatedUser
		command.userId = principal.id

		// Tous les amis
		command.confirm = true
		def friends = userFriendService.searchFollowing(command, this.getPagination([max:16]))	
		def recordsTotal = friends.totalCount
		def houses = houseService.listDefaultByUsers(friends*.friend, ['user', 'chauffage'])
		def consos = houseService.listLastConsoByHouses(houses)
		
		def friendsAndMe = [user]
		friendsAndMe.addAll(friends*.friend)
		
		def dateConso = DateUtils.firstDayInYear(new Date())
		def top5 = houseService.sortHouseConso(friendsAndMe, dateConso, "asc", 5)
		def flop5 = houseService.sortHouseConso(friendsAndMe, dateConso, "desc", 5)
		
		def repartitionChauffage = houseService.repartitionChauffage(friendsAndMe)
		
		// userFriends est accessible depuis le model avec la variable userFriend[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond friends, model: [command: command, recordsTotal: recordsTotal, user: user,
			houses: houses, consos: consos, secUser: user, top5: top5, flop5: flop5,
			repartitionChauffage: repartitionChauffage]
	}
	
	
	/**
	 * Recherche d'amis
	 * 
	 * @param command
	 * @return
	 */
	def userFriendList(UserFriendCommand command) {
		command.userId = principal.id
		command.confirm = true
		def friends = userFriendService.searchFollowing(command, this.getPagination([:]))
		def recordsTotal = friends.totalCount
		def users = friends*.friend
		
		// userFriends est accessible depuis le model avec la variable userFriend[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond users, model: [command: command, recordsTotal: recordsTotal]
	}
	
	
	/**
	 * Template abonnés en attente
	 * 
	 * @return
	 */
	def userFriendWaitingFollowing() {
		UserFriendCommand command = new UserFriendCommand(userId: principal.id, confirm: false)
		
		def followings = userFriendService.searchFollowing(command, [:])
		def followingHouses = houseService.listDefaultByUsers(followings*.friend, ['user', 'chauffage'])
		def followingConsos = houseService.listLastConsoByHouses(followingHouses)
		
		render template: 'userFriendWaitingFollowing', model: [followings: followings,
			followingHouses: followingHouses, followingConsos: followingConsos]
	}
	
	
	/**
	 * Template abonnements à confirmer
	 * 
	 * @return
	 */
	def userFriendConfirmFollower() {
		UserFriendCommand command = new UserFriendCommand(userId: principal.id, confirm: false)
		
		def followers = userFriendService.searchFollower(command, [:])
		def followerHouses = houseService.listDefaultByUsers(followers*.user, ['user', 'chauffage'])
		def followerConsos = houseService.listLastConsoByHouses(followerHouses)
		
		render template: 'userFriendConfirmFollower', model: [followers: followers,
			followerHouses: followerHouses, followerConsos: followerConsos]
	}
	
	
	/**
	 * Liste communauté
	 * 
	 * @param command
	 * @return
	 */
	def userFriendSearch(UserCommand command) {
		command.profilPublic = true
		command.notInIds = [principal.id]
		def users = userService.search(command, this.getPagination([max:16]))
		def recordsTotal = users.totalCount
		
		def houses = houseService.listDefaultByUsers(users, ['user', 'chauffage'])
		def consos = houseService.listLastConsoByHouses(houses)
		
		// users est accessible depuis le model avec la variable user[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond users, model: [command: command, recordsTotal: recordsTotal,
			houses: houses, consos: consos]
	}
	
	
	/**
	 * Demande invitation
	 * 
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "userFriendSearch", modelName = "")
	def inviteFriend(User friend) {
		userFriendService.inviteFriend(authenticatedUser, friend)
		redirect action: 'userFriendFollowing'
	}
	
	
	/**
	 * Suppression ami
	 * 
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "userFriendFollowing", modelName = "")
	def deleteFriend(UserFriend userFriend) {
		userFriendService.deleteFriend(authenticatedUser, userFriend)
		redirect action: 'userFriendFollowing'
	}
	
	
	/**
	 * Annulation d'une invitation
	 * 
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "userFriendFollowing", modelName = "")
	def cancelFriend(UserFriend userFriend) {
		userFriendService.cancelFriend(authenticatedUser, userFriend)
		redirect action: 'userFriendFollowing'
	}
	
	
	/**
	 * Annulation d'une invitation
	 * 
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "userFriendFollowing", modelName = "")
	def confirmFriend(UserFriend userFriend) {
		userFriendService.confirmFriend(authenticatedUser, userFriend)
		redirect action: 'userFriendFollowing'
	}
}
