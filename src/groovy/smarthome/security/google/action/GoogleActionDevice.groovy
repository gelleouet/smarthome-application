package smarthome.security.google.action

class GoogleActionDevice {
	/**
	 * Required. The ID of the device in the partner's cloud.
	 * This must be unique for the user and for the partner, as in cases of sharing we may use
	 * this to dedupe multiple views of the same device.
	 * It should be immutable for the device; if it changes, the Assistant will treat it as a new device.
	 */
	String id
	
	
	/**
	 * Required. The hardware type of device (for example, action.devices.types.LIGHT).
	 * See the full list of device types : https://developers.google.com/actions/smarthome/guides/
	 */
	String type
	
	
	/**
	 * Required. List of traits this device supports (for example, action.devices.traits.OnOff).
	 * This deﬁnes the commands, attributes, and states that the device has.
	 * See the full list of device traits https://developers.google.com/actions/smarthome/traits/
	 */
	List<String> traits = []
	
	
	/**
	 * Required. Names of this device. Note that while individual ﬁelds are optional,
	 * each device must have at least one name.
	 */
	GoogleActionDeviceName name = new GoogleActionDeviceName()
	
	
	/**
	 * Required. Indicates whether this device will have its states updated by the Real Time Feed.
	 * (TRUE to use the Real Time Feed for reporting state, and FALSE to use the polling model.)
	 */
	boolean willReportState
	
	
	/**
	 *  Optional. If the partner's cloud configuration includes placing devices in rooms, 
	 *  the name of the room can be provided here; 
	 *  the Assistant will attempt to align the room with those in the HomeGraph.
	 */
	String roomHint
	
	
	/**
	 * Optional. As roomHint, for structures that users set up in the partner's system.
	 * Structures are houses, buildings, etc -- containers for rooms.
	 */
	String structureHint
	
	
	/**
	 * Optional. Contains fields describing the device for use in one-off logic if needed 
	 * (e.g. 'broken firmware version X of light Y requires adjusting color', 
	 * or 'security ﬂaw requires notifying all users of firmware Z').
	 */
	GoogleActionDeviceInfo deviceInfo = new GoogleActionDeviceInfo()
	
	
	/**
	 * Optional, aligned with per-trait attributes as in Attributes below. 
	 * Right-hand values are string | int | boolean | number.
	 */
	Map attributes = [:]
	
	
	/**
	 * Optional. If the opaque customData object is provided in SYNC, it's sent here.
	 */
	Map customData = [:]	
}
