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
import grails.transaction.NotTransactional;

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
	
	/**
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
}
