package smarthome.automation

import org.springframework.security.access.annotation.Secured;

import smarthome.core.AbstractController;


@Secured("isAuthenticated()")
class HouseController extends AbstractController {

	HouseService houseService
	
	
	/**
	 * Suppression d'un mode par son index
	 * 
	 * @param command
	 * @return
	 */
	def deleteMode(HouseCommand command, int status) {
		houseService.deleteMode(command, status)
		render(template: 'modes', model: [modes: command.modes])
	}
	
	
	/**
	 * Ajout d'un mode
	 * 
	 * @param command
	 * @return
	 */
	def addMode(HouseCommand command) {
		houseService.addMode(command)
		render(template: 'modes', model: [modes: command.modes])
	}
}
