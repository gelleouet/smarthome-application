package smarthome.core.chart

import smarthome.automation.ChartTypeEnum
import smarthome.automation.ChartViewEnum
import smarthome.automation.DeviceChartCommand
import smarthome.automation.ProducteurEnergieAction;
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
	List<ProductionInvestissementChart> actionCharts = []


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
		def unite
		DeviceChartCommand deviceChartCommand = command.clone(DeviceChartCommand)
		
		title = "Production globale"
		
		command.actions.findAll { it.device }.each { action ->
			deviceChartCommand.device = action.device
			def percent = action.percentAction()
			def actionValues = action.device.newDeviceImpl().values(deviceChartCommand)
			
			actionCharts << buildActionChart(action, percent, actionValues)
			
			allValues.addAll(actionValues.collect { deviceValue ->
				[deviceValue: deviceValue, percent: percent]
			})
		}

		if (command.viewMode == ChartViewEnum.day) {
			allValues = allValues.groupBy {
				DateUtils.truncMinute5(it.deviceValue.dateValue)
			}

			unite = "Wh"
			chartType = ChartTypeEnum.Line.factory
			series << [color: '#f3d43d', type: SeriesTypeEnum.area.toString()]
			series << [color: '#3572b0', type: SeriesTypeEnum.line.toString()]
		} else {
			allValues = allValues.groupBy {
				it.deviceValue.dateValue
			}

			unite = "kWh"
			chartType = ChartTypeEnum.Column.factory
			series << [color: '#f3d43d', type: SeriesTypeEnum.bars.toString()]
			series << [color: '#3572b0', type: SeriesTypeEnum.bars.toString()]
		}

		colonnes << new GoogleDataTableCol(label: "Date", type: "datetime", property: "dateValue")
		colonnes << new GoogleDataTableCol(label: "Production ($unite)", property: "total", type: "number")
		colonnes << new GoogleDataTableCol(label: "Ma part ($unite)", property: "mapart", type: "number")
		
		vAxis << [title: "Production ($unite)"]
		
		values = allValues.collect { entry ->
			def result = [dateValue: entry.key]
			result.total = entry.value.sum { it.deviceValue.value }
			result.mapart = entry.value.sum { it.deviceValue.value * it.percent }
			return result
		}

		return this
	}
	
	
	/**
	 * 
	 * @return
	 */
	private ProductionInvestissementChart buildActionChart(ProducteurEnergieAction action, def percent, def values) {
		ProductionInvestissementChart actionChart = new ProductionInvestissementChart(command)
		def unite
		actionChart.title = "Production ${action.producteur.libelle}"
		
		if (command.viewMode == ChartViewEnum.day) {
			unite = "Wh"
			actionChart.chartType = ChartTypeEnum.Line.factory
			actionChart.series << [color: '#f3d43d', type: SeriesTypeEnum.area.toString()]
			actionChart.series << [color: '#3572b0', type: SeriesTypeEnum.line.toString()]
		} else {
			unite = "kWh"
			actionChart.chartType = ChartTypeEnum.Column.factory
			actionChart.series << [color: '#f3d43d', type: SeriesTypeEnum.bars.toString()]
			actionChart.series << [color: '#3572b0', type: SeriesTypeEnum.bars.toString()]
		}
		
		actionChart.colonnes << new GoogleDataTableCol(label: "Date", type: "datetime", property: "dateValue")
		actionChart.colonnes << new GoogleDataTableCol(label: "Production ($unite)", property: "total", type: "number")
		actionChart.colonnes << new GoogleDataTableCol(label: "Ma part ($unite)", property: "mapart", type: "number")
		
		actionChart.vAxis << [title: "Production ${action.producteur.libelle} ($unite)"]
		
		actionChart.values = values.collect { deviceValue ->
			[dateValue: deviceValue.dateValue, total: deviceValue.value, mapart: deviceValue.value*percent]
		}
		
		return actionChart
	}
}
