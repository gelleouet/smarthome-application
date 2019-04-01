package smarthome.automation

import java.io.Serializable;

import grails.async.Promises;
import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.SmartHomeException;
import smarthome.rule.HouseEstimationConsoRuleService;
import smarthome.rule.HouseSyntheseRuleService;
import smarthome.security.User;


class HouseService extends AbstractService {

	HouseSyntheseRuleService houseSyntheseRuleService
	HouseEstimationConsoRuleService houseEstimationConsoRuleService
	DeviceShareService deviceShareService
	GeocodingService geocodingService
	
	
	/**
	 * Maison par défaut de plusieurs users
	 * 
	 * @param userList
	 * @return
	 */
	List<House> listDefaultByUsers(List<User> userList, List<String> fetchs) {
		if (!userList) {
			return []
		}
		
		return House.createCriteria().list() {
			'in' 'user', userList
			eq 'defaut', true
			
			for (String fetch : fetchs) {
				join fetch
			}
		}	
	}
	
	
	List<HouseConso> listLastConsoByHouses(List<House> houses) {
		if (!houses) {
			return []
		}
		
		Date dateConso = new Date().clearTime()
		dateConso[Calendar.DAY_OF_YEAR] = 1
		
		return HouseConso.createCriteria().list() {
			'in' 'house', houses
			eq 'dateConso', dateConso
		}
	}
	
	
	/**
	 * Edition d'une maison
	 *
	 * @param device
	 * @return
	 */
	@PreAuthorize("hasPermission(#house, 'OWNER')")
	House edit(House house) {
		return house
	}
	
	
	/**
	 * Enregistrement maison
	 * 
	 * @param house
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	House save(House house) throws SmartHomeException {
		// valeurs par défaut au 1er enregistrement
		if (!house.id) {
			house.defaut = true
			house.name = "Maison principale"
		}
		
		return super.save(house)
	}
	
	
	/**
	 * Synchro des positions GPS en fonction location
	 * 
	 * @param house
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	House geocode(House house) throws SmartHomeException {
		Map coords
		
		if (house.location) {
			try {
				coords = geocodingService.geocode(house.location)
			} catch (Exception e) {
				log.error "Geocoding ${house.location} : ${e.message}"
			}
		}
		
		if (coords) {
			house.latitude = coords.latitude
			house.longitude = coords.longitude
		} else {
			house.latitude = null
			house.longitude = null
		}
		
		return this.save(house)
	}
	
	
	/**
	 * Calcul position gps en mode asynchrone
	 * 
	 * @param house
	 * @throws SmartHomeException
	 */
	void asyncGeocode(House house) throws SmartHomeException {
		def promise = Promises.task {
			geocode(house)
		}
		
		promise.onComplete { result ->
			
		}
	}
	
	
	/**
	 * Recherche de la maison principale d'un user
	 * 
	 * @param user
	 * @return
	 */
	House findDefaultByUser(User user) {
		return House.findByUserAndDefaut(user, true)
	}
	
	
	/**
	 * Recherche de toutes les maisons d'un user
	 * @param user
	 * @return
	 */
	List<House> findAllByUser(User user) {
		return House.findAllByUser(user)
	}
	
	
	/**
	 * Calcul de la conso sur une année à partir du compteur associé
	 * C'est une estimation car la conso est calculé sur une année complète et
	 * extrapollée sur le reste de l'année (réel sur l'année passée, et estimée sur le reste)
	 * 
	 * @param house
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	House calculConsoAnnuelle(House house, int year) throws SmartHomeException {
		if (!house.attached) {
			house.attach()
		}
		
		// si pas de compteur associé, pas besoin de faire le calcul car pas de conso
		if (!house?.compteur) {
			return house
		}
		
		houseEstimationConsoRuleService.execute(house, false, [year: year])
		
		return house	
	}
	
	
	/**
	 * Calcul des interprétations de la maison
	 * 
	 * @param house
	 * @return
	 * @throws SmartHomeException
	 */
	HouseSynthese calculSynthese(House house) throws SmartHomeException {
		return houseSyntheseRuleService.execute(house, false)
	}
	
	
	/**
	 * Change les modes d'une maison
	 * 
	 * @param command
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	HouseCommand changeMode(HouseCommand command) throws SmartHomeException {
		def existModes = []
		
		// supprime les modes non présents 
		command.house.modes?.removeAll { houseMode ->
			Mode mode = command.modes.find {
				it.id == houseMode.mode.id
			}
			
			// si on l'a trouvé, on le flag car on ne devra pas le réinséré
			if (mode) {
				existModes << mode
			}
			
			return !mode
		}
		
		// supprime les modes déjà présents
		command.modes.removeAll(existModes)
		
		// ajoute les modes sélectionnés
		command.modes.each {
			command.house.addToModes(new HouseMode(house: command.house, mode: it))	
		}
		
		this.save(command.house)
		
		return command
	}
	
	
	/**
	 * Liste les modes activés de la maison par défaut d'un user
	 * 
	 * @return
	 */
	List<Mode> defaultHouseModes(User user) {
		House house = this.findDefaultByUser(user)
		
		return houseModes(house)
	}
	
	
	/**
	 * Liste les modes activés d'une maison
	 * 
	 * @return
	 */
	List<Mode> houseModes(House house) {
		if (!house) {
			return []
		}
		
		return house.modes*.mode
	}
	
	
	/**
	 * Classement DPE (A ... G)
	 * @param house
	 * @param conso
	 * @return
	 */
	DPE classementDPE(House house, HouseConso conso) {
		if (!house?.surface || !conso) {
			return null
		}
		
		DPE dpe = new DPE()
		dpe.kwParAn = ((conso.kwHC + conso.kwHP + conso.kwBASE + conso.kwGaz) / house.surface) as Integer
		
		if (dpe.kwParAn <= 50) {
			dpe.note = "A"
		} else if (dpe.kwParAn > 50 && dpe.kwParAn <= 90) {
			dpe.note = "B"
		} else if (dpe.kwParAn > 90 && dpe.kwParAn <= 150) {
			dpe.note = "C"
		} else if (dpe.kwParAn > 150 && dpe.kwParAn <= 230) {
			dpe.note = "D"
		} else if (dpe.kwParAn > 230 && dpe.kwParAn <= 330) {
			dpe.note = "E"
		} else if (dpe.kwParAn > 330 && dpe.kwParAn <= 450) {
			dpe.note = "F"
		} else {
			dpe.note = "G"
		}
		
		dpe.index = (dpe.note as char) - ('A' as char)
		
		return dpe
	}
	
	
	/**
	 * Compte le nombre de maisons pour les calculs de conso
	 * 
	 * @return
	 */
	long countHouseForCalculConso() {
		return House.createCriteria().get {
			isNotNull "surface"
			isNotNull "compteur"
			projections {
				count("id")
			}
		}	
	}
	
	
	/**
	 * Les ids des maisons pour les calculs de conso
	 * 
	 * @return
	 */
	List<Map> listHouseIdsForCalculConso(Map pagination) {
		return House.createCriteria().list(pagination) {
			isNotNull "surface"
			isNotNull "compteur"
			projections {
				property "id", "id"
			}
			order "id"
			// transformer pour récupérer une map au lieu d'un tableau
			resultTransformer org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP
		}	
	}
	
	
	/**
	 * Utile pour les environnements sans session hibernate automatique
	 * Ex : Camel ESB
	 *
	 * @param id
	 * @return
	 */
	House findById(Serializable id) {
		House.get(id)
	}
	
	
	/**
	 * Partage une maison à un utilisateur
	 * Seuls les objets par défaut liés à la maison sont partagés
	 * 
	 * @param house
	 * @param sharedUser
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void shareHouse(House house, User sharedUser) throws SmartHomeException {
		if (house) {
			if (house.compteur) {
				deviceShareService.addShare(house.compteur, sharedUser.id)
			}	
			if (house.humidite) {
				deviceShareService.addShare(house.humidite, sharedUser.id)
			}
			if (house.temperature) {
				deviceShareService.addShare(house.temperature, sharedUser.id)
			}
		}
	}
	
	
	/**
	 * Partage la maison par défaut à un utilisateur
	 * Seuls les objets par défaut liés à la maison sont partagés
	 *
	 * @param house
	 * @param sharedUser
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void shareDefaultHouse(User user, User sharedUser) throws SmartHomeException {
		House house = this.findDefaultByUser(user)
		
		if (house) {
			this.shareHouse(house, sharedUser)
		}
		if (house.compteur) {
			deviceShareService.addShare(house.compteur, sharedUser.id)
		}
	}
}
