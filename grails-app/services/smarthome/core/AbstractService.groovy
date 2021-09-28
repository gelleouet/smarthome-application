package smarthome.core

import smarthome.core.ExchangeType;
import smarthome.core.SmartHomeException;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.transaction.annotation.Transactional;
import grails.converters.JSON;
import grails.plugin.springsecurity.SpringSecurityService;


/**
 * Classe de base pour tous les services :
 * <ul>
 * <li>Gestion des transactions en lecture seule par défaut</li>
 * <li>Rollback des transactions pour les exceptions métier LIMS</li>
 * <li>Envoi d'un message AMQP pour chaque méthode de service annotée @AsynchronousMessage. queue_name et message = [service_name].[method_name]</li>
 * </ul>
 * 
 * @author gregory
 *
 */
@Transactional(readOnly = true, rollbackFor = [SmartHomeException])
abstract class AbstractService {
	
	// auto injecté
	def rabbitTemplate
	
	SpringSecurityService springSecurityService
	
	
	/**
	 * IMPORTANT : ne jamais appeler directement cette méthode car peut y avoir des problèmes 
	 * en mode transaction (la session n'est pas flushée avant envoi des messages et au niveau
	 * des consumers, les données ne seront pas à jour)
	 * Depuis service : utiliser asyncSendMessage
	 * 
	 * Envoi d'un message asynchrone AMQP.
	 * Avant d'envoyer le message, on déclare la queue avec les options par défaut :
	 * durable: true
	 * exclusive : false
	 * autoDelete : false
	 * Cela évite de déclarer dans la config de l'application toutes les queues possibles
	 * 
	 * Les messages sont envoyés par défaut sur le défault exchange en utitlisant le nom de la
	 * queue comme routing key
	 * 
	 * Tous les messages sont sérialisés en JSON pour éviter des problèmes de conversion depuis objet Java
	 * par les consumers
	 * 
	 * @param queue
	 * @param message
	 * @return
	 */
	def sendAsynchronousMessage(String exchangeName, String routingKey, Object message, ExchangeType exchangeType) throws SmartHomeException {
		try {
			// on instancie un rabbit manager pour créer à la volée la queue
			def rabbitAdmin = new RabbitAdmin(rabbitTemplate.connectionFactory)
			
			// création du exchange à la volée  (si pas un exchange prédéfini)
			if (exchangeName != "amq.direct") {
				rabbitAdmin.declareExchange(newExchange(exchangeName, exchangeType))
			} else {
				// si exchange direct, on peut déjà créer la queue par défaut utilisé pour le routingKey
				rabbitAdmin.declareQueue(new Queue(routingKey, true, false, false))
			
				// création du binding exchange -> queue à la volée
				rabbitAdmin.declareBinding(new Binding(routingKey, DestinationType.QUEUE, exchangeName, routingKey, null))
			}
			
			// conversion des messages en JSON
			def jsonPayload = (!message) ?: (message as JSON).toString()
			
			rabbitTemplate.convertAndSend(exchangeName, routingKey, jsonPayload)
		} catch (Exception ex) {
			log.error("Envoi message", ex)
			// conversion de l'exception pour déclencher les rollback de transaction en cas d'erreur
			throw new SmartHomeException(ex)
		}
	}
	
	
	/**
	 * Méthode à utiliser pour envoyer des messages manuellement depuis un service
	 * En mode transaction, le message ne sera envoyé qu'arpès le commit
	 * En lecture seule, le message est envoyé immédiatement
	 * 
	 * @param exchangeName
	 * @param routingKey
	 * @param message
	 * @param exchangeType
	 * @return
	 * @throws SmartHomeException
	 */
	void asyncSendMessage(String exchangeName, String routingKey, Object message, ExchangeType exchangeType) throws SmartHomeException {
		TransactionUtils.executeAfterCommit {
			sendAsynchronousMessage(exchangeName, routingKey, message, exchangeType)
		}
	}
	
	
	/**
	 * Construction d'un exchange en fonction de son type
	 * 
	 * @param name
	 * @param exchangeType
	 * @return
	 */
	def newExchange(String name, ExchangeType exchangeType) {
		if (exchangeType == ExchangeType.FANOUT) {
			return new FanoutExchange(name, true, false)
		} else {
			return new DirectExchange(name, true, false)
		}
	}
	
	
	/**
	 * Enregistrement d'un domain
	 *
	 * @param domain
	 *
	 * @return domain
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def save(domain) throws SmartHomeException {
		if (!domain.save()) {
			throw new SmartHomeException("Erreur enregistrement domain object", domain);
		}
		
		return domain
	}
	
	
	/**
	 * Suppression d'une instance en gérant l'archivage si l'objet hérite de ValidableDomain
	 * @param domain
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def delete(domain) {
		try {
			// flush direct pour catcher une erreur SQL (ex : clé étrangère) et la renvoyer en SmartHomeException
			// sinon l'erreur est déclenchée hors méthode
			domain.delete(flush: true)
		} catch (Exception ex) {
			throw new SmartHomeException(ex, domain)
		}
	}
	
	
	/**
	 * Raccourci vers user connecté
	 * 
	 * @return
	 */
	Object principal() {
		springSecurityService.getPrincipal()
	}
	
	
	/**
	 * Raccourci vers ID user connecté
	 * 
	 * @return
	 */
	Long principalId() {
		springSecurityService.getPrincipal()?.id
	}
}
