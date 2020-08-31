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

import smarthome.core.SmartHomeCoreConstantes;
import smarthome.core.SmartHomeException;

/**
 * Prend en charge les messages AMQP de la queue registerService.resetPassowrd.
 * Envoit d'un mail à l'utilisateur contenant un lien avec le token d'identification
 * pour le rediriger vers la page lui demandant son nouveau mot de passe
 * 
 * @author gregory
 *
 */
class AgentSendMessageRouteBuilder /*extends RouteBuilder*/ {

	private static final log = LogFactory.getLog(this)
	
	final String EXCHANGE = SmartHomeCoreConstantes.DIRECT_EXCHANGE
	final String QUEUE = "smarthome.automation.agentService.sendMessage"
	
	
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
		
		// lecture depuis la queue AMQP
		from("rabbitmq://$rabbitHostname/$EXCHANGE?queue=${queueName}&routingKey=${queueName}&username=$rabbitUsername&password=$rabbitPassword&declare=true&automaticRecoveryEnabled=true&autoDelete=false")
		// garde le message original qui sera envoyé tel quel à l'agent
		.setProperty("message").simple('${bodyAs(String)}')
		// Décodage du JSON dans une map
		.unmarshal().json(JsonLibrary.Gson, Map.class)
		// extrait les options et les datas
		.setProperty("token").groovy('body.token')
		.setProperty("websocketKey").groovy('body.websocketKey')
		.to("bean:agentService?method=sendMessageToWebsocket(property.token, property.websocketKey, property.message)")
	}
}
