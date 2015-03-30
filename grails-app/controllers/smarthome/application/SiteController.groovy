package smarthome.application

import grails.plugin.springsecurity.annotation.Secured;

@Secured("permitAll()")
class SiteController {

	/**
	 * Page découvrir application
	 * 
	 * @return
	 */
    def decouvrir() {
		
	}
}
