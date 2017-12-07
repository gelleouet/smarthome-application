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

import smarthome.core.SmartHomeCoreConstantes;

/**
 * Gestionnaire envoi de mails avec template
 * 
 * @author gregory
 *
 */
class EmailRouteBuilder extends RouteBuilder {

	private static final log = LogFactory.getLog(this)
	
	final String EXCHANGE = SmartHomeCoreConstantes.DIRECT_EXCHANGE
	final String QUEUE = SmartHomeCoreConstantes.EMAIL_QUEUE
	
	
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

		String smtpHostname = grailsApplication.config.smtp.hostname
		String smtpPort = grailsApplication.config.smtp.port
		String smtpPassword = grailsApplication.config.smtp.password
		String smtpUsername = grailsApplication.config.smtp.username
		String smtpFrom = grailsApplication.config.smtp.from
		
		// lecture depuis la queue AMQP
		from("rabbitmq://$rabbitHostname/$EXCHANGE?queue=$QUEUE&routingKey=$QUEUE&username=$rabbitUsername&password=$rabbitPassword&declare=true&automaticRecoveryEnabled=true&autoDelete=false")
		// Décodage du JSON dans une map
		.unmarshal().json(JsonLibrary.Gson, Map.class)
		.setHeader("to").groovy("body.to")
		.setHeader("subject").groovy("body.subject")
		.setHeader("cc").groovy("body.cc ?: null")
		.setHeader("BCC").groovy("body.bcc ?: null")
		.setHeader("from", constant(smtpFrom))
		// template mail
		.to("velocity:/smarthome/esb/routes/EmailNotificationTemplate.vm")
		// envoi mail SMTP
		.to("smtp://$smtpHostname:$smtpPort?password=RAW($smtpPassword)&username=$smtpUsername&mail.smtp.starttls.enable=true&mail.smtp.auth=true&contentType=text/html")
	}
}
