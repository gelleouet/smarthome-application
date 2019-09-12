package smarthome.application

import smarthome.api.DataConnectService;
import smarthome.application.granddefi.RegisterCompteurCommand;
import smarthome.automation.DeviceService;
import smarthome.automation.HouseService;
import smarthome.automation.NotificationAccountSenderService;
import smarthome.automation.NotificationAccountService;
import smarthome.core.AbstractController;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;
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
	 * Affichage page config/résumé des compteurs
	 * 
	 * @return
	 */
	@NavigableAction(label = "Mes compteurs", navigation = NavigationEnum.navbarPrimary,
		header = "Grand Défi", icon = "tool")
	def compteur() {
		def user = authenticatedUser
		def model = grandDefiService.modelCompteur(user)
		
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
				redirect(action: 'dataconnect')
				return
			} else {
				grandDefiService.registerCompteurElec(command)
			}
		}
		
		redirect(action: 'compteur')
	}
	
	
	/**
	 * Consentement DataConnect
	 * 
	 * @return
	 */
	def dataconnect() {
		render(view: 'dataconnect')
	}
}
