
package smarthome.automation

import grails.converters.JSON;

import org.springframework.security.access.annotation.Secured;

import smarthome.automation.notification.NotificationSender;
import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.QueryUtils;
import smarthome.core.SmartHomeException;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;
import smarthome.security.User;



@Secured("isAuthenticated()")
class NotificationAccountController extends AbstractController {

	NotificationAccountService notificationAccountService
	
	
	/**
	 * dialogue ajout/modif notification compte
	 * 
	 * @param notification
	 * @return
	 */
	def dialogNotificationAccount(NotificationAccount notificationAccount, String typeNotification) {
		notificationAccountService.edit(notificationAccount)
		render(view: 'dialogNotificationAccount', model: [notificationAccount: notificationAccount,
			notificationSenders: notificationAccountService.listNotificationSender(),
			typeNotification: typeNotification])
	}
	
	
	
	/**
	 * Rendu du 
	 * @param notificationAccount
	 * @return
	 */
	def formTemplateNotificationSender(NotificationAccount notificationAccount, String className) {
		if (className && !notificationAccount.className) {
			notificationAccount.className = className
		}
		
		if (notificationAccount.className) {
			NotificationSender sender = notificationAccount.senderInstance
			
			if (notificationAccount.config) {
				notificationAccount.jsonConfig = JSON.parse(notificationAccount.config)
			}
			
			render(template: "sender/${sender.simpleName}", model: [notificationAccount: notificationAccount])	
		} else {
			nop()
		}
	}
	
	
	/**
	 * Enregistrement account
	 * 
	 * @param notificationAccount
	 * @return
	 */
	def save(NotificationAccount notificationAccount) {
		// la config est bindée en map, faut la transformer
		notificationAccount.config = notificationAccount.jsonConfig as JSON
		notificationAccount.user = User.read(principal.id)
		notificationAccountService.save(notificationAccount)
		
		redirect (action: 'profil', controller: 'user')
	}
}
