package smarthome.security.google.action

class GoogleActionResponsePayload {
	/** Optional. An error code for the entire transaction -- for auth failures and partner system unavailability.
	 * For individual device errors use the errorCode within the device object.
	 * 
	 * authExpired: Credentials have expired.
	 * authFailure: General failure to authenticate.
	 * deviceOffline: The target is unreachable.
	 * timeout: Internal timeout.
	 * deviceTurnedOff: The device is known to be turned hard off (if distinguishable from unreachable).
	 * deviceNotFound: The device doesn't exist on the partner's side. This normally indicates a failure in data synchronization or a race condition.
	 * valueOutOfRange: The range in parameters is out of bounds.
	 * notSupported: The command or its parameters are unsupported (this should generally not happen, as traits and business logic should prevent it).
	 * protocolError: Failure in processing the request.
	 * unknownError: Everything else, although anything that throws this should be replaced with a real error code.
	 * 
	 */
	String errorCode
	
	
	/**
	 * Optional. Detailed error which will never be presented to users but may be logged or used during development.
	 */
	String debugString
}
