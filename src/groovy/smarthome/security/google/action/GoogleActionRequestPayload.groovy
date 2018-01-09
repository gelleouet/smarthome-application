package smarthome.security.google.action

class GoogleActionRequestPayload {
	/**
	 * For action.devices.QUERY
	 */
	List<GoogleActionDevice> devices = []	
	
	/**
	 * For action.devices.EXECUTE
	 */
	List<GoogleActionRequestCommand> commands = []	
}
