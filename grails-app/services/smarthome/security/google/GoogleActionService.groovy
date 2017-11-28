package smarthome.security.google

import grails.plugin.springsecurity.SpringSecurityService;

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.SmartHomeException;
import smarthome.rule.GoogleActionRuleService;
import smarthome.security.User;
import smarthome.security.UserApplication;
import smarthome.security.google.action.GoogleActionRequest;
import smarthome.security.google.action.GoogleActionResponse;


class GoogleActionService extends AbstractService {

	GrailsApplication grailsApplication
	GoogleActionRuleService googleActionRuleService
	SpringSecurityService springSecurityService
	
	
	/**
	 * Enregistrement d'une nouvelle application pour un utilisateur
	 * 
	 * @param command
	 * @param user
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	UserApplication auth(GoogleActionAuthCommand command, User user) throws SmartHomeException {
		// Vérifie les infos de connexion
		if (command.client_id != grailsApplication.config.google.action.clientId) {
			throw new SmartHomeException("clientId not valid !")
		}
		
		command.applicationId = grailsApplication.config.google.action.applicationId
		
		if (command.redirect_uri != "${grailsApplication.config.google.action.redirectUri}${grailsApplication.config.google.action.applicationId}") {
			throw new SmartHomeException("redirectUri or projectId not valid !")
		}
		
		UserApplication userApp = UserApplication.findByUserAndApplicationId(user,
			command.applicationId)
		
		if (!userApp) {
			userApp = new UserApplication(user: user, applicationId: command.applicationId)
		}
		
		// rafraichit le token à chaque auth
		userApp.publicToken = UUID.randomUUID().toString().bytes.encodeBase64().toString()
		userApp.token = userApp.publicToken //springSecurityService.encodePassword(userApp.publicToken)
		
		return this.save(userApp)
	}
	
	
	/**
	 * Gestion d'une conversation avec un assistant
	 * L'utilisateur doit être connecté pendant toute la conversation
	 * 
	 * @param request
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	GoogleActionResponse conversation(GoogleActionRequest request, String token) throws SmartHomeException {
		if (!request.user?.accessToken) {
			throw new SmartHomeException("accessToken is empty !")
		}
		
		String userToken = request.user.accessToken //springSecurityService.encodePassword(request.user.accessToken)
		UserApplication userApp = UserApplication.findByToken(userToken)
		
		if (!userApp) {
			throw new SmartHomeException("accessToken not found !")
		}
		
		return googleActionRuleService.execute(request, true, [user: userApp.user])
	}
}
