package smarthome.rule

import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import smarthome.automation.Device;
import smarthome.automation.DeviceService;
import smarthome.core.SmartHomeException;
import smarthome.security.User;
import smarthome.security.UserConversation;
import smarthome.security.UserConversationService;
import smarthome.security.google.action.GoogleActionRawInput;
import smarthome.security.google.action.GoogleActionRequest;
import smarthome.security.google.action.GoogleActionResponse;



/**
 * Gestion d'une conversation
 * 
 * @author gregory
 *
 */
class GoogleActionRule implements Rule<GoogleActionRequest, GoogleActionResponse> {

	@Autowired
	UserConversationService userConversationService

	@Autowired
	DeviceService deviceService
	
	@Autowired
	MessageSource messageSource
	
	
	Map parameters
	Locale locale
	
	
	@Override
	GoogleActionResponse execute(GoogleActionRequest request) throws SmartHomeException {
		User user = parameters.user
		locale = new Locale(request.user.locale.substring(0, 2))
		
		GoogleActionResponse response = new GoogleActionResponse()
		response.conversationToken = request.conversation.conversationToken
		
		// texte libre
		if (request.inputs && request.inputs[0].rawInputs) {
			GoogleActionRawInput rawInput = request.inputs[0].rawInputs[0]
			String query = rawInput.query.toLowerCase()
			
			userConversationService.registerConversation(new UserConversation(user: user,
				conversationId: request.conversation.conversationId, query: query))
			
			def conversations = userConversationService.conversation(user, request.conversation.conversationId)
			def command = [response: response, request: request, user: user, query: query,
				conversations: conversations]
			
			for (Closure decision : decisionRules) {
				if (decision(command)) {
					return response
				}
			}
		}
		
		return response.askQuestion(i18nBundle("conversation.response.sorry", [user.prenom]))
	}
	
	
	/**
	 * Simplifie l'appel au traducteur à cause du tableau des params et locale
	 * 
	 * @param code
	 * @param params
	 * @param locale
	 * @return
	 */
	String i18nBundle(code, params = []) {
		return messageSource.getMessage(code, params as Object[], locale)
	}
	
	
	/**
	 * Toutes les règles de décision.
	 * Chacune doit renvoyer l'objet reponse à jour si elle prend en charge la query
	 */
	def decisionRules = [
		// début conversation => message de bienvenue
		{ command ->
			if (command.request.inputs[0].intent == "actions.intent.MAIN") {
				return command.response.askQuestion(i18nBundle("conversation.response.bienvenue", [command.user.prenom]))
			}
		},
	
		// message au revoir => fin de discussion
		{ command ->
			if (command.query == i18nBundle("conversation.request.query.aurevoir")) {
				return command.response.endConversation(i18nBundle("conversation.response.aurevoir", [command.user.prenom]))
			}
		},
	
		// action sur un device => message de confirmation
		{ command ->
			def matcher = (command.query =~ i18nBundle("conversation.request.query.pattern.action"))
			if (matcher.matches()) {
				def question = command.query.replace(matcher[0][1], i18nBundle("verbe.infinitif.${matcher[0][1]}"))
				return command.response.askQuestion(i18nBundle("conversation.response.confirm", [question]),
					[i18nBundle("conversation.oui"), i18nBundle("conversation.non")]
				)
			}
		},
	
		// confirmation oui => action sur le message précédent
		{ command ->
			if (command.query == i18nBundle("conversation.oui") && command.conversations.size() > 1) {
				// on récupère le message précédent
				def lastQuery = command.conversations[-2].query
				def matcher = (lastQuery =~ i18nBundle("conversation.request.query.pattern.action"))
				
				if (matcher.matches()) {
					Device device
					String action
					
					if (matcher[0][3] == i18nBundle("conversation.request.query.volets")) {
						device = Device.findByUserAndLabel(command.user, "Volets [Tous]")
						action = (matcher[0][1] == "ouvre" ? "open" : "close")
					} else if (matcher[0][3] == i18nBundle("conversation.request.query.portegarage"))	{
						device = Device.findByUserAndLabel(command.user, "Porte garage")
						action = "push"
					}
					
					if (action && device) {
						deviceService.execute(device, action, [:])
						return command.response.endConversation(i18nBundle("conversation.response.execute", [lastQuery]))
					}
				}
				
				return command.response.askQuestion(i18nBundle("conversation.response.error", [lastQuery]))
			}
		},
	
		// confirmation non => annulation discussion
		{ command ->
			if (command.query == i18nBundle("conversation.non")) {
				return command.response.endConversation(i18nBundle("conversation.response.cancel"))
			}
		},
	]
}
