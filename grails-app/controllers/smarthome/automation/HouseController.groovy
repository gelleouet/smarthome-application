package smarthome.automation

import org.springframework.security.access.annotation.Secured
import smarthome.automation.deviceType.Compteur
import smarthome.automation.deviceType.CompteurGaz
import smarthome.automation.deviceType.Humidite
import smarthome.automation.deviceType.TeleInformation
import smarthome.automation.deviceType.Temperature
import smarthome.core.AbstractController
import smarthome.security.User


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
		def compteursGaz = deviceService.listByUser(new DeviceSearchCommand([userId: user.id,
			deviceTypeClass: CompteurGaz.name]))
		def temperatures = deviceService.listByUser(new DeviceSearchCommand([userId: user.id,
			deviceTypeClass: Temperature.name]))
		def humidites = deviceService.listByUser(new DeviceSearchCommand([userId: user.id,
			deviceTypeClass: Humidite.name]))

		render(template: 'form', model: [house: house, compteurs: compteurs, user: user,
			temperatures: temperatures, humidites: humidites, compteursGaz: compteursGaz])
	}


	/**
	 * Changement de modes d'une maison
	 * 
	 * @param house
	 * @return
	 */
	def changeMode(HouseCommand command) {
		def user = authenticatedUser

		if (!command?.house?.id) {
			command.house = houseService.findDefaultByUser(user)
		}

		command.house.user = user
		houseService.changeMode(command)
		render(template: 'changeMode', model: [house: command.house, user: user,
			modes: modeService.listModesByUser(user)])
	}


	/**
	 * 
	 * @return
	 */
	def widgetMode() {
		def user = authenticatedUser
		def house = houseService.findDefaultByUser(user)
		render(template: 'changeMode', model: [house: house, user: user,
			modes: modeService.listModesByUser(user)])
	}


	def configMode() {
		redirect(controller: 'profil', action: 'profil')
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


	def calculConsoForUser(User user) {
		Date now = new Date()
		def house = houseService.findDefaultByUser(user)
		houseService.calculConsoAnnuelle(house, now[Calendar.YEAR])
		nop()
	}


	/**
	 * Rendu de la synthese confort de la maison
	 * 
	 * @return
	 */
	def syntheseConfort() {
		def user = authenticatedUser
		def house = houseService.findDefaultByUser(user)
		internSyntheseConfort(house)
	}


	/**
	 * 
	 * @return
	 */
	def syntheseConfortHouse(House house) {
		internSyntheseConfort(house)
	}


	private def internSyntheseConfort(house) {
		def houseSynthese = houseService.calculSynthese(house)
		render(template: 'syntheseConfort', model: [house: house, houseSynthese: houseSynthese])
	}


	/**
	 * Rendu de la synthese consommation du jour
	 * 
	 * @return
	 */
	def syntheseConsommationDay(House house) {
		def user = authenticatedUser

		if (!house?.id) {
			house = houseService.findDefaultByUser(user)
		}
		render(template: 'syntheseConsommationDay', model: [house: house])
	}


	/**
	 * Rendu de la synthese consommation du mois
	 * 
	 * @return
	 */
	def syntheseConsommationMonth(House house) {
		def user = authenticatedUser

		if (!house?.id) {
			house = houseService.findDefaultByUser(user)
		}

		render(template: 'syntheseConsommationMonth', model: [house: house])
	}


	/**
	 * Rendu de la synthese consommation de l'année
	 *
	 * @return
	 */
	def syntheseConsommationYear(House house) {
		def user = authenticatedUser

		if (!house?.id) {
			house = houseService.findDefaultByUser(user)
		}

		render(template: 'syntheseConsommationYear', model: [house: house])
	}


	/**
	 * Rendu de la synthese consommation jour/mois/année
	 *
	 * @return
	 */
	def syntheseConsommationAll(House house) {
		def user = authenticatedUser

		if (!house?.id) {
			house = houseService.findDefaultByUser(user)
		}
		render(template: 'syntheseConsommationAll', model: [house: house])
	}


	/**
	 * Calcul les coordonnées GPS de la maison principale
	 *
	 * @param user
	 * @return
	 */
	@Secured("hasRole('ROLE_ADMIN')")
	def geocodeDefaultHouse(User user) {
		House house = houseService.findDefaultByUser(user)

		if (house) {
			houseService.geocode(house)
		}

		nop()
	}
}
