package smarthome.security

import grails.plugin.springsecurity.annotation.Secured
import smarthome.core.AbstractController
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
class UserApplicationController extends AbstractController {

	UserApplicationService userApplicationService


	/**
	 * Liste des applications tierces
	 * 
	 * @return
	 */
	//@NavigableAction(label = "Applications", navigation = NavigationEnum.configuration, header = "Compte")
	def userApplications(UserApplicationCommand command) {
		command.user = authenticatedUser
		def userApplications = userApplicationService.search(command, this.getPagination([:]))
		def recordsTotal = userApplications.totalCount

		// userFriends est accessible depuis le model avec la variable userFriend[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond userApplications, model: [command: command, recordsTotal: recordsTotal, secUser: command.user]
	}


	/**
	 * Suppression d'une application
	 * 
	 * @param userApplication
	 * @return
	 */
	def delete(UserApplication userApplication) {
		userApplicationService.delete(userApplication)
		userApplications(new UserApplicationCommand())
	}
}
