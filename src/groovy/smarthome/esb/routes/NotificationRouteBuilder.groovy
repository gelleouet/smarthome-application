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
 * Ecoute le exchange "smarthome.automation.deviceService.changeValue" et 
 * déclenche les événements associés au devices
 * 
 * @author gregory
 *
 */
class NotificationRouteBuilder extends RouteBuilder {

	private static final log = LogFactory.getLog(this)
	
	final String IN_EXCHANGE = "smarthome.automation.deviceEventService.triggerEvent"
	final String IN_QUEUE = "smarthome.automation.notificationService.sendDeviceEventNotifications"
	
	
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
		
		
		// IMPORTANT : utiliser bridgeEndpoint=true sur le endpoint final RabbitMQ sinon, 
		// ca tourne en boucle sur la route
		
		// lecture depuis la queue AMQP
		from("rabbitmq://$rabbitHostname/$IN_EXCHANGE?queue=$IN_QUEUE&username=$rabbitUsername&password=$rabbitPassword&declare=true&autoDelete=false&automaticRecoveryEnabled=true&exchangeType=fanout")
		// Décodage du JSON dans une map
		.unmarshal().json(JsonLibrary.Gson, Map.class)
		// recupère l'event
		.setProperty("deviceEventId").groovy('body.event.id')
		.setProperty("deviceEvent").method("deviceEventService", "findById(property.deviceEventId)")
		.to("bean:notificationAccountService?method=sendDeviceEventNotifications(property.deviceEvent)")
	}
}
