package smarthome.automation.deviceType

import smarthome.automation.ChartTypeEnum
import smarthome.automation.ChartViewEnum
import smarthome.automation.CompteurIndex
import smarthome.automation.DeviceChartCommand
import smarthome.automation.DeviceValue
import smarthome.automation.DeviceValueDay
import smarthome.automation.DeviceValueMonth
import smarthome.core.CompteurUtils
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException
import smarthome.core.chart.GoogleChart
import smarthome.core.chart.GoogleDataTableCol

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class CompteurEau extends Compteur {

	/**
	 * (non-Javadoc)
	 *
	 * @see smarthome.automation.deviceType.Compteur#parseIndex(smarthome.automation.CompteurIndex)
	 */
	@Override
	void parseIndex(CompteurIndex index) throws SmartHomeException {
		// met à jour la valeur principale
		device.value = (index.index1 as Long).toString()

		// essaie de calculer une conso sur la période si un ancien index est trouvé
		DeviceValue lastIndex = lastIndex()
		addDefaultMetas()

		if (lastIndex) {
			def conso = (index.index1 - lastIndex.value)
			device.addMetavalue(META_METRIC_NAME, [value: (conso as Long).toString()])
		}
	}


	/** 
	 * (non-Javadoc)
	 *
	 * @see smarthome.automation.deviceType.Compteur#addDefaultMetas()
	 */
	@Override
	protected void addDefaultMetas() {
		device.addMetavalue(META_METRIC_NAME, [value: "0", label: "Période consommation", trace: true, unite: 'L'])
	}
}
