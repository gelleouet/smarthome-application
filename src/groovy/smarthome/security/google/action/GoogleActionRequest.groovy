package smarthome.security.google.action

import java.util.List;

class GoogleActionRequest {
	GoogleActionUser user = new GoogleActionUser()
	GoogleActionDevice device = new GoogleActionDevice()
	GoogleActionSurface surface = new GoogleActionSurface()
	List availableSurfaces = []
	GoogleActionConversation conversation = new GoogleActionConversation()
	List<GoogleActionInput> inputs = []
	boolean isInSandbox
}
