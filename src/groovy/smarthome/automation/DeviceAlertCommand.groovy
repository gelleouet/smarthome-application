package smarthome.automation

import grails.validation.Validateable


/**
 * Recherche alertes
 * 
 * @author gregory
 *
 */
@Validateable
class DeviceAlertCommand {
	Long id
	Boolean open
	Long userId
}
