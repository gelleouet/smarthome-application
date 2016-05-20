package smarthome.social

import smarthome.automation.Device;
import smarthome.automation.DeviceService;
import smarthome.automation.DeviceValue;
import smarthome.automation.DeviceValueService;
import smarthome.core.AbstractService;
import smarthome.core.Chronometre;
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
	FilActualiteCommand filActualite(User user, Map pagination) {
		Chronometre chrono = new Chronometre()
		FilActualiteCommand command = new FilActualiteCommand()
		
		command.userDevices = Device.findAllByUser(user)
		
		//on rajoute les objets partagés par amis
		def deviceIds = deviceService.listSharedDeviceId(user.id)
		if (deviceIds) {
			command.sharedDevices = Device.getAll(deviceIds)
		}
		
		def allDevices = []
		allDevices.addAll(command.userDevices)
		allDevices.addAll(command.sharedDevices)
		command.values = deviceValueService.lastValuesByDevices(allDevices, pagination)
		
		log.info("Fil actualite ${user.username} : ${chrono.stop()}ms")
		
		return command
	}
}
