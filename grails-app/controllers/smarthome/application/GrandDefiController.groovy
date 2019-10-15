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
	 * Point d'entrée des défis
	 * 
	 * @return
	 */
	@NavigableAction(label = "Mes défis", navigation = NavigationEnum.navbarPrimary,
	header = "Grand Défi", icon = "award")
	def defis(DefiCommand command) {
		mesresultats(command)
	}


	/**
	 * Résultats individuels
	 * 
	 * @param command
	 * @return
	 */
	def mesresultats(DefiCommand command) {
		command.user = authenticatedUser // spring security plugin
		def model = grandDefiService.modelMesResultats(command)
		model.viewName = "mesresultats"
		render(view: 'mesresultats', model: model)
	}


	/**
	 * Résultats de l'équipe
	 *
	 * @param command
	 * @return
	 */
	def resultatsequipe(DefiCommand command) {
		command.user = authenticatedUser // spring security plugin
		def model = grandDefiService.modelResultatsEquipe(command)
		model.viewName = "resultatsequipe"
		render(view: 'resultatsequipe', model: model)
	}


	/**
	 * Résultats du défi
	 *
	 * @param command
	 * @return
	 */
	def resultatsdefi(DefiCommand command) {
		command.user = authenticatedUser // spring security plugin
		def model = grandDefiService.modelResultatsDefi(command)
		model.viewName = "resultatsdefi"
		render(view: 'resultatsdefi', model: model)
	}
}
