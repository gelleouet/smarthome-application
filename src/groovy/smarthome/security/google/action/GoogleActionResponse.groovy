package smarthome.security.google.action

class GoogleActionResponse {
	/**
	 * Required. Id of request for ease of tracing
	 */
	String requestId
	
	GoogleActionResponsePayload payload = new GoogleActionResponsePayload()
	
	
	/**
	 * Injecte un message d'erreur
	 * 
	 * @param errorCode
	 * @return
	 */
	GoogleActionResponse error(String errorCode) {
		payload.errorCode = errorCode
		return this
	}
	
	
	/**
	 * Injecte le payload pour la response Sync
	 * 
	 * @return
	 */
	GoogleActionSyncResponsePayload setSyncPayload() {
		payload = new GoogleActionSyncResponsePayload()
		return payload	
	}
	
	
	/**
	 * Injecte le payload pour la response Query
	 * 
	 * @return
	 */
	GoogleActionQueryResponsePayload setQueryPayload() {
		payload = new GoogleActionQueryResponsePayload()
		return payload	
	}
}
