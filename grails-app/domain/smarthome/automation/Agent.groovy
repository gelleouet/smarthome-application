package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Un agent connecté à l'appli (Raspberry, Box domotique, Arduino, etc...)
 *  
 * @author gregory
 *
 */
@Validateable
class Agent {
	static belongsTo = [user: User]
	static hasMany = [devices: Device, tokens: AgentToken]
	
	String mac
	String privateIp
	String publicIp
	String agentModel
	Date lastConnexion
	boolean locked
	boolean online
	
    static constraints = {
		mac unique: ['user']
		privateIp nullable: true
		publicIp nullable: true
    }
	
	static mapping = {
		user index: "Agent_User_Idx"
		sort 'mac'
	}
	
	
	static {
		grails.converters.JSON.registerObjectMarshaller(Agent) {
			[id: it.id, mac: it.mac, agentModel: it.agentModel, locked: it.locked, online: it.online]
		}
	}
}
