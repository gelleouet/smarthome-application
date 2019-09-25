package smarthome.automation

import smarthome.application.granddefi.RegisterCompteurCommand
import smarthome.core.AbstractController
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum

import org.springframework.security.access.annotation.Secured

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Secured("isAuthenticated()")
class CompteurController extends AbstractController {

	CompteurService compteurService


	/**
	 * Affichage page config/résumé des compteurs
	 * 
	 * @return
	 */
	@NavigableAction(label = "Mes compteurs", navigation = NavigationEnum.navbarPrimary,
	header = "Grand Défi", icon = "tool")
	def compteur() {
		def user = authenticatedUser
		def model = compteurService.modelCompteur(user)

		render(view: 'compteur', model: model)
	}


	/**
	 * Enregistrement d'un compteur (elec, gaz, etc...)
	 *
	 * @param command
	 * @return
	 */
	def registerCompteur(RegisterCompteurCommand command) {
		command.user = authenticatedUser

		if (command.compteurType == 'elec') {
			if (command.compteurModel == 'Linky') {
				redirect(action: 'dataconnect', controller: 'dataConnect')
				return
			} else {
				compteurService.registerCompteurElec(command)
			}
		} else if (command.compteurType == 'gaz') {
			if (command.compteurModel == 'Gazpar') {
				redirect(action: 'adict', controller: 'adict')
				return
			} else {
				compteurService.registerCompteurGaz(command)
			}
		}

		redirect(action: 'compteur')
	}


	/**
	 * Reset config compteur elec
	 *
	 * @return
	 */
	def resetCompteurElec() {
		compteurService.resetCompteurElec(authenticatedUser)
		redirect(action: 'compteur')
	}


	/**
	 * Reset config compteur gaz
	 *
	 * @return
	 */
	def resetCompteurGaz() {
		compteurService.resetCompteurGaz(authenticatedUser)
		redirect(action: 'compteur')
	}
}
