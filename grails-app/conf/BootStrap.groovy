import smarthome.automation.AgentEndPoint

class BootStrap {

	// auto-inject
	def grailsApplication
	
	def init = { servletContext ->
		AgentEndPoint.register(grailsApplication, servletContext)
	}

	def destroy = {
	}
}
