import grails.util.Environment
import smarthome.core.EndPointUtils

class BootStrap {

	// auto-inject
	def smarthomeScheduler
	
	def init = { servletContext ->
		// Mettre ici le code spécial env dev
		if (Environment.current == Environment.DEVELOPMENT) {
			EndPointUtils.register(servletContext, smarthome.endpoint.AgentEndPoint)
			EndPointUtils.register(servletContext, smarthome.endpoint.ShellEndPoint)
			EndPointUtils.register(servletContext, smarthome.endpoint.TeleinfoEndPoint)
		} 

		// Mettre ici le code spécial env prod
		if (Environment.current == Environment.PRODUCTION) {
			smarthomeScheduler.start()
		}
		
		// Mettre ici le code multi env
	}

	
	def destroy = {
		// Mettre ici le code spécial env dev
		if (Environment.current == Environment.DEVELOPMENT) {
			
		}
		
		// Mettre ici le code spécial env prod
		if (Environment.current == Environment.PRODUCTION) {
			smarthomeScheduler.shutdown()
		}
		
		// Mettre ici le code multi env
	}
}
