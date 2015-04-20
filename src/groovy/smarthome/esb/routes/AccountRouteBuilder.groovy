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
class AccountRouteBuilder extends RouteBuilder {

	private static final log = LogFactory.getLog(this)
	
	final String EXCHANGE = "amq.direct"
	final String QUEUE = "smarthome.security.registerService.createAccount"
	
	
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

		String smtpHostname = grailsApplication.config.smtp.hostname
		String smtpPort = grailsApplication.config.smtp.port
		String smtpPassword = grailsApplication.config.smtp.password
		String smtpUsername = grailsApplication.config.smtp.username
		String smtpFrom = grailsApplication.config.smtp.from
		
		// lecture depuis la queue AMQP
		from("rabbitmq://$rabbitHostname/$EXCHANGE?queue=$QUEUE&routingKey=$QUEUE&username=$rabbitUsername&password=$rabbitPassword&declare=true&automaticRecoveryEnabled=true&autoDelete=false")
		.to("file://${messageDirectory}/${QUEUE}")
		// Décodage du JSON dans une map
		.unmarshal().json(JsonLibrary.Gson, Map.class)
		.setHeader("to").groovy("body.result.username")
		// template mail
		.to("velocity:/smarthome/esb/routes/AccountMailTemplate.vm")
		// envoi mail SMTP
		.setHeader("subject", constant("Activation compte SmartHome"))
		.setHeader("from", constant(smtpFrom))
		.setHeader("BCC", constant("contact@jdevops.com"))
		.to("smtp://$smtpHostname:$smtpPort?password=$smtpPassword&username=$smtpUsername&mail.smtp.starttls.enable=true&mail.smtp.auth=true&contentType=text/html")
	}
}
