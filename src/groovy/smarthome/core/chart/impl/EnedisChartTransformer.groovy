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
class EnedisChartTransformer implements ChartTransformer {

	/**
	 * Décale les dates d'une 1h en avance
	 * Convertit les W en Wh en divisant par 0.5 car la valeur original est la puissance cumulée 
	 * sur 1/2h 
	 */
	@Override
	Collection transform(ChartCommand command, Collection values) {
		values?.each { value ->
			use(TimeCategory) {
				value.dateValue = value.dateValue - 60.minutes
			}
			value.value = value.value * 0.5
			
		}
		
		return values	
	}

}
