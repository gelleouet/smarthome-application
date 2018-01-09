package smarthome.security.google.action

class GoogleActionDevice {
	/**
	 * Required. Partner ID to query, as per the id provided in SYNC
	 */
	String id
	
	/**
	 * Optional. If the opaque customData object is provided in SYNC, it's sent here.
	 */
	Map customData = [:]	
}
