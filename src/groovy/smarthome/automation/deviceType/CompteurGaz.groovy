package smarthome.automation.deviceType

import smarthome.automation.ChartViewEnum
import smarthome.automation.CompteurIndex
import smarthome.automation.DeviceChartCommand
import smarthome.automation.DeviceValue
import smarthome.automation.DeviceValueDay
import smarthome.automation.DeviceValueMonth
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException
import smarthome.core.chart.GoogleChart
import smarthome.core.chart.GoogleDataTableCol

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class CompteurGaz extends Compteur {

	static final String CONFIG_DEFAULT_COEF_CONVERSION = "DEFAULT_COEF_CONVERSION_GAZ"
	protected static final String META_COEF_CONVERSION = "coefConversion"


	/**
	 * (non-Javadoc)
	 *
	 * @see smarthome.automation.deviceType.Compteur#parseIndex(smarthome.automation.CompteurIndex)
	 */
	@Override
	void parseIndex(CompteurIndex index) throws SmartHomeException {
		if (!index.param1) {
			throw new SmartHomeException("Coefficient de conversion obligatoire pour le calcul en Wh !")
		}

		// met à jour la valeur principale
		device.value = (index.index1 as Long).toString()

		// essaie de calculer une conso sur la période si un ancien index est trouvé
		DeviceValue lastIndex = lastIndex()
		addDefaultMetas()

		if (lastIndex) {
			// FIXME formule à confirmer !! Les index sont en m3 ou dm3 ???
			// Cas ici si tout l'index est saisi (avec la part en dm3) => on obtient de suite les Wh
			// coef conversion = XX kWh pour 1m3
			def conso = (index.index1 - lastIndex.value) * index.param1.toDouble()
			device.addMetavalue(META_METRIC_NAME, [value: (conso as Long).toString()])
		}

		device.addMetadata(META_COEF_CONVERSION, [value: index.param1])
	}


	/** 
	 * (non-Javadoc)
	 *
	 * @see smarthome.automation.deviceType.Compteur#addDefaultMetas()
	 */
	@Override
	protected void addDefaultMetas() {
		device.addMetavalue(META_METRIC_NAME, [value: "0", label: "Période consommation", trace: true, unite: 'Wh'])
		device.addMetadata(META_COEF_CONVERSION, [label: 'Coefficient conversion'])
	}


	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#aggregateValueDay(java.util.Date)
	 */
	@Override
	List aggregateValueDay(Date dateReference) {
		// calcule les consos à partir des consos par période
		// on ne peut pas repartir des index car il faut les transformer avec le coef de conversion
		// qui peut changer sur la période
		def values = DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('day', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			sum(deviceValue.value) as ${AGGREGATE_METRIC_NAME})
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			AND deviceValue.name in (:metaNames)
			GROUP BY date_trunc('day', deviceValue.dateValue), deviceValue.name""", [device: device,
					dateDebut: DateUtils.firstTimeInDay(dateReference),
					dateFin: DateUtils.lastTimeInDay(dateReference), metaNames: [META_METRIC_NAME]])

		return values
	}



	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#aggregateValueMonth(java.util.Date)
	 */
	@Override
	List aggregateValueMonth(Date dateReference) {
		// calcule les consos à partir des consos par période
		// on ne peut pas repartir des index car il faut les transformer avec le coef de conversion
		// qui peut changer sur la période
		def values = DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('month', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			sum(deviceValue.value) as ${AGGREGATE_METRIC_NAME})
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			AND deviceValue.name in (:metaNames)
			GROUP BY date_trunc('month', deviceValue.dateValue), deviceValue.name""", [device: device,
					dateDebut: DateUtils.firstDayInMonth(dateReference),
					dateFin: DateUtils.lastTimeInDay(DateUtils.lastDayInMonth(dateReference)),
					metaNames: [META_METRIC_NAME]])

		return values
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see smarthome.automation.deviceType.Compteur#googleChart(smarthome.automation.DeviceChartCommand, java.util.List)
	 */
	@Override
	GoogleChart googleChart(DeviceChartCommand command, List values) {
		GoogleChart chart = new GoogleChart()
		command.device.extrasToJson()
		chart.values = values

		chart.colonnes << new GoogleDataTableCol(label: "Date", type: "datetime", property: "dateValue")

		if (command.viewMode == ChartViewEnum.day) {
			chart.colonnes << new GoogleDataTableCol(label: "Consommation (Wh)", property: "value", type: "number")
			chart.series << [type: 'bars', color: SERIES_COLOR.conso, targetAxisIndex: 0]
		} else {
			// les consos sont conveties en kWh
			chart.colonnes << new GoogleDataTableCol(label: "Consommation (kWh)", type: "number", value: { deviceValue, index, currentChart ->
				return (deviceValue.value / 1000.0).round(1)
			})
			chart.series << [type: 'bars', color: SERIES_COLOR.conso, targetAxisIndex: 0, annotation: true]
		}

		return chart
	}


	/** (non-Javadoc)
	 *
	 * @see smarthome.automation.deviceType.Compteur#chartDataTemplate()
	 */
	@Override
	def chartDataTemplate() {
		'/chart/datas/chartQualitatifDatas'
	}


	/**
	 * La clé des données aggrégées
	 *
	 * @return
	 */
	@Override
	protected String aggregateMetaName() {
		"${META_METRIC_NAME}${AGGREGATE_METRIC_NAME}"
	}

	/**
	 * Unité pour les widgets (peut être différent)
	 *
	 * @return
	 */
	@Override
	String defaultUnite() {
		"kWh"
	}
}
