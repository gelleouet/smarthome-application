package smarthome.automation

import smarthome.application.granddefi.RegisterCompteurCommand
import smarthome.automation.deviceType.Compteur
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
	DeviceValueService deviceValueService


	/**
	 * Affichage page config/résumé des compteurs
	 * 
	 * @return
	 */
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
		} else if (command.compteurType == 'eau') {
			compteurService.registerCompteurEau(command)
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
	 * Reset config compteur eau
	 *
	 * @return
	 */
	def resetCompteurEau() {
		compteurService.resetCompteurEau(authenticatedUser)
		redirect(action: 'compteur')
	}


	/**
	 * Saisie des index d'un compteur (elec, gaz, eau, etc....)
	 * 
	 * @param device
	 * @return
	 */
	def saisieIndex(CompteurIndex command) {
		command = this.parseFlashCommand("command", command)

		if (!command) {
			command = new CompteurIndex()
		}
		
		// recharge du device systématique pour éviter erreur no-session
		// après handle erreur par exemple
		command.device = Device.read(params['device.id'] ? params.int('device.id') : command.device.id)
		
		if (!command.device) {
			throw new SmartHomeException("Device introuvable !")
		}

		// vérif autorisation
		deviceService.edit(command.device)
		def defaultCoefGaz = configService.value(CompteurGaz.CONFIG_DEFAULT_COEF_CONVERSION)
		
		render(view: 'saisieIndex', model: [command: command, defaultCoefGaz: defaultCoefGaz])
	}


	/**
	 * Enregistrement des index
	 * 
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "saisieIndex", modelName = "command")
	def saveIndex(CompteurIndex command) {
		bindFile(command, 'photo', 'photo')
		compteurService.saveIndexForValidation(command)
		setInfo "Index enregistré avec succès. Il sera validé ultérieurement par un administrateur."
		
		saisieIndex(new CompteurIndex(device: command.device))
	}


	/**
	 * Les index en cours de validation par un admin
	 * 
	 * @return
	 */
	@Secured("hasAnyRole('ROLE_VALIDATION_INDEX', 'ROLE_ADMIN')")
	def compteurIndexs(CompteurIndexCommand command) {
		// moteur de recherche en view scope
		command = parseViewCommand(command)
		
		command.admin(principal.id) // plugin spring security
		checkErrors(this, command)
		def indexs = compteurService.listCompteurIndex(command)

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
		
		// recharge du device systématique pour éviter erreur no-session
		// après handle erreur par exemple
		index.device = Device.read(index.device.id)
		
		def defaultCoefGaz = configService.value(CompteurGaz.CONFIG_DEFAULT_COEF_CONVERSION)
		
		(index.device.newDeviceImpl() as Compteur).prepareForEdition(index)
		
		render(view: 'compteurIndex', model: [command: index, 
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
	
	
	/**
	 * Charge les derniers index d'un compteur
	 * Prévu pour être appelé uniquement en Ajax
	 * 
	 * @param command
	 * @return
	 */
	def datatableIndex(DeviceValueCommand command) {
		Device device = Device.read(command.deviceId)

		if (!device) {
			throw new SmartHomeException("Device introuvable !")
		}

		// vérif autorisation
		deviceService.edit(device)
		
		// récupère les infos de pagination
		command.pagination = this.getPagination([:])
		def indexList = (device.newDeviceImpl() as Compteur).listIndex(command)
		
		render (template: 'datatableIndex', model: [indexList: indexList, command: command,
			recordsTotal: indexList.totalCount])
	}
}
