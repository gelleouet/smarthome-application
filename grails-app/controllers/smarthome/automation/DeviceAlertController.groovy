package smarthome.automation

import org.springframework.security.access.annotation.Secured;

import smarthome.core.AbstractController;


@Secured("isAuthenticated()")
class DeviceAlertController extends AbstractController {

    private static final String COMMAND_NAME = 'deviceAlert'
	
	DeviceAlertService deviceAlertService
	DeviceService deviceService
	
	
	
	/**
	 * Recherche multi-critère des alertes
	 * 
	 * @param command
	 * @return
	 */
	def deviceAlerts(DeviceAlertCommand command) {
		command.userId = principal.id
		def tableauBords = deviceService.groupByTableauBord(principal.id)
		def alerts = deviceAlertService.search(command, this.getPagination([:]))
		def recordsTotal = alerts.totalCount
		
		// alerts est accessible depuis le model avec la variable deviceAlert[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond alerts, model: [recordsTotal: recordsTotal, command: command, tableauBords: tableauBords]
    }
	
	
	/**
	 * Ajout d'une config alerte
	 * 
	 * @param device
	 * @return
	 */
	def addLevelAlert(Device device) {
		deviceAlertService.addLevelAlert(device)
		render(template: 'deviceLevelAlertTable', model: [levelAlerts: device.levelAlerts])
	}
	
	
	/**
	 * Suppression config alerte
	 * 
	 * @param device
	 * @param status
	 * @return
	 */
	def deleteLevelAlert(Device device, int status) {
		deviceAlertService.deleteLevelAlert(device, status)
		render(template: 'deviceLevelAlertTable', model: [levelAlerts: device.levelAlerts])
	}
	
	
	/**
	 * Gestion de la marque "viewed"
	 * 
	 * @param alert
	 * @return
	 */
	def markViewed(DeviceAlert alert) {
		deviceAlertService.markViewed(alert)
		render(template: 'deviceAlertStatusLozenge', model: [alert: alert])
	}
}
