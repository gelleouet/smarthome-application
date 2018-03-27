package smarthome.automation

import java.io.Serializable;

import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;
import groovy.time.TimeCategory;

/**
 * Token de connexion pour un agent
 *  
 * @author gregory
 *
 */
@Validateable
class AgentToken implements Serializable {
	static belongsTo = [agent: Agent]
	static transients = ['websocketUrl']
	
	Date dateExpiration
	String token
	String websocketKey
	String websocketUrl
	String serverId // identifie le serveur dans un cluster
	
    static constraints = {
		token unique: true
		websocketKey nullable: true
		serverId nullable: true
		websocketUrl nullable: true, bindable: true
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		token index: "AgentToken_Token_Idx"
	}
	
	
	static {
		grails.converters.JSON.registerObjectMarshaller(AgentToken) {
			[dateExpiration: it.dateExpiration, token: it.token, websocketUrl: it.websocketUrl]
		}
	}
	
	
	/**
	 * Token est expiré
	 * 
	 * @return
	 */
	boolean hasExpired() {
		!dateExpiration //|| dateExpiration < new Date()
	}
	
	
	/**
	 * Calcul un nouveau token avec sa date d'expiration
	 * 
	 * @return
	 */
	def refreshToken() {
		use(TimeCategory) {
			dateExpiration = new Date() + 4.hours
		}
		
		token = UUID.randomUUID()
	}
}
