package smarthome.automation

import smarthome.security.User;
import grails.validation.Validateable;

/**
 * Token de connexion pour un agent
 *  
 * @author gregory
 *
 */
@Validateable
class AgentToken {
	static belongsTo = [agent: Agent]
	static transients = ['websocketUrl']
	
	Date dateExpiration
	String token
	String websocketKey
	String websocketUrl
	
    static constraints = {
		token unique: true
		websocketKey nullable: true
		websocketUrl nullable: true, bindable: true
    }
	
	static mapping = {
		token index: "AgentToken_Token_Idx"
	}
	
	
	static {
		grails.converters.JSON.registerObjectMarshaller(AgentToken) {
			[dateExpiration: it.dateExpiration, token: it.token, websocketUrl: it.websocketUrl]
		}
	}
	
	
	def hasExpired() {
		!dateExpiration || dateExpiration < new Date()
	}
}
