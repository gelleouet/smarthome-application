/**
 * 
 */
package smarthome.esb.routes

import org.apache.camel.CamelContext
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Prend en charge les messages AMQP de la queue registerService.resetPassowrd.
 * Envoit d'un mail à l'utilisateur contenant un lien avec le token d'identification
 * pour le rediriger vers la page lui demandant son nouveau mot de passe
 * 
 * @author gregory
 *
 */
class AgentReceiveMessageRouteBuilder extends RouteBuilder {

	private static final log = LogFactory.getLog(this)
	
	final String EXCHANGE = "amq.direct"
	final String QUEUE = "smarthome.automation.agentService.receiveMessage"
	
	
	@Autowired
	GrailsApplication grailsApplication
	
	/**
	 * 
	 */
	@Override
	void configure() throws Exception {
		String rabbitHostname = grailsApplication.config.rabbitmq.connectionfactory.hostname
		String rabbitUsername = grailsApplication.config.rabbitmq.connectionfactory.username
		String rabbitPassword = grailsApplication.config.rabbitmq.connectionfactory.password

		// lecture depuis la queue AMQP
		from("rabbitmq://$rabbitHostname/$EXCHANGE?queue=$QUEUE&routingKey=$QUEUE&username=$rabbitUsername&password=$rabbitPassword&declare=true&automaticRecoveryEnabled=true&autoDelete=false")
		// Décodage du JSON dans une map
		.unmarshal().json(JsonLibrary.Gson, Map.class)
		// extrait les options et les datas
		.setProperty("header").groovy('body.arg0.data.header')
		.setProperty("datas").groovy('body.arg0.data')
		.setProperty("agentId").groovy('body.result.id')
		.setProperty("agent").method("agentService", "findById(property.agentId)")
		.choice()
		.when(simple('${property.header} == "deviceValue"')).to("bean:deviceService?method=changeValueFromAgent(property.agent, property.datas)")
	}
}
