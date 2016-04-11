package smarthome.automation

import org.springframework.security.access.annotation.Secured;

import smarthome.core.AbstractController;


@Secured("isAuthenticated()")
class DeviceShareController extends AbstractController {

	DeviceService deviceService
	DeviceShareService deviceShareService
	
	
	def _dialogDeviceShare(Device device) {
		deviceService.edit(device)
		def shares = deviceShareService.listByDevice(device)
		render (template: 'dialogDeviceShare', model: 
			[device: device, shares: shares])
	}
}
