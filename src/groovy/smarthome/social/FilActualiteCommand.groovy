package smarthome.social

import smarthome.automation.Device;
import smarthome.automation.DeviceValue;
import grails.validation.Validateable;

@Validateable
class FilActualiteCommand {

	List<Device> userDevices = []
	List<Device> sharedDevices = []
	List<DeviceValue> values = []
	
	static constraints = {
		
	}
}
