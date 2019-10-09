package smarthome.application

import smarthome.application.granddefi.AccountCommand
import smarthome.automation.Chauffage
import smarthome.automation.ECS
import smarthome.common.Commune
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum
import smarthome.security.Profil
import grails.plugin.springsecurity.annotation.Secured


/**
 * Application Grand Défi
 * 
 * @author gregory
 *
 */
@Secured("hasAnyRole('ROLE_GRAND_DEFI', 'ROLE_ADMIN_GRAND_DEFI')")
class GrandDefiController extends AbstractController {

	GrandDefiService grandDefiService


	/**
	 * Affichage formulaire création d'un compte
	 * 
	 * @return
	 */
	@Secured("permitAll")
	def account() {
		def model = [:]
		model.communes = Commune.list()
		model.profils = Profil.list()
		model.chauffages = Chauffage.list()
		model.ecs = ECS.list()
		model.command = parseFlashCommand('command', new AccountCommand())

		render(view: 'account', model: model)
	}


	/**
	 * Création d'un compte Grand Défi
	 * 
	 * @param command
	 * @return
	 */
	@Secured("permitAll")
	@ExceptionNavigationHandler(actionName = "account", modelName = "command")
	def createAccount(AccountCommand command) {
		checkErrors(this, command)
		grandDefiService.createAccount(command)
		setInfo "Votre compte est créé. Veuillez consulter vos mails pour l'activer."
		forward(controller: 'login', action: 'auth')
	}


	/**
	 * Affichage des consommations élec et gaz
	 * @return
	 */
	@NavigableAction(label = "Mes consommations", navigation = NavigationEnum.navbarPrimary,
	header = "Grand Défi", icon = "bar-chart")
	def consommation() {
		def user = authenticatedUser
		def model = grandDefiService.modelConsommation(user)

		render(view: 'consommation', model: model)
	}


	/**
	 * Page "Mon Défi" avec les résultats du défi en individuel
	 * 
	 * @return
	 */
	@NavigableAction(label = "Mes défis", navigation = NavigationEnum.navbarPrimary,
	header = "Grand Défi", icon = "award")
	def mondefi(DefiCommand command) {
		command.user = authenticatedUser // spring security plugin
		def model = grandDefiService.modelMonDefi(command)
		render(view: 'mondefi', model: model)
	}
}
