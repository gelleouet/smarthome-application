package smarthome.security.google.action

class GoogleActionQueryResponsePayload extends GoogleActionResponsePayload {
	/**
	 * Map of devices. Each property has the following name and value:
	 * 
	 * <id>: Object. Required. Maps partner device ID to object of state properties, 
	 * 		as defined in States section below.
	 * errorCode: String. Optional. Expanding ERROR state if needed from the preset error codes,
	 * 		which will map to the errors presented to users.
	 * debugString: string. Optional. Detailed error which will never be presented to users 
	 * 		but may be logged or used during development.
	 */
	Map devices = [:]
}
