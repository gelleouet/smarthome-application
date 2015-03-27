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
 * Impression des étiquettes échantillon
 * 
 * Se branche sur la queue "printEtiquetteEchantillon" bindée sur l'exchange "lims.sample.echantillonService.reception"  
 * 
 * @author gregory
 *
 */
class DeviceTraceValueRouteBuilder extends RouteBuilder {

	private static final log = LogFactory.getLog(this)
	
	final String IN_EXCHANGE = "smarthome.automation.deviceService.changeValue"
	final String IN_QUEUE = "smarthome.automation.deviceService.traceValue"
	
	
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
		String messageDirectory = grailsApplication.config.rabbitmq.messageDirectory

		log.debug "Use rabbitmq server $rabbitHostname..."
		
		
		// IMPORTANT : utiliser bridgeEndpoint=true sur le endpoint final RabbitMQ sinon, 
		// ca tourne en boucle sur la route
		
		// lecture depuis la queue AMQP
		from("rabbitmq://$rabbitHostname/$IN_EXCHANGE?queue=$IN_QUEUE&username=$rabbitUsername&password=$rabbitPassword&declare=true&autoDelete=false&automaticRecoveryEnabled=true&exchangeType=fanout")
		.to("file://${messageDirectory}/${IN_QUEUE}")
		// Décodage du JSON dans une map
		.unmarshal().json(JsonLibrary.Gson, Map.class)
		// recupère l'imprimante de destination
		.setProperty("device").groovy('smarthome.automation.Device.get(body.result.id)')
		.to("bean:deviceService?method=traceValue(property.device)")
	}
}
