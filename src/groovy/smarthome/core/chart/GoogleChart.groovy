package smarthome.core.chart

import grails.converters.JSON;
import grails.web.JSONBuilder;
import java.util.List;


class GoogleChart {
	String chartType
	def values
	List<GoogleDataTableCol> colonnes = []
	List<Map> series = []
	List<Map> metaValues = []
	List<Map> vAxis = []
	
	GoogleChart joinChart
	
	
	
	/**
	 * Transformation des données en JSON
	 * @see https://developers.google.com/chart/interactive/docs/datatables_dataviews
	 * 
	 * @return
	 */
	JSON toJsonDataTable() {
		JSONBuilder builder = new JSONBuilder()
		
		def dataTableRows = []
		
		def dataTableCols = colonnes.collect {
			return ["label": it.label, "type": it.type, "role": it.role, "p": it.style]
		}
		
		values.eachWithIndex { deviceValue, index ->
			def values = []
			
			for (GoogleDataTableCol col : colonnes) {
				def value = null
				
				if (col.staticValue) {
					value = col.staticValue
				} else if (col.value) {
					value = col.value(deviceValue, index, this)
				} else if (col.property) {
					value = deviceValue[(col.property)]
				} else if (col.metaName && metaValues.size() == values.size()) {
					value = metaValues[index][(col.metaName)]
				}
				
				// cas spécial pour les dates qui doivent être formattées avec le mot clé "Date"
				// @see https://developers.google.com/chart/interactive/docs/datesandtimes
				if (value instanceof Date) {
					if (col.type == "timeofday") {
						value = [value[Calendar.HOUR_OF_DAY], value[Calendar.MINUTE], value[Calendar.SECOND]]
					} else {
						def format = col.type == "date" ? "yyyy,${value.getAt(Calendar.MONTH)},d" : 
							"yyyy,${value.getAt(Calendar.MONTH)},d,HH,mm"
						value = "Date(${value.format(format)})"
					}
				}
				
				values << [v: value]
			}
			
			def row = ["c": values]
			
			if (deviceValue?.hasProperty('id')) {
				row["p"] = [deviceValueId: deviceValue.id]	
			}
			
			dataTableRows << row
		}
		
		return builder.build {
			cols = dataTableCols
			rows = dataTableRows
		}
	}
	
	
	String lineDashStyle(def serie) {
		serie.lineStyle == "dash" ? "[14, 8, 7, 8]" : "[]"
	}
}
