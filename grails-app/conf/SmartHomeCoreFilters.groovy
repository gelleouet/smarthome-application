import smarthome.core.SmartHomeCoreConstantes
import org.apache.commons.logging.LogFactory

class SmartHomeCoreFilters {
	
	private static final log = LogFactory.getLog(this)
	
	
	def filters = {
		/**
		 * LastURIFilter
		 *
		 * Rajoute dans la session, l'URI de la dernière requête, hors appel Ajax
		 * 
		 * Est utilisée notamment en cas d'erreur pour rediriger vers la page précédente
		 * ou pour le bouton cancel
		 */
		lastURI(controller: '*', action: '*', uriExclude: '/assets/**') {
			afterView = { Exception ex ->
				if (!request.xhr) {
					String lastUrl = request.forwardURI.replace(request.contextPath, "")
					log.debug 'Save last URI...' + lastUrl
					session[(SmartHomeCoreConstantes.SESSION_LAST_URI)] = lastUrl
				}
			}
		}
	}
}
