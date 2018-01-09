package smarthome.security.google.action

import java.util.List;

class GoogleActionRequest {
	/**
	 * Required. Id of request for ease of tracing
	 */
	String requestId
	
	List<GoogleActionInput> inputs = []
}
