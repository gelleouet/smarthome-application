package smarthome.automation

import org.springframework.security.access.annotation.Secured;

import smarthome.core.AbstractController;
import smarthome.security.User;


@Secured("isAuthenticated()")
class ModeController extends AbstractController {

	ModeService modeService
	
	
	/**
	 * Suppression d'un mode par son index
	 * 
	 * @param command
	 * @return
	 */
	def deleteMode(ModeCommand command, int status) {
		modeService.deleteMode(command, status)
		render(template: 'modes', model: [modes: command.modes])
	}
	
	
	/**
	 * Ajout d'un mode
	 * 
	 * @param command
	 * @return
	 */
	def addMode(ModeCommand command) {
		modeService.addMode(command)
		render(template: 'modes', model: [modes: command.modes])
	}
	
	
	/**
	 * Edition d'une maison
	 * 
	 * @param house
	 * @return
	 */
	def templateEditByUser() {
		def user = params.user
		def modes = modeService.listModesByUser(user)
		render(template: 'modes', model: [user: user, modes: modes])
	}
}
