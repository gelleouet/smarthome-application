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

/**
 * Exécution des events programmés
 * 
 * @author gregory
 *
 */
class EventRouteBuilder /*extends RouteBuilder*/ {

	private static final log = LogFactory.getLog(this)
	
	final String EXCHANGE = SmartHomeCoreConstantes.DIRECT_EXCHANGE
	final String QUEUE = SmartHomeCoreConstantes.EVENT_QUEUE
	
	
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
		
		
		// IMPORTANT : utiliser bridgeEndpoint=true sur le endpoint final RabbitMQ sinon, 
		// ca tourne en boucle sur la route
		
		// lecture depuis la queue AMQP
		from("rabbitmq://$rabbitHostname/${EXCHANGE}?queue=${QUEUE}&routingKey=${QUEUE}&username=$rabbitUsername&password=$rabbitPassword&declare=true&autoDelete=false&automaticRecoveryEnabled=true")
		// Décodage du JSON dans une map
		.unmarshal().json(JsonLibrary.Gson, Map.class)
		// filtre les messages sans result
		.setProperty("result").groovy('body.result')
		.filter().simple('${property.result} != null')
		// recupère le device event
		.setProperty("eventId").groovy('body.result.id')
		.setProperty("event").method("eventService", "findById(property.eventId)")
		.to("bean:eventService?method=triggerEvent(property.event, '', null)")
	}
}
