package smarthome.security

import smarthome.security.UserService
import grails.plugin.springsecurity.annotation.Secured
import smarthome.automation.DeviceService
import smarthome.automation.HouseService
import smarthome.automation.ModeService
import smarthome.common.Commune
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum
import smarthome.security.User
/**
 * Controller gestion utilisateur
 * 
 * @author gregory
 *
 */
@Secured("isAuthenticated()")
class ProfilController extends AbstractController {

	UserService userService
	HouseService houseService
	DeviceService deviceService
	ModeService modeService


	/**
	 * Affichage du profil simple (pour la modifcation du user par lui-même)
	 * 
	 * @return
	 */
	@NavigableAction(label = "Profil", navigation = NavigationEnum.configuration,
	defaultGroup = true, header = "Compte", icon = "user")
	def profil() {
		// plugin spring security add authenticatedUser property
		def user = parseFlashCommand("user", authenticatedUser)
		def house = houseService.findDefaultByUser(user)
		
		render(view: 'profil', model: [user: user, profils: Profil.list(),
			communes: Commune.list(), house: house])
	}


	/**
	 * Widget profil
	 * 
	 * @return
	 */
	def widgetProfil() {
		// plugin spring security add authenticatedUser property
		def user = authenticatedUser
		def house = houseService.findDefaultByUser(user)
		render(template: 'widgetProfil', model: [user: user, house: house])
	}



	/**
	 * Dialog profil publique d'un user
	 * 
	 * @param user
	 * @return
	 */
	def dialogProfilPublic(User user) {
		def house = houseService.findDefaultByUser(user)
		def userDeviceCount = deviceService.countDevice(user)
		def sharedDeviceCount = deviceService.listSharedDeviceId(user.id).size()
		render(template: 'dialogProfilPublic', model: [user: user, house: house, userDeviceCount: userDeviceCount,
			sharedDeviceCount: sharedDeviceCount, viewOnly: true])
	}


	/**
	 * Enregistrement du profil
	 * 
	 * @param profil
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "profil", modelName = "user")
	def saveProfil(ProfilCommand command) {
		checkErrors(this, command.user)
//		boolean syncCoords = command.house.isDirty('location')

		command.house.user = command.user
		houseService.save(command.house)

//		if (syncCoords) {
//			houseService.asyncGeocode(command.house)
//		}

		// on ne mappe que les infos "non sensibles" (ie pas le mot de passe)
		// @see constraint bindable User
		userService.save(command.user, false)

//		modeService.saveModes(command.modes, command.user)

		redirect(action: 'profil')
	}


	/**
	 * Ajax render profil form
	 * 
	 * @param profil
	 * @return
	 */
	@Secured("permitAll()")
	def formProfil(Profil profil) {
		render(template: "/profil/${ profil.view }")
	}
}
