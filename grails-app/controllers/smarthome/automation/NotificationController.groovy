package smarthome.automation

import grails.converters.JSON

import org.springframework.security.access.annotation.Secured

import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.QueryUtils
import smarthome.core.SmartHomeException
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum


@Secured("isAuthenticated()")
class NotificationController extends AbstractController {

	private static final String COMMAND_NAME = 'notification'

	NotificationService notificationService
	NotificationAccountService notificationAccountService


	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Notifications", navigation = NavigationEnum.configuration, header = "Compte")
	def notifications(NotificationCommand command) {
		command.user = authenticatedUser
		def notifications = notificationService.search(command, this.getPagination([:]))
		def recordsTotal = notifications.totalCount

		// notifications est accessible depuis le model avec la variable notification[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond notifications, model: [recordsTotal: recordsTotal, command: command]
	}


	/**
	 * Edition
	 *
	 * @param notification
	 * @return
	 */
	def edit(Notification notification) {
		def editNotification = parseFlashCommand(COMMAND_NAME, notification)
		if (editNotification?.id) {
			notificationService.edit(editNotification)
		}
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editNotification]))
	}


	/**
	 * Exécution pour test
	 * 
	 * @param notification
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "notifications", modelName = "")
	def executeTest(Notification notification) {
		notificationService.edit(notification)
		notificationService.executeTest(notification)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]

		// Compléter le model
		model.notificationAccounts = notificationAccountService.listForUser(authenticatedUser)

		// on remplit avec les infos du user
		model << userModel

		return model
	}


	/**
	 * Enregistrement modification
	 *
	 * @param notification
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = NotificationController.COMMAND_NAME)
	def save(Notification notification) {
		notification.user = authenticatedUser
		// la config est bindée en map, faut la transformer
		notification.parametersFromJson()
		notification.validate()
		checkErrors(this, notification)
		notificationService.save(notification)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Suppression
	 *
	 * @param notification
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "notifications", modelName = "")
	def delete(Notification notification) {
		notificationService.delete(notification)
		redirect(action: COMMAND_NAME + 's')
	}
}
