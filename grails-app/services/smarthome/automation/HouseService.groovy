package smarthome.automation

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;

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
	 * Calcul de la conso annuelle à partir du compteur associé
	 * C'est une estimation car la conso est calculé sur une année complète et
	 * extrapollée sur le reste de l'année (réel sur l'année passée, et estimée sur le reste)
	 * 
	 * Le calcul est exécuté une fois par jour même si plusieurs appels
	 * 
	 * @param house
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	House calculConsoAnnuelle(House house) throws SmartHomeException {
		houseEstimationConsoRuleService.execute(house, false)
		return house	
	}
	
	
	/**
	 * Recherche de la maison par défaut et caclul de la conso annuelle
	 * @param user
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	House calculDefaultConsoAnnuelle(User user) throws SmartHomeException {
		def house = this.findDefaultByUser(user)
		
		if (house) {
			house = this.calculConsoAnnuelle(house)
		}
		
		return house
	}
	
	
	/**
	 * Liste les modes d'un utilisateur
	 * 
	 * @param user
	 * @return
	 */
	List<HouseMode> listModesByUser(User user) {
		return HouseMode.createCriteria().list {
			eq 'user', user
			order 'name'
		}
	}
	
	
	/**
	 * Suppression d'un mode sans persistance
	 * 
	 * @param command
	 * @param status
	 * @return
	 */
	HouseCommand deleteMode(HouseCommand command, int status) {
		command.modes?.removeAll {
			it.status == status
		}
		
		return command
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
}
