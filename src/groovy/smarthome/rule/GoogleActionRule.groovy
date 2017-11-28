package smarthome.rule

import smarthome.core.SmartHomeException;
import smarthome.security.User;
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

	private static final Map Verbes = [
		"ouvre": "ouvrir",
		"ferme": "fermer",
		"allume": "allumer",
		"éteins": "éteindre"	
	]
	
	Map parameters
	
	
	@Override
	GoogleActionResponse execute(GoogleActionRequest request) throws SmartHomeException {
		User user = parameters.user
		
		GoogleActionResponse response = new GoogleActionResponse()
		response.conversationToken = request.conversation.conversationToken
		
		// début discussion : message de bienvenue
		if (!request.inputs || request.inputs[0].intent == "actions.intent.MAIN") {
			response.askQuestion("Bonjour ${user.prenom}, que puis-je faire pour vous ?",
				["smarthome.automation.ACTIONS"])
		}
		// texte libre
		else if (request.inputs[0].intent == "actions.intent.TEXT" && request.inputs[0].rawInputs) {
			GoogleActionRawInput rawInput = request.inputs[0].rawInputs[0]
			def pattern = /(ouvre|ferme|allume|éteins)(\s?)(les|la|le)?(\s?)(.*)/
			def matcher = (rawInput.query =~ pattern)
			
			if (matcher.matches()) {
				def question = rawInput.query.replace(matcher[0][1], Verbes[(matcher[0][1])])
				response.askQuestion("Voulez-vous ${question} ?", ["smarthome.automation.ACTIONS"])
			} else {
				response.askQuestion("Désolé ${user.prenom}, je n'ai pas compris votre demande...", ["smarthome.automation.ACTIONS"])
			}
		}
		
		return response
	}
}
