package smarthome.automation

import org.springframework.security.access.annotation.Secured;
import smarthome.core.AbstractController;


@Secured("isAuthenticated()")
class DevicePlanningController extends AbstractController {

	DeviceService deviceService
	DevicePlanningService devicePlanningService
	
	
	/**
	 * Ajout d'un planning sur un device
	 * 
	 * @param device
	 * @return
	 */
	def addPlanning(Device device) {
		// check si edition possible
		deviceService.edit(device)
		devicePlanningService.addPlanning(device)
		renderDevicePlannings(device)
	}
	
	
	/**
	 * Suppression d'un planning sur un device
	 *
	 * @param device
	 * @return
	 */
	def deletePlanning(Device device, int status) {
		// check si edition possible
		deviceService.edit(device)
		devicePlanningService.deletePlanning(device, status)
		renderDevicePlannings(device)
	}
	
	
	/**
	 * Duplication d'un planning sur un device
	 *
	 * @param device
	 * @return
	 */
	def copyPlanning(Device device, int status) {
		// check si edition possible
		deviceService.edit(device)
		devicePlanningService.copyPlanning(device, status)
		renderDevicePlannings(device)
	}
	
	
	/**
	 * Rendu du template devicePlannings
	 */
	private void renderDevicePlannings(Device device) {
		render (template: 'devicePlannings', model: [device: device,
			devicePlannings: device.devicePlannings])	
	}
}
