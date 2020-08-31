/**
 * 
 */
package smarthome.esb.routes.disabled

import org.apache.camel.CamelContext
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smarthome.core.SmartHomeException;

/**
 * REcoit les messages de tracage du teleinfo et les oriente au bon service
 * 
 * @author gregory
 *
 */
class TeleinfoMessageRouteBuilder /*extends RouteBuilder*/ {

	private static final log = LogFactory.getLog(this)
	
	final String EXCHANGE = "smarthome.automation.agentService.teleinfoMessage"
	final String QUEUE = "smarthome.automation.agentService.teleinfoMessageConsole"
	
	
	@Autowired
	GrailsApplication grailsApplication
	
	/**
	 * 
	 */
	//@Override
	void configure() throws Exception {
		String rabbitHostname = grailsApplication.config.rabbitmq.connectionfactory.hostname
		String rabbitUsername = grailsApplication.config.rabbitmq.connectionfactory.username
		String rabbitPassword = grailsApplication.config.rabbitmq.connectionfactory.password
		
		// ATTENTION : l'envoi des messages passent par le websocket mais dans un cluster seul un serveur est connecté
		// C'est pour cela qu'une queue est créée pour chaque serveur et seul un serveur répondra au message
		// on rajoute donc le nom du serveur à la queue par défaut
		def serverId = grailsApplication.config.smarthome.cluster.serverId
		
		if (!serverId) {
			throw new SmartHomeException("smarthome.cluster.serverId property must be set !")
		}
		
		def queueName = QUEUE + '.' + serverId
		
		// IMPORTANT : utiliser bridgeEndpoint=true sur le endpoint final RabbitMQ sinon, 
		// ca tourne en boucle sur la route
		
		// lecture depuis la queue AMQP
		from("rabbitmq://$rabbitHostname/${EXCHANGE}?queue=${queueName}&username=$rabbitUsername&password=$rabbitPassword&declare=true&autoDelete=false&automaticRecoveryEnabled=true&exchangeType=fanout")
		// Décodage du JSON dans une map
		.unmarshal().json(JsonLibrary.Gson, Map.class)
		.setProperty("datas").groovy('body.arg0.data')
		.setProperty("agentId").groovy('body.result.id')
		.setProperty("agent").method("agentService", "findById(property.agentId)")
		.to("bean:agentService?method=teleinfoMessage(property.agent, property.datas)")
	}
}
