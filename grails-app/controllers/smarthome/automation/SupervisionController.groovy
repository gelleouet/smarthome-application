package smarthome.automation

import grails.plugin.springsecurity.annotation.Secured
import smarthome.application.DefiCommand
import smarthome.application.DefiService
import smarthome.automation.export.ExportTypeEnum;
import smarthome.common.Commune
import smarthome.core.AbstractController;
import smarthome.core.ConfigService
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.ExportService
import smarthome.security.Profil
import smarthome.security.UserService;


@Secured("hasRole('ROLE_SUPERVISION')")
class SupervisionController extends AbstractController {
	UserService userService
	DeviceService deviceService
	DeviceTypeService deviceTypeService
	ExportService exportService
	DefiService defiService
	ConfigService configService
	
	
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
			communes: Commune.list(), profils: Profil.list(), compteurTypes: deviceTypeService.listCompteur(),
			command: command, defis: defiService.listCatalogue(new DefiCommand(), [:]),
			defaultDefiId: configService.value("GRAND_DEFI_ID")])
	}
	
	
	/**
	 * Dialog paramètres export
	 * 
	 * @param command
	 * @return
	 */
	def dialogExport(SupervisionCommand command) {
		Map exportImpls = [(smarthome.automation.export.UserExcelDeviceValueExport.name): 'Export profils']
		render (template: 'dialogExport', model: [command: command, exportImpls: exportImpls])
	}
	
	
	/**
	 * Export des données pour un profil admin
	 *
	 */
	@ExceptionNavigationHandler(actionName = "supervision", modelName = "command")
	def export(SupervisionCommand command) {
		command.adminId = principal.id
		exportService.export(command, response)
	}
}
