package smarthome.core.chart

import smarthome.automation.ChartTypeEnum
import smarthome.automation.ChartViewEnum
import smarthome.automation.DeviceChartCommand
import smarthome.automation.ProductionChartCommand
import smarthome.automation.SeriesTypeEnum
import smarthome.core.DateUtils
import smarthome.security.User

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class ProductionInvestissementChart extends GoogleChart {
	ProductionChartCommand command


	ProductionInvestissementChart(ProductionChartCommand command) {
		this.command = command
	}


	/**
	 * Chargement des données et init du chart
	 * 
	 * @return
	 */
	ProductionInvestissementChart build() {
		def allValues = []
		DeviceChartCommand deviceChartCommand = command.clone(DeviceChartCommand)

		colonnes << new GoogleDataTableCol(label: "Date", type: "datetime", property: "dateValue")
		colonnes << new GoogleDataTableCol(label: "Production globale", property: "total", type: "number")
		colonnes << new GoogleDataTableCol(label: "Ma part", property: "mapart", type: "number")

		command.actions.findAll { it.device }.each { action ->
			deviceChartCommand.device = action.device
			def percent = action.percentAction()
			allValues.addAll(action.device.newDeviceImpl().values(deviceChartCommand).collect { deviceValue ->
				[deviceValue: deviceValue, percent: percent]
			})
		}

		if (command.viewMode == ChartViewEnum.day) {
			allValues = allValues.groupBy {
				DateUtils.truncMinute5(it.deviceValue.dateValue)
			}

			chartType = ChartTypeEnum.Line.factory
			vAxis << [title: "Production (Wh)"]
			series << [color: '#f3d43d', type: SeriesTypeEnum.area.toString()]
			series << [color: '#3572b0', type: SeriesTypeEnum.line.toString()]
		} else {
			allValues = allValues.groupBy {
				it.deviceValue.dateValue
			}

			chartType = ChartTypeEnum.Column.factory
			vAxis << [title: "Production (kWh)"]
			series << [color: '#f3d43d', type: SeriesTypeEnum.bars.toString()]
			series << [color: '#3572b0', type: SeriesTypeEnum.bars.toString()]
		}

		values = allValues.collect { entry ->
			def result = [dateValue: entry.key]
			result.total = entry.value.sum { it.deviceValue.value }
			result.mapart = entry.value.sum { it.deviceValue.value * it.percent }
			return result
		}

		return this
	}
}
