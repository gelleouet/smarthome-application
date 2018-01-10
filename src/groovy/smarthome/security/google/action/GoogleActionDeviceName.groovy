package smarthome.security.google.action


class GoogleActionDeviceName {
	/**
	 * Optional. List of names provided by the partner rather than the user,
	 * often manufacturer names, SKUs, etc.
	 */
	List<String> defaultNames = []
	
	
	/**
	 * Optional. Primary name of the device, generally provided by the user. 
	 * This is also the name the Assistant will prefer to describe the device in responses.
	 */
	String name
	
	
	/**
	 * Optional. Additional names provided by the user for the device.
	 */
	List<String> nicknames = []
}
