package smarthome.core.aspect

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import smarthome.core.AsynchronousMessage;
import smarthome.core.ExchangeType;
import smarthome.core.SmartHomeCoreConstantes;
import smarthome.core.SmartHomeException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
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
	TransactionAttributeSource transactionAttributeSource
	
	private static final log = LogFactory.getLog(this)
	
	
	
	/**
	 * FIXME : Le pointcut "@annotation(AsynchronousMessage)" ne fonctionne plus quand un service
	 * contient une annotation Spring ACL (Postfilter, Preauthorise, etc.).
	 *
	 * Il fonctionne correctement si <aop:aspectj-autoproxy> est activé mais du coup c'est les annotations
	 * Spring ACL qui ne fonctionnent plus
	 *
	 * Du coup on prend plus large et le code filtre les appels de méthode en vérifiant la présence
	 * des annotations
	 */
	@Pointcut("execution(* smarthome..*Service.*(..))")
	void smarthomeServiceMethod() {}
	
	
	
	@AfterReturning(value = "smarthomeServiceMethod()", returning = "result")
	void afterAsynchronousMessage(JoinPoint joinPoint, Object result) {
		MethodSignature signature = (MethodSignature) joinPoint.signature
		Method method = signature.method
		Annotation asyncMessage = method.getAnnotation(AsynchronousMessage)
		
		if (asyncMessage) {
			def transactionAttribute = transactionAttributeSource.getTransactionAttribute(joinPoint.signature.method, joinPoint.target.class)

			if (transactionAttribute) {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
					@Override
					public void afterCommit() {
						sendAsyncMessage(joinPoint, asyncMessage, result)
					}
				});
			} else {
				sendAsyncMessage(joinPoint, asyncMessage, result)
			}
		}
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
		def payload = [:]
		payload.result = result
		payload.serviceMethodName = StringUtils.uncapitalize(joinPoint.target.class.simpleName) + '.' + joinPoint.signature.name
		
		if (joinPoint.args) {
			joinPoint.args.eachWithIndex { arg, index ->
				payload."arg$index" = arg
			}
		}
		
		log.debug "Publish message to ${asynchronousMessage.exchange()}"
		joinPoint.target.sendAsynchronousMessage(asynchronousMessage.exchange(), routingKey, payload,
			asynchronousMessage.exchangeType())
		
		// envoi en même temps vers le exchange prévu pour le workflow
		// on a un seul exchange workflow, cela permet d'avoir une seule route pour exécuter les workflow
		// plutot que définir une route pour chaque workflow à exécuter. Cela évite de la configuration
		// pour démarrer le système workflow
		joinPoint.target.sendAsynchronousMessage(SmartHomeCoreConstantes.DIRECT_EXCHANGE,
			SmartHomeCoreConstantes.WORKFLOW_QUEUE, payload, ExchangeType.DIRECT)
	}
}
