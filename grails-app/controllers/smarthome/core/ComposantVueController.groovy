package smarthome.core

import smarthome.core.AbstractController;
import org.springframework.security.access.annotation.Secured;


@Secured("isAuthenticated()")
class ComposantVueController extends AbstractController {

    private static final String COMMAND_NAME = 'composantVue'
	
	ComposantVueService composantVueService
	
	/**
	 * Enregistrement des datas pour l'utilisateur connecté
	 * Dédié principalement à des appels Ajax
	 * 
	 * @return
	 */
	def saveData(String name, String page, String data) {
		// principal (plugin sping security)
		composantVueService.saveData(name, page, principal.id, data)
		render (text: data ?: '')
	}
}
