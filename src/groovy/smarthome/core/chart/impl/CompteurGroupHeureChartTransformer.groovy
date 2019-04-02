/**
 * 
 */
package smarthome.core.chart.impl

import groovy.time.TimeCategory;

import java.util.Collection

import smarthome.automation.ChartCommand;
import smarthome.automation.Device;
import smarthome.core.chart.ChartTransformer;

/**
 * @author gregory.elleouet@gmail.com<Grégory Elléoouet>
 *
 */
class CompteurGroupHeureChartTransformer implements ChartTransformer {

	/**
	 * Regroupe les consos sur par plage d'une 1/2H
	 */
	@Override
	Collection transform(ChartCommand command, Collection values) {
		def newValues = values?.groupBy { value ->
			((value.dateValue.time / 60000) / 30) as Long
		}.collect { entry ->
			if (entry.value) {
				[dateValue: entry.value.max{ it.dateValue }.dateValue,
				 device: entry.value[0].device,
				 name: entry.value[0].name,
				 value: entry.value.sum{ it.value}]
			}
		}
		
		if (newValues) {
			newValues.add(0, [dateValue: newValues[0].dateValue.clearTime(),
				 device: newValues[0].device,
				 name: newValues[0].name,
				 value: 0d])	
		}
		
		return newValues
	}

}
