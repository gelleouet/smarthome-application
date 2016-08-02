package smarthome.automation

import java.util.Map;
import java.util.ServiceLoader;

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;

import org.codehaus.groovy.grails.web.mapping.LinkGenerator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import smarthome.automation.notification.EmailNotificationSender;
import smarthome.automation.notification.Notification;
import smarthome.automation.notification.NotificationAccountEnum;
import smarthome.automation.notification.NotificationSender;
import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.ClassUtils;
import smarthome.core.ExchangeType;
import smarthome.core.ScriptUtils;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class NotificationAccountService extends AbstractService {

	
	/**
	 * Edition ACL
	 *
	 * @param workflow
	 * @return
	 */
	@PreAuthorize("hasPermission(#notificationAccount, 'OWNER')")
	NotificationAccount edit(NotificationAccount notificationAccount) {
		return notificationAccount
	}
	
	
	/**
	 * Liste des sender de notifications
	 * 
	 * @return
	 */
	List<NotificationSender> listNotificationSender() {
		def senders = []
		
		ServiceLoader.load(NotificationSender).each {
			senders << it
		}
		
		return senders
	}
	
	
	/**
	 * Envoi des notifications lors du déchenchement d'un event
	 *
	 * @param deviceEvent
	 * @param context
	 */
	void sendDeviceEventNotifications(DeviceEvent deviceEvent) throws SmartHomeException {
		if (!deviceEvent.attached) {
			deviceEvent.attach()
		}
		
		def notifications = DeviceEventNotification.createCriteria().list {
			eq 'deviceEvent', deviceEvent
			join 'deviceEvent'
			join 'deviceEvent.device'
		}
		
		def user = deviceEvent.user
		def context = [deviceEvent: deviceEvent, device: deviceEvent.device]
		
		notifications.each { notification ->
			def message
			
			// construction du message
			if (notification.message) {
				if (notification.script) {
					// Utiliser une transaction séparée en lecture seule pour éviter des abus
					DeviceEvent.withTransaction([propagationBehavior: TransactionDefinition.PROPAGATION_REQUIRES_NEW, readOnly: true]) {
						message = ScriptUtils.runScript(notification.message, context)?.toString()
					}
				} else {
					message = notification.message
				}
			}
			
			if (!message) {
				message = "NOTIFICATION SMARTHOME\rDevice : ${notification.deviceEvent.device.label}\rValeur : ${notification.deviceEvent.device.value}"
			}
			
			sendNotification(new Notification(message: message, type: notification.type, user: deviceEvent.user))
		}
	}
	
	
	/**
	 * Envoi d'une notification
	 * 
	 * @throws SmartHomeException
	 */
	void sendNotification(Notification notification) throws SmartHomeException {
		log.info "Send notification $notification.type to $notification.user.username : $notification.message"
		
		NotificationSender sender
		
		// besoin d'un compte pour l'envoi des sms
		if (notification.type == NotificationAccountEnum.sms) {
			def account = NotificationAccount.findByUserAndType(notification.user, notification.type)
			sender = account.senderInstance
			if (account.config) {
				sender.config = JSON.parse(account.config)
			}
		} else if (notification.type == NotificationAccountEnum.mail) {
			sender = new EmailNotificationSender()			
		}
		
		if (sender) {
			// injecte un service pour l'accès au système AMQP et transaction
			sender.config.notificationAccountService = this
			sender.send(notification)
		}
	}
}
