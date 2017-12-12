package smarthome.automation

import smarthome.automation.deviceType.AbstractDeviceType;
import grails.validation.Validateable;


@Validateable
class DeviceChartCommand extends AbstractChartCommand<DeviceChartCommand> {
	Device device
	AbstractDeviceType deviceImpl
	String metaName
	
	
	static constraints = {
		deviceImpl nullable: true
		metaName nullable: true
		navigation nullable: true
		dateDebutUser nullable: true
	}
	
	
	@Override
	DeviceChartCommand cloneForLastYear() {
		DeviceChartCommand command = super.cloneForLastYear()
		command.deviceImpl = deviceImpl
		command.device = device
		command.metaName = metaName
		return command
	}
}
