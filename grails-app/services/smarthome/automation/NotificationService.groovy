package smarthome.automation

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.annotation.Transactional

import smarthome.automation.notification.NotificationSender
import smarthome.core.AbstractService
import smarthome.core.ApplicationUtils
import smarthome.core.ExchangeType
import smarthome.core.QueryUtils
import smarthome.core.ScriptUtils
import smarthome.core.SmartHomeCoreConstantes
import smarthome.core.SmartHomeException
import smarthome.security.User


class NotificationService extends AbstractService {

	GrailsApplication grailsApplication
	LinkGenerator grailsLinkGenerator


	/**
	 * Recherche paginée
	 * 
	 * @param command
	 * @param pagination
	 * @return
	 * @throws SmartHomeException
	 */
	List<Notification> search(NotificationCommand command, Map pagination) throws SmartHomeException {
		return Notification.createCriteria().list(pagination) {
			eq 'user', command.user
			if (command.description) {
				ilike 'description', QueryUtils.decorateMatchAll(command.description)
			}
			join 'notificationAccount'
			join 'notificationAccount.notificationAccountSender'
			order 'description'
		}
	}


	/**
	 * Liste les notifications de l'utilisateur
	 * 
	 * @param user
	 * @return
	 */
	List<Notification> listForUser(User user) {
		return search(new NotificationCommand(user: user), [:])
	}


	/**
	 * Edition ACL
	 *
	 * @param workflow
	 * @return
	 */
	@PreAuthorize("hasPermission(#notification, 'OWNER')")
	Notification edit(Notification notification) {
		return notification
	}


	/**
	 * Exécution d'une notification dans un contexte donné
	 * 
	 * @param notification
	 * @param context
	 * @return
	 * @throws SmartHomeException
	 */
	Notification execute(Notification notification, Map context) throws SmartHomeException {
		if (!notification.attached) {
			notification.attach()
		}

		// vérif si autorisation pour utiliser le service d'envoi
		notification.notificationAccount.assertAutorisation()

		// Prépare les infos JSON
		notification.parametersToJson()
		notification.notificationAccount.configToJson()

		// Calculer le message à partir du script
		// Utiliser une transaction séparée en lecture seule pour éviter des abus
		Notification.withTransaction([propagationBehavior: TransactionDefinition.PROPAGATION_REQUIRES_NEW, readOnly: true]) {
			notification.convertMessage = ScriptUtils.runScript(notification.message, context)?.toString()
		}

		// Instancie le sender pour l'envoi de la notification avec injection des dépendances
		NotificationSender notificationSender = notification.notificationAccount.notificationAccountSender.newNotificationSender()
		ApplicationUtils.autowireBean(notificationSender)
		notificationSender.send(notification, context)

		return notification
	}


	/**
	 * Exécution sur un contexte test
	 * 
	 * @param notification
	 * @return
	 * @throws SmartHomeException
	 */
	Notification executeTest(Notification notification) throws SmartHomeException {
		Event event = new Event(libelle: "Event_Label")
		Device device = new Device(label: "Device_Label", value: "device_value", dateValue: new Date())
		DeviceAlert alert = new DeviceAlert(device: device, level: LevelAlertEnum.error, dateDebut: new Date())
		DeviceLevelAlert alertLevel = new DeviceLevelAlert(device: device, level: alert.level, mode: ModeAlertEnum.max,
		value: 0d)

		return execute(notification, [device: device, alert: alert, alertLevel: alertLevel, event: event])
	}


	/**
	 * Envoi asynchrone d'un email
	 * Méthode utilitaire pour rediriger vers le bus AMQP
	 * 
	 * @param email
	 */
	void sendEmail(Map email) {
		// inject des infos sur l'appli
		email.contextPath = grailsLinkGenerator.serverBaseURL
		email.appCode = grailsApplication.metadata['app.code']
		email.appVersion = grailsApplication.metadata['app.version']
		
		email.appTwitter = grailsApplication.config.social.twitter
		email.appFacebook = grailsApplication.config.social.facebook
		email.appInstagram = grailsApplication.config.social.instagram
		email.appWeb = grailsApplication.config.social.web
		

		// Envoi des messages sur une instance applicative identique !!
		// suffixe avec le nom de l'application
		asyncSendMessage(SmartHomeCoreConstantes.DIRECT_EXCHANGE,
				SmartHomeCoreConstantes.EMAIL_QUEUE + "." + grailsApplication.metadata['app.name'],
				email, ExchangeType.DIRECT)
	}
}
