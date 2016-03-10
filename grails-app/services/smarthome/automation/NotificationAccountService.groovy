package smarthome.automation

import java.util.ServiceLoader;

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;

import org.codehaus.groovy.grails.web.mapping.LinkGenerator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import smarthome.automation.notification.Notification;
import smarthome.automation.notification.NotificationSender;
import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.ClassUtils;
import smarthome.core.ExchangeType;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class NotificationAccountService extends AbstractService {

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
	 * Envoi d'une notification
	 * 
	 * @throws SmartHomeException
	 */
	void sendNotification(Notification notification) throws SmartHomeException {
		log.info "Send notification $notification.type to $notification.user.username..."
		
		// recherche compte sur
		def account = NotificationAccount.findByUserAndType(notification.user, notification.type)
		
		if (account) {
			NotificationSender sender = account.senderInstance
			
			if (account.config) {
				sender.config = JSON.parse(account.config)
			}
			
			sender.send(notification)
		}
	}
}
