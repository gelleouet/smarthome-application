package smarthome.automation.deviceType


import smarthome.automation.ChartTypeEnum
import smarthome.automation.ChartViewEnum
import smarthome.automation.DeviceChartCommand
import smarthome.automation.DeviceValue
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException
import smarthome.core.chart.GoogleChart
import smarthome.core.chart.GoogleDataTableCol


/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class AezeoBallonSolaire extends AbstractDeviceType {

	static final String TEMPERATURE_HAUT_NAME = "Temperature_haut"
	static final String TEMPERATURE_MILIEU_NAME = "Temperature_milieu"
	static final String TEMPERATURE_BAS_NAME = "Temperature_bas"
	
	static final String COLOR_HAUT = "#ff0000"
	static final String COLOR_MILIEU = "#FF9D00"
	static final String COLOR_BAS = "#007fff"
	
	static final String AGGREGATE_FUNCTION = "max"
	
	
	
	static final List DAY_METANAMES = [TEMPERATURE_HAUT_NAME, TEMPERATURE_MILIEU_NAME, TEMPERATURE_BAS_NAME]

	
	
	Double temperatureHaut() {
		device.metavalue(TEMPERATURE_HAUT_NAME)?.convertValueToDouble(1)
	}
	
	
	Double temperatureMilieu() {
		device.metavalue(TEMPERATURE_MILIEU_NAME)?.convertValueToDouble(1)
	}
	
	
	Double temperatureBas() {
		device.metavalue(TEMPERATURE_BAS_NAME)?.convertValueToDouble(1)
	}
	
	
	/**
	 * Un chart allégé sur la date spécifiée
	 * 
	 * @return
	 */
	GoogleChart googleChartLight(DeviceChartCommand command) {
		GoogleChart chart = googleChart(command)
		
		// désactive certaines options
		chart.title = null
		chart.showLegend = false
		chart.showHAxis = false
		
		return chart
	}
	
	
	@Override
	Object chartDataTemplate() {
		'/chart/datas/chartQualitatifDatas'
	}


	@Override
	List<String> dayMetaNames() {
		DAY_METANAMES
	}

		
	@Override
	GoogleChart googleChart(DeviceChartCommand command, List values) {
		GoogleChart chart = new GoogleChart(chartType: ChartTypeEnum.Line.factory)
		command.device.extrasToJson()
		
		if (command.viewMode == ChartViewEnum.day) {
			chart.values = values.groupBy { it.dateValue }
			
			chart.colonnes << new GoogleDataTableCol(label: "Date", type: "datetime", property: "key")
			chart.colonnes << new GoogleDataTableCol(label: "T° haut (°C)", type: "number", value: { deviceValue, index, currentChart ->
				deviceValue.value.find{ it.name == TEMPERATURE_HAUT_NAME }?.value
			})
			chart.colonnes << new GoogleDataTableCol(label: "T° milieu (°C)", type: "number", value: { deviceValue, index, currentChart ->
				deviceValue.value.find{ it.name == TEMPERATURE_MILIEU_NAME }?.value
			})
			chart.colonnes << new GoogleDataTableCol(label: "T° bas (°C)", type: "number", value: { deviceValue, index, currentChart ->
				deviceValue.value.find{ it.name == TEMPERATURE_BAS_NAME }?.value
			})
		} else {
			chart.values = values
			
			chart.colonnes << new GoogleDataTableCol(label: "Date", type: "datetime", property: "key")
			chart.colonnes << new GoogleDataTableCol(label: "T° haut (°C)", type: "number", value: { deviceValue, index, currentChart ->
				deviceValue.value.find{ it.name == "${TEMPERATURE_HAUT_NAME}${AGGREGATE_FUNCTION}" }?.value
			})
			chart.colonnes << new GoogleDataTableCol(label: "T° milieu (°C)", type: "number", value: { deviceValue, index, currentChart ->
				deviceValue.value.find{ it.name == "${TEMPERATURE_MILIEU_NAME}${AGGREGATE_FUNCTION}" }?.value
			})
			chart.colonnes << new GoogleDataTableCol(label: "T° bas (°C)", type: "number", value: { deviceValue, index, currentChart ->
				deviceValue.value.find{ it.name == "${TEMPERATURE_BAS_NAME}${AGGREGATE_FUNCTION}" }?.value
			})
		}
		
		chart.series << [color: COLOR_HAUT]
		chart.series << [color: COLOR_MILIEU]
		chart.series << [color: COLOR_BAS]
		
		return chart
	}
	
	
	@Override
	List aggregateValueDay(Date dateReference) {
		// Ballon : on ne calcule que les T° max sur chaque zone
		// les meta aggrégées seront appelées [metavalue]max (ex : Temperature_hautmax)
		return DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('day', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			${AGGREGATE_FUNCTION}(deviceValue.value) as ${AGGREGATE_FUNCTION})
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device AND deviceValue.name in (:metanames)
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			GROUP BY deviceValue.name, date_trunc('day', deviceValue.dateValue)""", [device: device,
				dateDebut: DateUtils.firstTimeInDay(dateReference), dateFin: DateUtils.lastTimeInDay(dateReference),
				metanames: DAY_METANAMES])
	}
	
	
	@Override
	List aggregateValueMonth(Date dateReference) {
		// Ballon : on ne calcule que les T° max sur chaque zone
		// les meta aggrégées seront appelées [metavalue]max (ex : Temperature_hautmax)
		return DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('month', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			${AGGREGATE_FUNCTION}(deviceValue.value) as ${AGGREGATE_FUNCTION})
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device AND deviceValue.name in (:metanames)
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			GROUP BY deviceValue.name, date_trunc('month', deviceValue.dateValue)""", [device: device,
				dateDebut: DateUtils.firstDayInMonth(dateReference), dateFin: DateUtils.lastTimeInDay(DateUtils.lastDayInMonth(dateReference)),
				metanames: DAY_METANAMES])
	}
	
}
