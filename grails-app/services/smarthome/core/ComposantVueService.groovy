package smarthome.core

import org.springframework.transaction.annotation.Transactional;
import smarthome.core.AbstractService;
import smarthome.core.SmartHomeException;


class ComposantVueService extends AbstractService {

	
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
	 * Renvoit un composant pour le user connecté
	 * 
	 * @param name
	 * @param page
	 * @return
	 */
	ComposantVue findPrincipal(String name, String page) {
		return find(name, page, springSecurityService.principal.id)	
	}
	
	
	
	/**
	 * Changement d'un data sur un composant Vue.
	 * Si le composant n'existe pas, il est créé
	 * 
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	ComposantVue saveData(String name, String page, long userId, String data) throws SmartHomeException {
		ComposantVue composantVue = find(name, page, userId)
		 
		if (! composantVue) {
			composantVue = new ComposantVue(name: name, page: page, userId: userId)
		}
		
		composantVue.data = data
		
		return this.save(composantVue)
	}
}
