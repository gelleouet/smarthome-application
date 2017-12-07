package smarthome.esb.routes

import org.apache.camel.CamelContext
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.springframework.beans.factory.annotation.Autowired;

import smarthome.core.SmartHomeCoreConstantes;

/**
 * Vidage des caches en mode distribué
 * 
 * @author gregory
 *
 */
class CacheRouteBuilder extends RouteBuilder {

	private static final log = LogFactory.getLog(this)
	
	final String EXCHANGE = SmartHomeCoreConstantes.CACHE_QUEUE
	
	@Autowired
	GrailsApplication grailsApplication
	
	
	@Override
	void configure() throws Exception {
		String rabbitHostname = grailsApplication.config.rabbitmq.connectionfactory.hostname
		String rabbitUsername = grailsApplication.config.rabbitmq.connectionfactory.username
		String rabbitPassword = grailsApplication.config.rabbitmq.connectionfactory.password

		def serverId = grailsApplication.config.smarthome.cluster.serverId
		
		if (!serverId) {
			throw new Exception("lims.cluster.serverId property must be set !")
		}
		
		// personnalisation de la queue en fonction du serveur
		def queueName = EXCHANGE + '.' + serverId
		
		// IMPORTANT : utiliser bridgeEndpoint=true sur le endpoint final RabbitMQ sinon,
		// ca tourne en boucle sur la route
		
		from("rabbitmq://$rabbitHostname/$EXCHANGE?queue=$queueName&username=$rabbitUsername&password=$rabbitPassword&declare=true&autoDelete=false&automaticRecoveryEnabled=true&exchangeType=fanout")
		.unmarshal().json(JsonLibrary.Gson, Map.class)
		.setProperty("cacheName").groovy('in.body.arg0')
		.to("bean:cacheService?method=clearApp(property.cacheName)")
	}
}
