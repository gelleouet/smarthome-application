import grails.util.Environment
import smarthome.core.EndPointUtils

class BootStrap {

	// auto-inject
	def smarthomeScheduler
	
	def init = { servletContext ->
		if (Environment.current == Environment.DEVELOPMENT) {
			EndPointUtils.register(servletContext, smarthome.endpoint.AgentEndPoint)
			EndPointUtils.register(servletContext, smarthome.endpoint.ShellEndPoint)
			EndPointUtils.register(servletContext, smarthome.endpoint.TeleinfoEndPoint)
		}
		
		// démarre le gestionnaire de cron
		smarthomeScheduler.start()
	}

	
	def destroy = {
		smarthomeScheduler.shutdown()
	}
}
