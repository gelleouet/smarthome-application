import smarthome.automation.AgentEndPoint

class BootStrap {

	// auto-inject
	def grailsApplication
	def smarthomeScheduler
	
	def init = { servletContext ->
		// démarre le websocket
		AgentEndPoint.register(grailsApplication, servletContext)
		
		// démarre le gestionnaire de cron
		smarthomeScheduler.start()
	}

	
	def destroy = {
		smarthomeScheduler.shutdown()
	}
}
