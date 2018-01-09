package smarthome.security.google.action

class GoogleActionInput {
	/**
	 * Required. Possible values :
	 * 		action.devices.SYNC
	 * 		action.devices.QUERY
	 * 		action.devices.EXECUTE
	 */
	String intent
	
	GoogleActionRequestPayload payload
}
