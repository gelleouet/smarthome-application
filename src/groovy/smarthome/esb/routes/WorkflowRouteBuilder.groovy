package smarthome.esb.routes

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
 * Déclenchement des workflows LIMS
 * 
 * Se branche sur la queue "lims.core.workflow" 
 * bindée sur l'exchange "lims.core.serviceMethodExecutionAspect.workflow"  
 * 
 * @author gregory
 *
 */
class WorkflowRouteBuilder extends RouteBuilder {

	private static final log = LogFactory.getLog(this)
	
	@Autowired
	GrailsApplication grailsApplication
	
	
	@Override
	void configure() throws Exception {
		String rabbitHostname = grailsApplication.config.rabbitmq.connectionfactory.hostname
		String rabbitUsername = grailsApplication.config.rabbitmq.connectionfactory.username
		String rabbitPassword = grailsApplication.config.rabbitmq.connectionfactory.password

		def queueName = SmartHomeCoreConstantes.WORKFLOW_QUEUE
		
		log.info "Build workflow Camel route : $queueName"
		
		// IMPORTANT : utiliser bridgeEndpoint=true sur le endpoint final RabbitMQ sinon, 
		// ca tourne en boucle sur la route
		
		// lecture depuis la queue AMQP
		from("rabbitmq://$rabbitHostname/${SmartHomeCoreConstantes.DIRECT_EXCHANGE}?queue=$queueName&routingKey=$queueName&username=$rabbitUsername&password=$rabbitPassword&declare=true&autoDelete=false&automaticRecoveryEnabled=true")
		// Décodage du JSON dans une map
		.unmarshal().json(JsonLibrary.Gson, Map.class)
		// détermine le workflow à exécuter
		.setProperty("workflowLibelle").groovy("'Activiti_' + body.workflowName")
		.setProperty("workflow").method("workflowService", "findByLibelle(property.workflowLibelle)")
		// filtre les messages sans worklow connu
		.filter().simple('${property.workflow} != null')
		// recupère l'objet datas
		.setProperty("context").groovy('body')
		// envoi les datas au service workflow
		.to("bean:workflowService?method=execute(property.workflow, property.context)")
	}
}
