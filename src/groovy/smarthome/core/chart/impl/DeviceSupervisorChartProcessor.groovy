package smarthome.core.chart.impl

import java.util.Map

import org.springframework.beans.factory.annotation.Autowired;

import smarthome.automation.ChartViewEnum;
import smarthome.automation.Device;
import smarthome.automation.DeviceChartCommand;
import smarthome.automation.DeviceValue;
import smarthome.automation.DeviceValueService;
import smarthome.core.chart.GoogleChart;
import smarthome.core.chart.GoogleChartProcessor;
import smarthome.core.chart.GoogleDataTableCol;


/**
 * Affichage sur le graphe l'état d'un device supervisor.
 * Ce device prend généralement l'état on/off pour indiquer si mise en route ou pas
 * 
 * @author Gregory
 *
 */
class DeviceSupervisorChartProcessor implements GoogleChartProcessor {

	@Autowired
	DeviceValueService deviceValueService
	
	
	@Override
	GoogleChart process(GoogleChart chart, Map params) {
		DeviceChartCommand command = params.command
		
		if (command.viewMode == ChartViewEnum.day && command.device.extrasJson.deviceSupervisor && chart.values) {
			Device deviceSupervisor = Device.findByUserAndLabel(command.device.user,
				command.device.extrasJson.deviceSupervisor)
			
			if (deviceSupervisor) {
				chart.joinChart = new GoogleChart()
				chart.joinChart.values = deviceValueService.values(new DeviceChartCommand(device: deviceSupervisor,
					dateChart: command.dateChart, viewMode: command.viewMode))
				
				chart.joinChart.colonnes = [
					new GoogleDataTableCol(label: "Date", property: "dateValue", type: "datetime"),
					new GoogleDataTableCol(label: deviceSupervisor.label, property: "value", type: "number")
				]
				
				chart.series << [color: '#815b3a', type: 'area', axisIndex: 1]
				
				chart.vAxis << [title: 'Valeur']
				chart.vAxis << [title: 'Etat', maxValue: 2]
			}
		}
		
		return chart
	}

}
