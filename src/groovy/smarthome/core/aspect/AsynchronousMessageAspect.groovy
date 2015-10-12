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
		if (joinPoint.this instanceof AbstractService) {
			def routingKey
			def result = joinPoint.proceed()
			
			// détermination du routing key : par défaut = package.nameService.nameMethod
			if (asynchronousMessage.routingKey() != "") {
				routingKey = asynchronousMessage.routingKey()
			} else {
				routingKey = ClassUtils.prefixAMQ(joinPoint.target) + '.' + joinPoint.signature.name
			}
			
			// construction d'une map contenant les paramètres d'entrée et le résultat de la méthode
			def payload =[:]
			payload.result = result
			payload.serviceMethodName = StringUtils.uncapitalize(joinPoint.target.class.simpleName) + '.' + joinPoint.signature.name
			
			if (joinPoint.args) {
				joinPoint.args.eachWithIndex { arg, index ->
					payload."arg$index" = arg
				}
			}
			
			log.debug "Publish message to ${asynchronousMessage.exchange()}"
			
			joinPoint.target.sendAsynchronousMessage(asynchronousMessage.exchange(), routingKey, payload, asynchronousMessage.exchangeType())
			
			return result
		} else {
			throw new RuntimeException("Apply AsynchronousMessageAspect only on AbstractLimsService method !")
		}
	}
}
