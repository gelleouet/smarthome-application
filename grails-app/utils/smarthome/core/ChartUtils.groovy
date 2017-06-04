package smarthome.core

import java.util.Map.Entry;

import smarthome.automation.AbstractChartCommand;
import smarthome.automation.ChartViewEnum;


/**
 * Méthodes utilitaires sur les graphiques
 * 
 * @author gregory
 *
 */
class ChartUtils {
	
	/**
	 * Insère une valeur dans un tableau json à une certaine position en laissant les autres positions vides
	 * @param value
	 * @param position
	 * @param length
	 * @return
	 */
	static String jsonArrayValue(Double value, int position, int length) {
		String str = ""
		
		for (int idx=0; idx<length; idx++) {
			if (idx == position && value != null) {
				str += value
			} else {
				str += "null"
			}
			
			if (idx < length-1) {
				str+= ","
			}
		}
		
		return str	
	}
	
	
	/**
	 * 
	 * @param values
	 * @param keys
	 * @param valueName
	 * @return
	 */
	static String jsonArrayValue(Map values, List keys) {
		String str = ""
		
		for (def key : keys) {
			if (str != "") {
				str += ","
			}
			
			def value = values[(key)]
			
			if (value != null && value[(key.function)] != null) {
				str += value[(key.function)]
			} else {
				str += "null"
			}
		}
		
		return str
	}
	
	
	/**
	 * Harmonise la liste "compareDatas" pour que chaque entrée de la liste "datas" ait une 
	 * correspondance dans la liste "compareDatas" en focntion de la vue
	 * 
	 * @param command
	 * @param datas
	 * @param compareDatas
	 * @param compareDatas
	 */
	static void harmonizeCompareDatas(AbstractChartCommand command, List datas, List compareDatas) {
		// parcours de la liste nouvelle
		for (def data : datas) {
			// recherche une entrée dans la liste compare en fonction de la vue
			def compareData = compareDatas.find {
				switch (command.viewMode) {
					case (ChartViewEnum.month): return it.day == data.day
					case (ChartViewEnum.year): return it.month == data.month && it.year == data.year
				}
			}
			
			if (!compareData) {
				compareDatas << [day: data.day, month: data.month, year: data.year, hour: data.hour,
					min:0, max:0, avg:0, sum:0, count:0]
			}
		}	
	}
	
	 
	/**
	 * Harmonise une map de datas
	 * 
	 * @param command
	 * @param datas
	 * @param compareDatas
	 * @param compareDatas
	 */
	static void harmonizeCompareMapDatas(AbstractChartCommand command, Map datas, Map compareDatas) {
		// parcours de la liste nouvelle
		for (Entry entry : datas) {
			List compareList = compareDatas.get(entry.key)
			
			if (compareList == null) {
				compareList = []
				compareDatas.put(entry.key, compareList)
			}
			
			harmonizeCompareDatas(command, entry.value, compareList)
		}	
	} 
	
	
	/**
	 * Bascule une map de datas dans une list de map
	 * 
	 * @param command
	 * @param datas
	 * @return
	 */
	static List groupMapDatas(AbstractChartCommand command, Map datas) {
		List result = []
		
		if (command.viewMode == ChartViewEnum.month) {
			command.dateDebut().upto(command.dateFin()) {
				Map item = [day: it]
				// recherches des value associées à cette date
				for (Entry entry : datas) {
					for (def data : entry.value) {
						if (data.day == it) {
							item[(entry.key)] = data 
						}	
					}	
				}
				result << item
			}
		} else if (command.viewMode == ChartViewEnum.year) {
			int year = command.dateChart[Calendar.YEAR]
			
			for (int month=1; month<=12; month++) {
				Map item = [month: month, year: year]
				// recherches des value associées au mois/année
				for (Entry entry : datas) {
					for (def data : entry.value) {
						if (data.month == month && data.year == year) {
							item[(entry.key)] = data
						}
					}
				}
				result << item
			}
		}
		
		return result
	}
}
