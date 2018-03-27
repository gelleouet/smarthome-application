package smarthome.automation

import smarthome.automation.deviceType.AbstractDeviceType;
import grails.validation.Validateable;


@Validateable
class DeviceChartCommand extends AbstractChartCommand<DeviceChartCommand> {
	Device device
	AbstractDeviceType deviceImpl
	List<Device> compareDevices = []
	List<List> compareValues = []
	
	
	static constraints = {
		deviceImpl nullable: true
		navigation nullable: true
		dateDebutUser nullable: true
	}
	
	
	@Override
	void navigation() {
		// pas de chart à la journée si plusieurs devices car les heures risquent de ne pas correspondre
		// et puis trop de données à gérer
		if (compareDevices && viewMode == ChartViewEnum.day) {
			viewMode = ChartViewEnum.month	
		}
		
		super.navigation()
	}


	@Override
	DeviceChartCommand cloneForLastYear() {
		DeviceChartCommand command = super.cloneForLastYear()
		command.deviceImpl = deviceImpl
		command.device = device
		return command
	}
	
	
	@Override
	DeviceChartCommand clone() {
		DeviceChartCommand command = super.clone()
		command.deviceImpl = deviceImpl
		command.device = device
		return command
	}
}
