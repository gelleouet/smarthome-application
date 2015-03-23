package smarthome.application

import smarthome.core.AbstractController;
import grails.plugin.springsecurity.annotation.Secured


/**
 * Tableau de bord central
 * 
 * @author gregory
 *
 */
@Secured("isAuthenticated()")
class TableauBordController extends AbstractController {

	/**
	 * 
	 * @return
	 */
	def index() {
		render(view: 'tableauBord')
	}
}
