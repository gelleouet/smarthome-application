package smarthome.automation

import org.springframework.security.access.annotation.Secured;
import smarthome.core.AbstractController;
import smarthome.security.User;


@Secured("isAuthenticated()")
class EnergieController extends AbstractController {

	DeviceService deviceService
	HouseService houseService
	
	
	/**
	 * Affichage du watmetre pour le compteur
	 * 
	 * @param device
	 * @return
	 */
	def watmetre(Device device) {
		deviceService.edit(device)
		render(view: '/deviceType/teleInformation/watmetre', model: [device: device])	
	}
	
	
	/**
	 * Widget compteur par défaut
	 * 
	 * @return
	 */
	def widget() {
		def user = authenticatedUser
		def house = houseService.findDefaultByUser(user)
		render(template: '/deviceType/teleInformation/widget', model: [house: house])
	}
}
