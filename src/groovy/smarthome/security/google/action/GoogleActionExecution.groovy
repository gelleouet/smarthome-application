package smarthome.security.google.action


class GoogleActionExecution {
	/**
	 * Required. The command (see below) to execute, with (usually) accompanying parameters.
	 */
	String command
	
	/**
	 * Optional, but aligned with the parameters for each command (below).
	 */
	Map params = [:]
}
