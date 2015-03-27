/**
 * 
 */
package smarthome.automation

import grails.validation.Validateable


/**
 * Définit le format des messages du websocket
 * 
 * @author gregory
 *
 */
class AgentEndPointMessage {
	String mac
	String token
	String applicationKey
	String username
	String websocketKey
	Map data
}
