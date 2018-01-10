package smarthome.security.google.action

class GoogleActionSyncResponsePayload extends GoogleActionResponsePayload {
	/**
	 * Reﬂects the unique (and immutable) user ID on the agent's platform. 
	 * The string is opaque to Google, so if there's an immutable form vs a mutable form on the agent side, 
	 * use the immutable form (e.g. an account number rather than email).
	 */
	String agentUserId
	
	
	/**
	 * Array of devices. 
	 * Zero or more devices are returned (zero devices meaning the user has no devices,
	 * or has disconnected them all)
	 */
	List<GoogleActionDevice> devices = []
}
