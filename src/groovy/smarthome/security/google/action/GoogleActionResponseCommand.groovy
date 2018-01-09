package smarthome.security.google.action


class GoogleActionResponseCommand {
	/**
	 * Required. Partner device IDs of the response
	 */
	List<String> ids = []
	
	/**
	 * Required. Current status types:
	 * SUCCESS - confirmed that the command(s) has/have succeeded.
	 * PENDING - commands are enqueued but expected to succeed.
	 * OFFLINE - target devices(s) in offline state or unreachable.
	 * ERROR - unable to perform the commands.
	 */
	String status
	
	/**
	 * Optional. Expanding ERROR state if needed from the preset error codes, 
	 * which will map to the errors presented to users.
	 */
	String errorCode
	
	/**
	 *  Optional, but aligned with per-trait states as in Attributes below.
	 *  These are the states _after_ execution, if available.
	 */
	Map states = [:]
}
