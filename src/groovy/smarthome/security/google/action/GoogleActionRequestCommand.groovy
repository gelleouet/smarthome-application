package smarthome.security.google.action


class GoogleActionRequestCommand {
	/**
	 * Required.
	 */
	List<GoogleActionDevice> devices = []
	
	
	/**
	 * Required. The ordered list of execution commands for the attached ids.
	 */
	List<GoogleActionExecution> execution = []
}
