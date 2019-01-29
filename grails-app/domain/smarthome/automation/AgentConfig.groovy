package smarthome.automation

import java.io.Serializable;
import java.util.Map;

import smarthome.core.JsonDataDomain;
import smarthome.core.SmartHomeCoreConstantes;
import smarthome.security.User;
import grails.validation.Validateable;
import groovy.time.TimeCategory;

/**
 * Configuration de l'agent
 * Ce sont les données stokées dans le fichier credentials de l'agent.
 * Cela permet d'éditer la config depuis l'application
 *  
 * @author gregory
 *
 */
@Validateable
class AgentConfig extends JsonDataDomain implements Serializable {
	Agent agent
	Date lastSync
	String data
	
	
	// Propriétés utilisateurs
	Map jsonData = [:]
	
	
	static transients = ['jsonData']
	
	static belongsTo = [agent: Agent]
	
    static constraints = {
		
    }
	
	static mapping = {
		table schema: SmartHomeCoreConstantes.DEFAULT_SCHEMA
		agent index: "AgentConfig_Idx"
	}
	
	
//	static {
//		grails.converters.JSON.registerObjectMarshaller(AgentConfig) {
//			[dateExpiration: it.dateExpiration, token: it.token, websocketUrl: it.websocketUrl]
//		}
//	}
}
