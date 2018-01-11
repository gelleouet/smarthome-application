package smarthome.security.google.action

import smarthome.automation.Device;

class GoogleActionExecuteResponsePayload extends GoogleActionResponsePayload {
	/**
	 *  Required. Each object contains one or more devices with response details. N.B. 
	 *  These may not be grouped the same way as in the request. 
	 *  For example, the request might turn 7 lights on, with 3 lights succeeding and 4 failing, 
	 *  thus with two groups in the response.
	 */
	List<GoogleActionResponseCommand> commands = []
	
	
	/**
	 * Ajout du résultat d'une commande
	 * Les commandes sont organisées par status
	 * 
	 * @param status
	 * @param deviceId
	 * @return
	 */
	GoogleActionResponseCommand addCommand(String status, String deviceId) {
		// recherche d'une commande avec le même status
		GoogleActionResponseCommand command = commands.find { it.status == status }
		
		if (!command) {
			command = new GoogleActionResponseCommand(status: status)
			commands << command
		}
		
		command.ids << deviceId
		
		return command
	}
}
