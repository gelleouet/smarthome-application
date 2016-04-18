import smarthome.core.SmartHomeCoreConstantes
import org.apache.commons.logging.LogFactory

class SmartHomeCoreFilters {
	
	private static final log = LogFactory.getLog(this)
	
	
	def filters = {
		/**
		 * userAgent
		 *
		 * Détermine pour chaque requête si le client est un mobile
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
		
		
		/**
		 * Trace le temps d'exécution de certaines actions
		 * Les plus demandées et les plus importantes
		 */
		traceTimeRequest(controller: 'device|tableauBord', action: '*') {
			
			before = {
				long start = System.currentTimeMillis()
				request['request-time-ellpase'] = start
				return true
			}
			
			afterView = { Exception ex ->
				long start = request['request-time-ellpase']
				long timeEllapse = System.currentTimeMillis() - start
				println "${new Date().format('yyyy-MM-dd HH:mm:ss,S')} Render view ${controllerName}.${actionName} : ${timeEllapse}ms"
			}
			
		}
	}
}
