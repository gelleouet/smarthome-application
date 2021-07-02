package smarthome.automation

import grails.plugin.springsecurity.annotation.Secured
import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.ExportService
import smarthome.security.User
import smarthome.security.UserService;


@Secured("hasRole('ROLE_SUPERVISION')")
class SupervisionController extends AbstractController {
	UserService userService
	DeviceService deviceService
	ExportService exportService
	
	
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
	 * Dialog préparation export admin
	 * 
	 * @param command
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "supervision", modelName = "command")
	def dialogExport(DeviceSearchCommand command) {
		User user = authenticatedUser
		command.adminId = user.id
		// charge les impls export du user connecté
		def exportImpls = user.exports*.exportImpl
		// calcul des metavalue pour sélection par user
		def metavalueNames = deviceService.distinctMetavalueNames(command)
		
		render(template: 'dialogExport', model: [command: command, exportImpls: exportImpls,
			metavalueNames: metavalueNames])
	}
	
	
	/**
	 * Export des données pour un profil admin
	 *
	 */
	@ExceptionNavigationHandler(actionName = "supervision", modelName = "command")
	def exportAdmin(ExportCommand command) {
		User user = authenticatedUser
		command.adminId = user.id
		
		exportService.export(command, response)
	}
}
