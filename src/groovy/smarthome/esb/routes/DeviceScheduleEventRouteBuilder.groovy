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
class DeviceScheduleEventRouteBuilder extends RouteBuilder {

	private static final log = LogFactory.getLog(this)
	
	final String EXCHANGE = "amq.direct"
	final String QUEUE = "smarthome.automation.deviceEventService.executeScheduleDeviceEvent"
	
	
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
		from("rabbitmq://$rabbitHostname/${EXCHANGE}?queue=${QUEUE}&routingKey=${QUEUE}&username=$rabbitUsername&password=$rabbitPassword&declare=true&autoDelete=false&automaticRecoveryEnabled=true")
		// Décodage du JSON dans une map
		.unmarshal().json(JsonLibrary.Gson, Map.class)
		// recupère le device event
		.setProperty("deviceEventId").groovy('body.result.id')
		.setProperty("deviceEvent").method("deviceEventService", "findById(property.deviceEventId)")
		.to("bean:deviceEventService?method=triggerDeviceEvent(property.deviceEvent, '', null)")
	}
}
