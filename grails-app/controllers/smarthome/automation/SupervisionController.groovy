package smarthome.automation

import grails.plugin.springsecurity.annotation.Secured
import smarthome.automation.export.ExportTypeEnum;
import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.security.UserService;


@Secured("hasRole('ROLE_SUPERVISION')")
class SupervisionController extends AbstractController {
	UserService userService
	DeviceValueService deviceValueService
	DeviceService deviceService
	
	
	/**
	 * Tableau de bord pour les profils type admin de plusieurs utilisateurs
	 * Affiche les devices de chaque utilisateur
	 * avec des états de contrôles (batterie, graphe, etc)
	 *
	 * @param command
	 *
	 * @return
	 */
	def supervision(DeviceSearchCommand command) {
		def user = authenticatedUser
		command.adminId = user.id
		command.pagination = this.getPagination([:])
		
		def devices = deviceService.listByAdmin(command)
		def users = userService.listByAdmin(user)
		def deviceImpls = DeviceType.list()
		
		render(view: 'supervision', model: [user: user, secUser: user,
			devices: devices, recordsTotal: devices.totalCount,
			users: users, deviceImpls: deviceImpls, command: command])
	}
	
	
	/**
	 * Export des données pour un profil admin
	 *
	 */
	@ExceptionNavigationHandler(actionName = "supervision", modelName = "command")
	def exportAdmin(ExportCommand command) {
		def user = authenticatedUser
		command.adminId = user.id
		deviceValueService.export(command, ExportTypeEnum.admin, response)
	}
}
