package smarthome.automation


import grails.converters.JSON

import org.springframework.security.access.annotation.Secured

import smarthome.automation.notification.NotificationSender
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.QueryUtils
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum


@Secured("hasRole('ROLE_ADMIN')")
class NotificationAccountSenderController extends AbstractController {

	private static final String COMMAND_NAME = 'notificationAccountSender'

	NotificationAccountSenderService notificationAccountSenderService

	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	//@NavigableAction(label = "Connecteurs", navigation = NavigationEnum.configuration, header = "Système")
	def notificationAccountSenders(String notificationAccountSenderSearch) {
		def search = QueryUtils.decorateMatchAll(notificationAccountSenderSearch)

		def notificationAccountSenders = NotificationAccountSender.createCriteria().list(this.getPagination([:])) {
			if (notificationAccountSenderSearch) {
				ilike('libelle', search)
			}
		}
		def recordsTotal = notificationAccountSenders.totalCount

		// notificationAccountSenders est accessible depuis le model avec la variable notificationAccountSender[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond notificationAccountSenders, model: [recordsTotal: recordsTotal, notificationAccountSenderSearch: notificationAccountSenderSearch]
	}


	/**
	 * Edition
	 *
	 * @param notificationAccountSender
	 * @return
	 */
	def edit(NotificationAccountSender notificationAccountSender) {
		def editNotificationAccountSender = parseFlashCommand(COMMAND_NAME, notificationAccountSender)
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editNotificationAccountSender]))
	}


	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]
		// Compléter le model

		// on remplit avec les infos du user
		model << userModel

		return model
	}


	/**
	 * Enregistrement modification
	 *
	 * @param notificationAccountSender
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = NotificationAccountSenderController.COMMAND_NAME)
	def save(NotificationAccountSender notificationAccountSender) {
		checkErrors(this, notificationAccountSender)
		notificationAccountSenderService.save(notificationAccountSender)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Suppression
	 *
	 * @param notificationAccountSender
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "notificationAccountSenders", modelName = NotificationAccountSenderController.COMMAND_NAME)
	def delete(NotificationAccountSender notificationAccountSender) {
		notificationAccountSenderService.delete(notificationAccountSender)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Rendu
	 * 
	 * @param notificationAccount
	 * @return
	 */
	@Secured("isAuthenticated()")
	def formTemplateNotificationSender(NotificationAccount notificationAccount) {
		if (notificationAccount.notificationAccountSender) {
			NotificationSender sender = notificationAccount.notificationAccountSender.newNotificationSender()
			notificationAccount.configToJson()
			render(template: "impl/${sender.simpleName}/form", model: [notificationAccount: notificationAccount])
		} else {
			nop()
		}
	}


	/**
	 * 
	 * @param notification
	 * @return
	 */
	@Secured("isAuthenticated()")
	def formSenderParameter(Notification notification) {
		if (notification.notificationAccount) {
			NotificationSender sender = notification.notificationAccount.notificationAccountSender.newNotificationSender()
			notification.parametersToJson()
			render(template: "impl/${sender.simpleName}/formParameter", model: [notification: notification])
		} else {
			nop()
		}
	}
}
