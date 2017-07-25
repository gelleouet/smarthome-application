package smarthome.automation

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import smarthome.automation.deviceType.Humidite;
import smarthome.automation.deviceType.TeleInformation;
import smarthome.automation.deviceType.Temperature;
import smarthome.core.AbstractController;
import smarthome.security.User;


@Secured("isAuthenticated()")
class HouseController extends AbstractController {

	HouseService houseService
	DeviceService deviceService
	ModeService modeService
	
	
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
	
	
	/**
	 * Changement de modes d'une maison
	 * 
	 * @param house
	 * @return
	 */
	def changeMode(HouseCommand command) {
		def user = authenticatedUser
		command.house.user = user
		houseService.changeMode(command)
		render(template: 'changeMode', model: [house: command.house, user: user,
			modes: modeService.listModesByUser(user)])
	}
	
	
	/**
	 * Calcul conso à la demande
	 * 
	 * @param house
	 * @return
	 */
	def calculConso(House house) {
		houseService.edit(house)
		Date now = new Date()
		houseService.calculConsoAnnuelle(house, now[Calendar.YEAR])
		nop()
	}
	
	
	/**
	 * Rendu de la synthese de la maison
	 * 
	 * @return
	 */
	@PreAuthorize("hasPermission(#house, 'OWNER')")
	def synthese(House house) {
		def houseSynthese = houseService.calculSynthese(house)
		render(template: 'synthese', model: [house: house, houseSynthese: houseSynthese])
	}
}
