/**
 * 
 */
package smarthome.core.chart

import smarthome.automation.ChartCommand;
import smarthome.automation.Device;

/**
 * @author gregory.elleouet@gmail.com<Grégory Elléoouet>
 *
 */
interface ChartTransformer {
	/**
	 * 
	 * @param command
	 * @param values
	 * @return
	 */
	Collection transform(ChartCommand command, Collection values)
}
