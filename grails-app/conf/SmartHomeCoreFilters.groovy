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
		userAgent(controller: '*', action: '*', uriExclude: '/assets/**') {
			after = { model ->
				if (model) {
					String agentHeader = request.getHeader('User-Agent')
					if (agentHeader) {
						model['mobileAgent'] = agentHeader.contains('iPhone') || 
							agentHeader.contains('Android') || agentHeader.contains('Windows Phone')
					} 
				}
				return true
			}
		}
	}
}
