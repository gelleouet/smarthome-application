package smarthome.core



import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.QueryUtils;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;

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
	def saveData(String name, String page, String dataName, String dataValue) {
		// principal (plugin sping security)
		composantVueService.setData(name, page, principal.id, dataName, dataValue)
		render (text: dataValue ?: '')
	}
	
	
	
	/**
	 * Renvoit la valeur d'une data l'utilisateur connecté
	 * 
	 * @param name
	 * @param page
	 * @param dataName
	 * @return
	 */
	def getData(String name, String page, String dataName) {
		// principal (plugin sping security)
		def value = composantVueService.getData(name, page, principal.id, dataName)
		render (text: value ?: '')
	}
}
