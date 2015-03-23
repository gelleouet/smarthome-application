package smarthome.core

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;

import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.SmartHomeException;


class ComposantVueService extends AbstractService {

	/**
	 * Enregistrement d'un ComposantVue
	 * 
	 * @param composantVue
	 * 
	 * @return ComposantVue
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
    ComposantVue save(ComposantVue composantVue) throws SmartHomeException {
		if (!composantVue.save()) {
			throw new SmartHomeException("Erreur enregistrement composantVue", composantVue);
		}
		
		return composantVue
	}
	
	
	
	/**
	 * Recherche d'un composant vue pour un user
	 * 
	 * @param name
	 * @param page
	 * @param userId
	 * @return
	 */
	ComposantVue find(String name, String page, long userId) {
		// Création d"un composant exemple pour la recherche
		ComposantVue exemple = new ComposantVue(name: name, page: page, userId: userId)
		
		return ComposantVue.find(exemple)
	}
	
	
	
	/**
	 * Changement d'un data sur un composant Vue. Si le composant n'existe pas, il est créé
	 * Seul le data concerné est changé
	 * 
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@CachePut(value = 'grailsServiceCache', key = "#name + '' + #page + '' + #userId + '' + #dataName")
	String setData(String name, String page, long userId, String dataName, String dataValue) throws SmartHomeException {
		ComposantVue composantVue = find(name, page, userId)
		 
		if (! composantVue) {
			composantVue = new ComposantVue(name: name, page: page, userId: userId)
		}
		
		// recherche du data concerné après transformation en JSON
		def json = composantVue.data ? JSON.parse(composantVue.data) : [:];
		json[dataName] = dataValue
		composantVue.data = new JSON(json).toString(false)
				
		
		if (!composantVue.save()) {
			throw new SmartHomeException("Erreur enregistrement composantVue", composantVue);
		}
		
		return dataValue
	}
	
	
	/**
	 * Recherche la valeur d'un data
	 * 
	 * @param name
	 * @param page
	 * @param userId
	 * @return
	 */
	@Cacheable(value = 'grailsServiceCache', key = "#name + '' + #page + '' + #userId + '' + #dataName")
	String getData(String name, String page, long userId, String dataName) {
		ComposantVue composantVue = find(name, page, userId)
		
		if (! composantVue) {
			return null
		}
		
		def json = JSON.parse(composantVue.data)
		return json[dataName]
	} 
}
