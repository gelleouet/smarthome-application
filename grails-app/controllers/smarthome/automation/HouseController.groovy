package smarthome.automation

import org.springframework.security.access.annotation.Secured;

import smarthome.automation.deviceType.Humidite;
import smarthome.automation.deviceType.TeleInformation;
import smarthome.automation.deviceType.Temperature;
import smarthome.core.AbstractController;
import smarthome.security.User;


@Secured("isAuthenticated()")
class HouseController extends AbstractController {

	HouseService houseService
	DeviceService deviceService
	
	
	/**
	 * Suppression d'un mode par son index
	 * 
	 * @param command
	 * @return
	 */
	def deleteMode(HouseCommand command, int status) {
		houseService.deleteMode(command, status)
		render(template: 'modes', model: [modes: command.modes])
	}
	
	
	/**
	 * Ajout d'un mode
	 * 
	 * @param command
	 * @return
	 */
	def addMode(HouseCommand command) {
		houseService.addMode(command)
		render(template: 'modes', model: [modes: command.modes])
	}
	
	
	/**
	 * Edition d'une maison
	 * 
	 * @param house
	 * @return
	 */
	def templateEditByUser() {
		def user = params.user
		def house = houseService.findDefaultByUser(user)
		def compteurs = deviceService.listByUser(new DeviceSearchCommand([userId: user.id,
			deviceTypeClass: TeleInformation.name]))
		def temperatures = deviceService.listByUser(new DeviceSearchCommand([userId: user.id,
			deviceTypeClass: Temperature.name]))
		def humidites = deviceService.listByUser(new DeviceSearchCommand([userId: user.id,
			deviceTypeClass: Humidite.name]))
		render(template: 'form', model: [house: house, compteurs: compteurs, user: user,
			temperatures: temperatures, humidites: humidites])
	}
}
