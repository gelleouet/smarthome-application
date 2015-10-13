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
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
		// on récupère les infos de la transaction sur la méthode du service
		def transactionAttribute = transactionAttributeSource.getTransactionAttribute(joinPoint.signature.method, joinPoint.target.class)
		
		// mode transaction : exécution de la méthode et préparation des envois de message amqp
		// les messages sont envoyées après le commit de la transaction
		if (transactionAttribute) {
			new GrailsTransactionTemplate(transactionManager, transactionAttribute).execute({
				def result = executeJoinPoint(joinPoint)
				
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						sendAsyncMessage(joinPoint, asynchronousMessage, result)
					}
				});
			
				return result
			})
		}
		// exécution sans transaction
		else {
			def result = executeJoinPoint(joinPoint)
			sendAsyncMessage(joinPoint, asynchronousMessage, result)
			return result
		}
	}
	
	
	/**
	 * Exécute le joinpoint et renvoit le retour la méthode
	 * 
	 * @param joinPoint
	 * @return
	 */
	private def executeJoinPoint(ProceedingJoinPoint joinPoint) {
		return joinPoint.proceed()
	}
	
	
	
	/**
	 * Envoi le message AMQP
	 * 
	 * @param joinPoint
	 * @param asynchronousMessage
	 * @return
	 */
	private def sendAsyncMessage(ProceedingJoinPoint joinPoint, AsynchronousMessage asynchronousMessage, Object result) {
		def routingKey
		
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
	}
}
