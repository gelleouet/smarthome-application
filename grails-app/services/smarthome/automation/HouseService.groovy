package smarthome.automation

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;

import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class HouseService extends AbstractService {

	
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
		Date dateFin = new Date().clearTime()
		
		// 1 seul calcul par jour si toutes les infos sont complètes
		if (house.surface && house.compteur && (!house.dateCalculConso || house.dateCalculConso < dateFin)) {
			Date dateDebut = dateFin.copyWith([date: 1, month: 0]) // 1er janvier
			def nbJour = (dateFin - 1) - dateDebut
			
			// pas de calcul les 2er jours de l'année car calcul sur J-1
			if (nbJour) {
				def firstHP = DeviceValue.firstValueByDay(house.compteur, 'hchp', dateDebut)
				def lastHP = DeviceValue.lastValueByDay(house.compteur, 'hchp', dateFin - 1)
				def firstHC = DeviceValue.firstValueByDay(house.compteur, 'hchc', dateDebut)
				def lastHC = DeviceValue.lastValueByDay(house.compteur, 'hchc', dateFin - 1)
				
				if (firstHP && lastHP && firstHC && lastHC) {
					def consoAnnuelle = (lastHP.value - firstHP.value) + (lastHC.value - firstHC.value)
					
					if (consoAnnuelle) {
						// produit en croix pour extrapoller sur une année complète
						house.consoAnnuelle = consoAnnuelle * 365 / nbJour
						house.consoAnnuelle = house.consoAnnuelle.round(0)
						house.dateCalculConso = dateFin
						this.save(house)
						log.info "Conso annuelle ${house.name} : ${house.consoAnnuelle}Wh"
					}
				}
			}
		}
		
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
}
