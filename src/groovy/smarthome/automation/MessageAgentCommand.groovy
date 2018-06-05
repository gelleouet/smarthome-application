package smarthome.automation

import grails.validation.Validateable;


@Validateable
class MessageAgentCommand {
	
	String username
	String mac
	String applicationKey
	String privateIp
	String publicIp
	String agentModel
	Map data = [:]
	
	
	static constraints = {
		publicIp nullable: true
	}
}
