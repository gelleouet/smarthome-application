package smarthome.automation

import smarthome.application.granddefi.RegisterCompteurCommand
import smarthome.automation.deviceType.CompteurGaz
import smarthome.core.AbstractController
import smarthome.core.ConfigService
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.SmartHomeException
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum
import smarthome.security.Profil

import org.springframework.security.access.annotation.Secured

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
@Secured("isAuthenticated()")
class CompteurController extends AbstractController {

	CompteurService compteurService
	ConfigService configService
	DeviceService deviceService
	DeviceTypeService deviceTypeService


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
	@ExceptionNavigationHandler(actionName = "compteur")
	def registerCompteur(RegisterCompteurCommand command) {
		command.user = authenticatedUser // plugin spring security

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


	/**
	 * Saisie des index d'un compteur (elec, gaz, eau, etc....)
	 * 
	 * @param device
	 * @return
	 */
	def saisieIndex(SaisieIndexCommand command) {
		command = this.parseFlashCommand("command", command)
		def device = Device.read(command.deviceId)

		if (!device) {
			throw new SmartHomeException("Device introuvable !")
		}

		// vérif autorisation
		deviceService.edit(device)
		def defaultCoefGaz = configService.value(CompteurGaz.CONFIG_DEFAULT_COEF_CONVERSION)

		render(view: 'saisieIndex', model: [command: command, device: device,
			defaultCoefGaz: defaultCoefGaz])
	}


	/**
	 * Enregistrement des index
	 * 
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "saisieIndex", modelName = "command")
	def saveIndex(SaisieIndexCommand command) {
		bindFile(command, 'photo', 'photo')
		checkErrors(this, command)
		compteurService.saveIndexForValidation(command)
		setInfo "Index enregistré avec succès. Il sera validé ultérieurement par un administrateur."
		forward(action: 'compteur')
	}


	/**
	 * Les index en cours de validation par un admin
	 * 
	 * @return
	 */
	@Secured("hasAnyRole('ROLE_VALIDATION_INDEX', 'ROLE_ADMIN')")
	@NavigableAction(label = "Validation des index", navigation = NavigationEnum.navbarPrimary,
	header = "Grand Défi", icon = "check-square")
	def compteurIndexs(CompteurIndexCommand command) {
		command.admin(authenticatedUser) // plugin spring security
		checkErrors(this, command)
		def indexs = compteurService.listCompteurIndex(command, getPagination([:]))

		render(view: 'compteurIndexs', model: [command: command, indexs: indexs,
			profils: Profil.list(), compteurTypes: deviceTypeService.listCompteur()])
	}


	/**
	 * Affichage d'un index par un admin
	 * 
	 * @param index
	 * @return
	 */
	@Secured("hasAnyRole('ROLE_VALIDATION_INDEX', 'ROLE_ADMIN')")
	def compteurIndex(CompteurIndex index) {
		index = this.parseFlashCommand('index', index)
		def defaultCoefGaz = configService.value(CompteurGaz.CONFIG_DEFAULT_COEF_CONVERSION)
		render(view: 'compteurIndex', model: [command: index, device: index.device,
			modeAdmin: true, defaultCoefGaz: defaultCoefGaz])
	}


	/**
	 * Validation d'un index par un admin
	 * 
	 * @param index
	 * @return
	 */
	@Secured("hasAnyRole('ROLE_VALIDATION_INDEX', 'ROLE_ADMIN')")
	@ExceptionNavigationHandler(actionName = "compteurIndex", modelName = "index")
	def validIndex(CompteurIndex compteurIndex) {
		checkErrors(this, compteurIndex)
		compteurService.validIndex(compteurIndex)
		redirect(action: 'compteurIndexs')
	}


	/**
	 * Affichage photo prise du compteur
	 * 
	 * @param index
	 * @return
	 */
	def compteurIndexImg(CompteurIndex index) {
		if (index.photo) {
			render(file: index.photo, contentType: 'image/png')
		} else {
			nop()
		}
	}


	/**
	 * Suppression d'un index
	 * 
	 * @param index
	 * @return
	 */
	def deleteIndex(CompteurIndex index) {
		compteurService.delete(index)
		redirect(action: 'compteurIndexs')
	}
}
