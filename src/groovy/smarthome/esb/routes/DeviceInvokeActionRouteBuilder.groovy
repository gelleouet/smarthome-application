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
 * Ecoute le exchange "smarthome.automation.deviceService.changeValue" et filtre les messages
 * venant uniquement de la méhode "device.invokeAction" pour envoyer un message si nécessaire
 * à l'agent associé via le websocket
 * 
 * @author gregory
 *
 */
class DeviceInvokeActionRouteBuilder extends RouteBuilder {

	private static final log = LogFactory.getLog(this)
	
	final String IN_EXCHANGE = "smarthome.automation.deviceService.changeValue"
	final String IN_QUEUE = "smarthome.automation.deviceService.invokeAction"
	
	
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
		// filtre uniquement les appels du service deviceService.invokeAction
		.filter().groovy("body.serviceMethodName == 'deviceService.invokeAction'")
		.setProperty("invokeAction").groovy('body.arg1')
		// recupère le device
		.setProperty("deviceId").groovy('body.result.id')
		.setProperty("device").method("deviceService", "findById(property.deviceId)")
		// récupère l'agent associé
		.setProperty("agent").method("agentService", "findByDevice(property.device)")
		// filtre les devices sans agent
		.filter().simple('${property.agent} != null')
		// construit le message à envoyer
		.setProperty("data").method("deviceService", "invokeActionMessage(property.device, property.invokeAction)")
		.to("bean:agentService?method=sendMessage(property.agent, property.data)")
	}
}
