package smarthome.automation

import java.io.Serializable

import smarthome.automation.deviceType.AbstractDeviceType
import smarthome.automation.deviceType.CompteurEau
import smarthome.automation.deviceType.CompteurGaz
import smarthome.automation.deviceType.Humidite
import smarthome.automation.deviceType.TeleInformation
import smarthome.automation.deviceType.Temperature
import smarthome.core.SmartHomeCoreConstantes
import smarthome.core.chart.GoogleChart
import smarthome.security.User
import grails.validation.Validateable

/**
 * Description des maisons d'un user
 *  
 * @author gregory
 *
 */
@Validateable
class House implements Serializable {
	User user
	Set modes = []
	Set consos = []
	Set weathers = []
	String name
	boolean defaut
	Double surface
	Device compteur
	Device compteurGaz
	Device compteurEau
	Device temperature
	Device humidite
	Chauffage chauffage
	ECS ecs
	String location // ou ville
	String latitude
	String longitude
	String codePostal
	String adresse
	Integer nbPersonne


	static belongsTo = [user: User]

	static hasMany = [modes: HouseMode, consos: HouseConso, weathers: HouseWeather]

	static constraints = {
		surface nullable: true
		compteur nullable: true
		compteurGaz nullable: true
		compteurEau nullable: true
		temperature nullable: true
		humidite nullable: true
		chauffage nullable: true
		ecs nullable: true
		location nullable: true
		latitude nullable: true
		longitude nullable: true
		codePostal nullable: true
		adresse nullable: true
		nbPersonne nullable: true
	}

	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		user index: "House_User_Idx"
		modes cascade: 'all-delete-orphan'
		consos cascade: 'all-delete-orphan'
		weathers cascade: 'all-delete-orphan'
		latitude length: 32
		longitude length: 32
		codePostal length: 8
	}


	/**
	 * Recherche de la dernière conso
	 * 
	 * @return
	 */
	HouseConso currentConso() {
		Date now = new Date()
		return consoByYear(now[Calendar.YEAR])
	}


	/**
	 * La conso d'une année (enregistrée au 1er janvier)
	 * 
	 * @param year
	 * @return
	 */
	HouseConso consoByYear(int year) {
		return HouseConso.createCriteria().get {
			eq "house.id", this.id
			eq "dateConso", HouseConso.dateConsoForYear(year)
		}
	}


	/**
	 * Création d'une nouvelle conso pour une année (enregistrée au 1er janvier)
	 * 
	 * @param year
	 * @return
	 */
	HouseConso newConsoForYear(int year) {
		HouseConso conso = new HouseConso(house: this,
		dateConso: HouseConso.dateConsoForYear(year))
		return conso
	}


	/**
	 * Retourne une instance du compteur électrique principal
	 * 
	 * @return
	 */
	AbstractDeviceType compteurElectriqueImpl() {
		if (compteur) {
			return compteur.newDeviceImpl()
		}
		return null
	}


	/**
	 * Retourne une instance du compteur gaz principal
	 *
	 * @return
	 */
	AbstractDeviceType compteurGazImpl() {
		if (compteurGaz) {
			return compteurGaz.newDeviceImpl()
		}
		return null
	}
	
	
	/**
	 * Retourne une instance du compteur eau principal
	 *
	 * @return
	 */
	AbstractDeviceType compteurEauImpl() {
		if (compteurEau) {
			return compteurEau.newDeviceImpl()
		}
		return null
	}


	/**
	 * Retrouve le device équivalent dans ceux associés à la maison
	 * 
	 * @param device
	 * @return
	 */
	Device findSameDevice(Device device) {
		def deviceImpl = device.newDeviceImpl()

		if (deviceImpl instanceof Temperature) {
			return this.temperature
		} else if (deviceImpl instanceof Humidite) {
			return this.humidite
		} else if (deviceImpl instanceof TeleInformation) {
			return this.compteur
		} else if (deviceImpl instanceof CompteurGaz) {
			return this.compteurGaz
		} else if (deviceImpl instanceof CompteurEau) {
			return this.compteurEau
		}

		return null
	}


	/**
	 * Service météo d'une maison
	 * 
	 * @return
	 */
	HouseWeather weather() {
		weathers?.size() ? weathers[0] : null
	}


	/**
	 * Création d'un chart global sur les consommations d'énergie d'une maison
	 * 
	 * @param command
	 * @return
	 */
	GoogleChart chartConsommationEnergie(ChartCommand command) {
	}
}
