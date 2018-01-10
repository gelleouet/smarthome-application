package smarthome.security.google.action


class GoogleActionDeviceInfo {
	/**
	 *  Especially useful when the partner is a hub for other devices. 
	 *  Google may provide a standard list of manufacturers here so that e.g. 
	 *  TP-Link and Smartthings both describe 'osram' the same way.
	 */
	String manufacturer
	
	
	/**
	 * The model or SKU identifier of the particular device
	 */
	String model
	
	
	/**
	 * Specific version number attached to the hardware if available.
	 */
	String hwVersion
	
	
	/**
	 * Specific version number attached to the software/firmware, if available.
	 */
	String swVersion
}
