package smarthome.automation.deviceType

import smarthome.automation.ChartViewEnum
import smarthome.automation.DeviceChartCommand
import smarthome.automation.DeviceValue
import smarthome.automation.DeviceValueDay
import smarthome.automation.DeviceValueMonth
import smarthome.automation.SeriesTypeEnum
import smarthome.automation.WorkflowContext
import smarthome.automation.WorkflowEvent
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException
import smarthome.core.chart.GoogleChart
import smarthome.core.chart.GoogleDataTableCol

/**
 * Capteur générique
 * 
 * @author gregory
 *
 */
class PanneauSolaire extends AbstractDeviceType {
	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#values()
	 */
	@Override
	List values(DeviceChartCommand command) throws SmartHomeException {
		def values = []

		if (command.viewMode == ChartViewEnum.day) {
			values = DeviceValue.values(command.device, command.dateDebut(), command.dateFin())
		} else if (command.viewMode == ChartViewEnum.month) {
			values = DeviceValueDay.values(command.device, command.dateDebut(), command.dateFin())
		} else if (command.viewMode == ChartViewEnum.year) {
			values = DeviceValueMonth.values(command.device, command.dateDebut(), command.dateFin())
		}

		return values
	}

	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#aggregateValueDay(java.util.Date)
	 */
	@Override
	List aggregateValueDay(Date dateReference) {
		// calcule la conso du jour en repartant des index
		def values = DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('day', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			(sum(deviceValue.value) / 1000) as sum)
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			AND deviceValue.name is null
			GROUP BY deviceValue.name, date_trunc('day', deviceValue.dateValue)""", [device: device,
					dateDebut: DateUtils.firstTimeInDay(dateReference), dateFin: DateUtils.lastTimeInDay(dateReference)])

		return values
	}



	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#aggregateValueMonth(java.util.Date)
	 */
	@Override
	List aggregateValueMonth(Date dateReference) {
		// calcule la conso du mois en repartant des index
		def values = DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('month', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			(sum(deviceValue.value) / 1000) as sum)
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			AND deviceValue.name is null
			GROUP BY deviceValue.name, date_trunc('month', deviceValue.dateValue)""", [device: device,
					dateDebut: DateUtils.firstDayInMonth(dateReference), dateFin: DateUtils.lastTimeInDay(DateUtils.lastDayInMonth(dateReference))])

		return values
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see smarthome.automation.deviceType.AbstractDeviceType#googleChart(smarthome.automation.DeviceChartCommand, java.util.List)
	 */
	@Override
	GoogleChart googleChart(DeviceChartCommand command, List values) {
		GoogleChart chart = new GoogleChart()
		command.device.extrasToJson()
		chart.values = values

		if (command.viewMode == ChartViewEnum.day) {
			chart.colonnes = [
				new GoogleDataTableCol(label: "Date", type: "datetime", property: "dateValue"),
				new GoogleDataTableCol(label: "Production (Wh)", property: "value", type: "number")
			]
			chart.vAxis << [title: "Production (Wh)"]
			// série par défaut en bleu
			chart.series << [color: '#3572b0', type: SeriesTypeEnum.area.toString()]
		} else {
			chart.colonnes = [
				new GoogleDataTableCol(label: "Date", type: "datetime", property: "dateValue"),
				new GoogleDataTableCol(label: "Production (kWh)", property: "value", type: "number")
			]
			chart.vAxis << [title: "Production (kWh)"]
			// série par défaut en bleu
			chart.series << [color: '#3572b0', type: SeriesTypeEnum.bars.toString()]
		}

		return chart
	}


	/**
	 * La production totale sur une année en kWh
	 * 
	 * @param annee
	 * @return
	 */
	Double productionTotalAnnee(int annee) {
		def jourAnnee = new Date().copyWith([year: annee])

		def results = DeviceValueMonth.createCriteria().list() {
			eq 'device', device
			between 'dateValue', DateUtils.firstDayInYear(jourAnnee), DateUtils.lastDayInYear(jourAnnee)
			eq 'name', 'sum'
			projections {
				sum 'value'
			}
		}

		return results && results[0] ? results[0] : 0.0
	}
}
