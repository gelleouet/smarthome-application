package smarthome.automation

import grails.plugin.springsecurity.annotation.Secured
import smarthome.automation.export.ExportTypeEnum;
import smarthome.common.Commune
import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.security.Profil
import smarthome.security.UserService;


@Secured("hasRole('ROLE_SUPERVISION')")
class SupervisionController extends AbstractController {
	UserService userService
	DeviceValueService deviceValueService
	DeviceService deviceService
	DeviceTypeService deviceTypeService
	
	
	/**
	 * Tableau de bord pour les profils type admin de plusieurs utilisateurs
	 * Affiche les devices de chaque utilisateur
	 * avec des états de contrôles (batterie, graphe, etc)
	 *
	 * @param command
	 *
	 * @return
	 */
	def supervision(SupervisionCommand command) {
		// moteur de recherche en view scope
		command = parseViewCommand(command)
		command.admin(principal.id) // plugin spring security
		checkErrors(this, command)
		
		def devices = deviceService.listSupervision(command)
		// ne pas oublier de réinjecter la pagination car géré par le command
		setPagination(command.pagination())
		
		render(view: 'supervision', model: [devices: devices, recordsTotal: devices.totalCount,
			communes: Commune.list(), profils: Profil.list(), compteurTypes: deviceTypeService.listCompteur(), command: command])
	}
	
	
	/**
	 * Dialog paramètres export
	 * 
	 * @param command
	 * @return
	 */
	def dialogExportAdmin(SupervisionCommand command) {
		render (template: 'dialogExportAdmin', model: [command: command])
	}
	
	
	/**
	 * Export des données pour un profil admin
	 *
	 */
	@ExceptionNavigationHandler(actionName = "supervision", modelName = "command")
	def exportAdmin(SupervisionCommand command) {
		command.adminId = principal.id
		deviceValueService.export(command, ExportTypeEnum.admin, response)
	}
}
