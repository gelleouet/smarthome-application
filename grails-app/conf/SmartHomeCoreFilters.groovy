import smarthome.core.SmartHomeCoreConstantes
import org.apache.commons.logging.LogFactory

class SmartHomeCoreFilters {
	
	private static final log = LogFactory.getLog(this)
	
	
	def filters = {
		/**
		 * defaultSmarthome
		 *
		 * Complétion du model : 
		 *  - mobileAgent
		 *  - secUser
		 */
		defaultSmarthome(controller: '*', action: '*', uriExclude: '/assets/**') {
			after = { model ->
				if (model) {
					// insère le type de device
					String agentHeader = request.getHeader('User-Agent')
					if (agentHeader) {
						model['mobileAgent'] = agentHeader.contains('iPhone') || 
							agentHeader.contains('Android') || agentHeader.contains('Windows Phone')
					} 
					
					// associe un objet 'secUser' pour éviter le recharger (ex dans les menus, etc)
					if (model['user'] && !model['secUser']) {
						model['secUser'] = model['user']
					}
				}
				return true
			}
		}
		
		
		/**
		 * Trace le temps d'exécution de certaines actions
		 * Les plus demandées et les plus importantes
		 */
		traceTimeRequest(controller: 'device|tableauBord|house|houseWeather|profil', action: '*') {
			
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
