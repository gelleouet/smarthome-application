package smarthome.automation

import org.springframework.security.access.annotation.Secured;

import smarthome.core.AbstractController;


@Secured("isAuthenticated()")
class DeviceShareController extends AbstractController {

	DeviceService deviceService
	
	
	def dialogDeviceShare(Device device) {
		deviceService.edit(device)
		render (view: 'dialogDeviceShare', model: [device: device])
	}
}
