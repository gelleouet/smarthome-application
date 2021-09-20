package smarthome.automation

import java.io.Serializable
import java.util.List
import java.util.Map
import grails.converters.JSON
import grails.transaction.NotTransactional

import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

import smarthome.automation.datasource.AbstractDataSourceProvider
import smarthome.automation.notification.EmailNotificationSender
import smarthome.automation.notification.NotificationSender
import smarthome.core.AbstractService
import smarthome.core.AsynchronousMessage
import smarthome.core.ExchangeType
import smarthome.core.QueryUtils
import smarthome.core.ScriptUtils
import smarthome.core.SmartHomeException
import smarthome.security.User


class NotificationAccountService extends AbstractService {



	/**
	 * Exécution type provider
	 * 
	 * @param notificationAccount
	 * @return
	 * @throws SmartHomeException
	 */
	@NotTransactional
	void execute(NotificationAccount notificationAccount) throws SmartHomeException {
		def providerImpl = notificationAccount.notificationAccountSender.newNotificationSender()

		if (providerImpl instanceof AbstractDataSourceProvider) {
			providerImpl.execute(notificationAccount)
		} else {
			throw new SmartHomeException("Not a provider !")
		}
	}


	/**
	 * Enregistrement
	 *
	 * @param domain
	 *
	 * @return domain
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	NotificationAccount save(NotificationAccount notificationAccount) throws SmartHomeException {
		// vérif du rôle sur sender
		notificationAccount.assertAutorisation()

		if (!notificationAccount.save()) {
			throw new SmartHomeException("Erreur enregistrement notificationAccount", notificationAccount)
		}

		return notificationAccount
	}


	/**
	 * Recherche paginée
	 * 
	 * @return
	 */
	List<NotificationAccount> search(NotificationAccountCommand command, Map pagination) {
		return NotificationAccount.createCriteria().list(pagination) {
			eq 'user', command.user
			notificationAccountSender {
				if (command.libelle) {
					ilike 'libelle', QueryUtils.decorateMatchAll(command.libelle)
				}
				order 'libelle'
			}
		}
	}


	/**
	 * Les services de l'utilisateur
	 * 
	 * @param user
	 * @return
	 */
	List<NotificationAccount> listForUser(User user) {
		return search(new NotificationAccountCommand(user: user), [:])
	}


	NotificationAccount findByUserAndSender(User user, NotificationAccountSender sender) {
		return NotificationAccount.createCriteria().get {
			eq 'user', user
			eq 'notificationAccountSender', sender
		}
	}


	NotificationAccount findByUserAndLibelleSender(User user, String senderLibelle) {
		return NotificationAccount.createCriteria().get {
			eq 'user', user
			notificationAccountSender {
				eq 'libelle', senderLibelle
			}
		}
	}


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
	 * Flag d'une exécution avec ou sans erreur
	 * 
	 * @param notificationAccount
	 * @param error
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	NotificationAccount flagExecution(NotificationAccount notificationAccount, String error) throws SmartHomeException {
		if (!error) {
			notificationAccount.lastExecution = new Date()
		}
		notificationAccount.error = error
		return super.save(notificationAccount)
	} 


	/**
	 * Envoi des notifications lors du déchenchement d'un event
	 *
	 * @param deviceEvent
	 * @param context
	 */
	void sendDeviceEventNotifications(Event deviceEvent) throws SmartHomeException {
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
					Event.withTransaction([propagationBehavior: TransactionDefinition.PROPAGATION_REQUIRES_NEW, readOnly: true]) {
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


	/**
	 * Compte les comptes avec cron associé
	 * 
	 * @return
	 */
	long countWithCron() {
		return NotificationAccount.createCriteria().get {
			notificationAccountSender {
				isNotNull 'cron'
			}
			projections {
				count("id")
			}
		}
	}


	/**
	 * 
	 * @param pagination
	 * @return
	 */
	List<Map> listIdsWithCron(Map pagination) {
		return NotificationAccount.createCriteria().list(pagination) {
			notificationAccountSender {
				isNotNull 'cron'
			}
			projections {
				property "id", "id"
			}
			order "id"
			// transformer pour récupérer une map au lieu d'un tableau
			resultTransformer org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP
		}
	}


	NotificationAccount findById(Serializable id) {
		return NotificationAccount.createCriteria().get {
			join 'notificationAccountSender'
			join 'user'
			idEq id
		}
	}
}
