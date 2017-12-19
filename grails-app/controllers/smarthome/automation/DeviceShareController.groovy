package smarthome.automation

import org.springframework.security.access.annotation.Secured;

import smarthome.core.AbstractController;


@Secured("isAuthenticated()")
class DeviceShareController extends AbstractController {

	DeviceService deviceService
	DeviceShareService deviceShareService
	
	
	/**
	 * 
	 * @param device
	 * @return
	 */
	def dialogDeviceShare(Device device) {
		deviceService.edit(device)
		def shares = deviceShareService.listByDevice(device)
		render (template: 'dialogDeviceShare', model: [device: device, shares: shares])
	}
	
	
	/**
	 * 
	 * @param deviceShare
	 * @return
	 */
	def delete(DeviceShare deviceShare) {
		def device = deviceShare.device
		deviceService.edit(device)
		deviceShareService.delete(deviceShare)
		def shares = deviceShareService.listByDevice(device)
		render (template: 'datatable', model: [device: device, shares: shares])
	}
	
	
	/**
	 * 
	 * @param device
	 * @return
	 */
	def addShare(Device device, long sharedUserId) {
		deviceService.edit(device)
		deviceShareService.addShare(device, sharedUserId)
		def shares = deviceShareService.listByDevice(device)
		render (template: 'datatable', model: [device: device, shares: shares])
	}
}
