
package smarthome.automation

import grails.converters.JSON

import org.springframework.security.access.annotation.Secured
import smarthome.automation.notification.NotificationSender
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.SmartHomeException
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum
import smarthome.security.User



@Secured("isAuthenticated()")
class NotificationAccountController extends AbstractController {

	private static final String COMMAND_NAME = 'notificationAccount'

	NotificationAccountService notificationAccountService



	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Services", navigation = NavigationEnum.configuration, header = "Compte")
	def notificationAccounts(NotificationAccountCommand command) {
		command.user = authenticatedUser
		def notificationAccounts = notificationAccountService.search(command, this.getPagination([:]))
		def recordsTotal = notificationAccounts.totalCount

		// devices est accessible depuis le model avec la variable device[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond notificationAccounts, model: [recordsTotal: recordsTotal, command: command]
	}


	/**
	 * Edition
	 *
	 * @param device
	 * @return
	 */
	def edit(NotificationAccount notificationAccount) {
		def editObject = parseFlashCommand(COMMAND_NAME, notificationAccount)
		editObject = notificationAccountService.edit(editObject)
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editObject]))
	}


	/**
	 * Création
	 *
	 * @return
	 */
	def create() {
		def editObject = parseFlashCommand(COMMAND_NAME, new NotificationAccount())
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editObject]))
	}


	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]
		// Compléter le model
		model.notificationSenders = NotificationAccountSender.list()
		// on remplit avec les infos du user
		model << userModel
		return model
	}


	/**
	 * Enregistrement d'un nouveau
	 *
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = "notificationAccount")
	def save(NotificationAccount notificationAccount) {
		notificationAccount.user = authenticatedUser
		// la config est bindée en map, faut la transformer
		notificationAccount.configFromJson()
		notificationAccount.validate() // important car les erreurs sont traitées lors du binding donc le device.user sort en erreur
		checkErrors(this, notificationAccount)
		notificationAccountService.save(notificationAccount)
		redirect(action: 'notificationAccounts')
	}


	/**
	 * Exécute une action sur un device
	 *
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "notificationAccounts", modelName = "")
	def delete(NotificationAccount notificationAccount) {
		notificationAccountService.delete(notificationAccount)
		redirect(action: 'notificationAccounts')
	}


	/**
	 * Exécution
	 *
	 * @param notification
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "notificationAccounts", modelName = "")
	def execute(NotificationAccount notificationAccount) {
		notificationAccountService.execute(notificationAccount)
		redirect(action: 'notificationAccounts')
	}
}
