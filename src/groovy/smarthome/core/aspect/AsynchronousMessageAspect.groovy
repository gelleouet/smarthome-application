package smarthome.core.aspect

import java.lang.reflect.Method;

import smarthome.core.AsynchronousMessage;
import smarthome.core.SmartHomeException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.codehaus.groovy.grails.orm.support.GrailsTransactionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import smarthome.core.AbstractService;
import smarthome.core.ClassUtils;

/**
 * AOP pour déclencher l'envoi de messages AMQP
 * Ne s'applique qu'aux services héritant de @see AbstractLimsService
 * Le message est toujours sérialisé en JSON, sinon les classes doivent exister du coté "consumer"
 * 
 * L'envoi des messages est exécuté dans la même transaction que l'appel au service.
 * Du coup, si erreur pendant l'envoi du message, la transaction est rollbackée. Les infos de la transaction
 * sont récupérées depuis le service sur l'appel de la méthode source
 * 
 * @author gregory
 *
 */
@Aspect
@Component
class AsynchronousMessageAspect {
	
	@Autowired
	PlatformTransactionManager transactionManager
	
	@Autowired
	TransactionAttributeSource transactionAttributeSource
	
	private static final log = LogFactory.getLog(this)
	
	@Around("@annotation(asynchronousMessage)")
	Object aroundAsynchronousMessage(ProceedingJoinPoint joinPoint, AsynchronousMessage asynchronousMessage) {
		log.debug "Around asynchronousMessagePointcut service:$joinPoint.target method:$joinPoint.signature.name"
		
		if (joinPoint.this instanceof AbstractService) {
			def serviceLims = joinPoint.target
			
			// recherche simple de la méthode par son nom
			Method[] methods = ClassUtils.findMethods(serviceLims.class, joinPoint.signature.name)
			
			if (methods?.length == 1) {
				// on récupère les infos de la transaction sur la méthode du service
				def transactionAttribute = transactionAttributeSource.getTransactionAttribute(methods[0], serviceLims.class)
				
				// on construit une closure à exécuter soit en mode transaction soit sans transaction
				def closure = {
					def routingKey
					def result = joinPoint.proceed()
					
					// détermination du routing key : par défaut = nameService.nameMethod
					if (asynchronousMessage.routingKey() != "") {
						routingKey = asynchronousMessage.routingKey()
					} else {
						routingKey = ClassUtils.prefixAMQ(serviceLims) + '.' + joinPoint.signature.name
					}
					
					// construction d'une map contenant les paramètres d'entrée et le résultat de la méthode
					def payload =[:]
					payload.result = result
					
					if (joinPoint.args) {
						joinPoint.args.eachWithIndex { arg, index ->
							payload."arg$index" = arg
						}
					}
					
					log.debug "Publish message to ${asynchronousMessage.exchange()} -> ${routingKey}  : $payload"
					
					serviceLims.sendAsynchronousMessage(asynchronousMessage.exchange(), routingKey, payload, asynchronousMessage.exchangeType())
					
					return result
				}
				
				// mode transaction
				if (transactionAttribute) {
					new GrailsTransactionTemplate(transactionManager, transactionAttribute).execute(closure)
				} else {
				// mode normal
					closure.call()
				}
			} else {
			throw new RuntimeException("Can't run AsynchronousMessageAspect on overloaded or not found mehod !")
			}
		} else {
			throw new RuntimeException("Apply AsynchronousMessageAspect only on AbstractLimsService method !")
		}
	}
}
