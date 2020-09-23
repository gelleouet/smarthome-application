package smarthome.application

import smarthome.application.granddefi.AccountCommand
import smarthome.automation.Chauffage
import smarthome.automation.ECS
import smarthome.automation.House
import smarthome.automation.HouseService
import smarthome.common.Commune
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.SmartHomeException
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum
import smarthome.security.Profil
import smarthome.security.User
import grails.plugin.springsecurity.annotation.Secured
import smarthome.automation.CompteurIndexCommand


/**
 * Application Grand Défi
 * 
 * @author gregory
 *
 */
@Secured("isAuthenticated()")
class GrandDefiController extends AbstractController {

	GrandDefiService grandDefiService
	HouseService houseService


	/**
	 * Accueil GDE
	 * 
	 * @return
	 */
	def index() {
		def user = authenticatedUser
		House house = houseService.findDefaultByUser(user)

		if (house?.compteur) {
			redirect(action: 'consommationElec')
		} else if (house?.compteurGaz) {
			redirect(action: 'consommationGaz')
		} else if (house?.compteurEau) {
			redirect(action: 'consommationEau')
		} else {
			redirect(controller: 'compteur', action: 'compteur')
		}
	}


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

		// sélection du profil par défaut
		if (!model.command.profil) {
			model.command.profil = model.profils.find { it.id == 1L }
		}

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
	 * Affichage page config/résumé des compteurs
	 *
	 * @return
	 */
	@NavigableAction(label = "Mes compteurs", navigation = NavigationEnum.navbarPrimary, icon = "tool")
	def compteur() {
		redirect(controller: 'compteur', action: 'compteur')
	}


	/**
	 * Les index en cours de validation par un admin
	 *
	 * @return
	 */
	@Secured("hasAnyRole('ROLE_VALIDATION_INDEX', 'ROLE_ADMIN')")
	@NavigableAction(label = "Validation des index", navigation = NavigationEnum.navbarPrimary,
		icon = "check-square")
	def compteurIndexs(CompteurIndexCommand command) {
		redirect(controller: 'compteur', action: 'compteurIndexs')
	}


	/**
	 * Affichage des consommations
	 * @return
	 */
	@NavigableAction(label = "Electricité", navigation = NavigationEnum.navbarPrimary,
		header = "Mes consommations", icon = "zap", iconHeader = "bar-chart")
	def consommationElec() {
		def user = authenticatedUser
		def model = grandDefiService.modelConsommation(user, DefiCompteurEnum.electricite)

		render(view: 'consommationElec', model: model)
	}
	
	
	/**
	 * Affichage des consommations
	 * @return
	 */
	@NavigableAction(label = "Gaz", navigation = NavigationEnum.navbarPrimary,
			header = "Mes consommations", icon = "fire", iconLib = "awesome", iconHeader = "bar-chart")
	def consommationGaz() {
		def user = authenticatedUser
		def model = grandDefiService.modelConsommation(user, DefiCompteurEnum.gaz)
		
		render(view: 'consommationGaz', model: model)
	}
	
	
	/**
	 * Affichage des consommations
	 * @return
	 */
	@NavigableAction(label = "Eau", navigation = NavigationEnum.navbarPrimary,
			header = "Mes consommations", icon = "droplet", iconHeader = "bar-chart")
	def consommationEau() {
		def user = authenticatedUser
		def model = grandDefiService.modelConsommation(user, DefiCompteurEnum.eau)
		
		render(view: 'consommationEau', model: model)
	}


	/**
	 * Point d'entrée des défis
	 * 
	 * @return
	 */
	@NavigableAction(label = "Mes défis", navigation = NavigationEnum.navbarPrimary,
		iconHeader = "award", customMenuView = true)
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


	/**
	 * Les participants du GDE
	 * 
	 * @param command
	 * @return
	 */
	def participants(DefiCommand command) {
		command.user = authenticatedUser // spring security plugin
		def model = grandDefiService.modelParticipants(command)
		model.viewName = "participants"
		render(view: 'participants', model: model)
	}
	
	
	/**
	 * Inscription à un défi
	 * 
	 * @param defiId
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "catalogue", controllerName = "defi")
	def inscription(long defiId) {
		User user = authenticatedUser // spring security plugin
		DefiEquipeParticipant defiUser = grandDefiService.inscriptionDefi(user, defiId, [:])
		Defi defi = defiUser.defiEquipe.defi
		
		setInfo "Félicitations : vous êtes inscrit au défi ${defi.libelle} !"
		mesresultats(new DefiCommand(defi: defi))
	}
	
	
	/**
	 * Désinscription du défi
	 * 
	 * @param defiId
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "catalogue", controllerName = "defi")
	def desinscription(long defiId) {
		User user = authenticatedUser // spring security plugin

		try {
			grandDefiService.desinscriptionDefi(user, defiId)
			setInfo "Vous êtes désinscrit du défi !"
			forward controller: "defi", action: "catalogue"
		} catch (SmartHomeException e) {
			// redirige vers le défi en cours
			setError e.message
			Defi defi = Defi.read(defiId)
			mesresultats(new DefiCommand(defi: defi))
		}		
		
	}
}
