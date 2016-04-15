package smarthome.social

import smarthome.automation.Device;
import smarthome.automation.DeviceService;
import smarthome.automation.DeviceValue;
import smarthome.automation.DeviceValueService;
import smarthome.core.AbstractService;
import smarthome.security.User;

class SocialService extends AbstractService {

    DeviceService deviceService
    DeviceValueService deviceValueService
	
	
	/**
	 * Fil d'actualité d'un user. Affiche les dernières valeurs de tous ses objets (personnels et partagés)
	 * 
	 * @param user
	 * @param pagination
	 * @return
	 */
	List<DeviceValue> filActualite(User user, Map pagination) {
		List<Device> devices = Device.findAllByUser(user)
		//on rajoute les objets partagés par amis
		def deviceIds = deviceService.listSharedDeviceId(user.id)
		if (deviceIds) {
			devices.addAll(Device.getAll(deviceIds))
		}
		
		return deviceValueService.lastValuesByDevices(devices, pagination)
	}
}