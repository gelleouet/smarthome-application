package smarthome.core.chart;

import java.util.Map;


/**
 * Processor pour manipuler un @see GoogleDataTable
 * 
 * @author Gregory
 *
 */
public interface GoogleChartProcessor {
	/**
	 * Exécution du processeur
	 * 
	 * @param chart
	 * @param params
	 * @return
	 */
	public GoogleChart process(GoogleChart chart, Map params);
}
