package smarthome.core.chart

import grails.converters.JSON;
import grails.web.JSONBuilder;
import groovy.time.TimeCategory;

import java.util.List;

import smarthome.automation.ChartViewEnum;
import smarthome.core.DateUtils;


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
			return ["label": it.label, "type": it.type, "role": it.role, "pattern": it.pattern, "p": it.style]
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
	
	
	/**
	 * Calcul les labels en abscisse en fonction de la vue
	 * 
	 * @param command
	 * @return
	 */
	String ticks(def command) {
		def ticks = []
		def date
		
		use (TimeCategory) {
			if (command.viewMode == ChartViewEnum.day) {
				// un tick toutes les 2 heures
				date = command.dateChart
				
				for (int h=0; h<=23; h+=2) {
					ticks << "new Date(${date[Calendar.YEAR]}, ${date[Calendar.MONTH]}, ${date[Calendar.DAY_OF_MONTH]}, ${h}, 0)"
				}
			} else if (command.viewMode == ChartViewEnum.month) {
				// un tick tous les 3 jours
				date = DateUtils.firstDayInMonth(command.dateChart)
				
				for (int d=1; d<=date.toCalendar().getActualMaximum(Calendar.DAY_OF_MONTH); d+=3) {
					ticks << "new Date(${date[Calendar.YEAR]}, ${date[Calendar.MONTH]}, ${d})"
				}
			} else if (command.viewMode == ChartViewEnum.year) {
				// un tick par mois
				date = DateUtils.firstDayInYear(command.dateChart)
				
				for (int m=0; m<12; m++) {
					ticks << "new Date(${date[Calendar.YEAR]}, ${m}, 1)"
				}	
			}
		}
		
		return ticks.join(",")
	}
	
	String format(def command) {
		String format = ""
		
		if (command.viewMode == ChartViewEnum.day) {
			format = 'HH:mm'
		} else if (command.viewMode == ChartViewEnum.month) {
			format = 'd MMM yyyy'
		} else if (command.viewMode == ChartViewEnum.year) {
			format = 'MMM yyyy'
		}
		
		return format
	}
	
	String hAxisTitle(def command) {
		String title = ""
		
		if (command.viewMode == ChartViewEnum.day) {
			title = command.dateChart.format('EEEE dd MMMM yyyy')
		} else if (command.viewMode == ChartViewEnum.month) {
			title = command.dateChart.format('MMMM yyyy')
		} else if (command.viewMode == ChartViewEnum.year) {
			title = command.dateChart.format('yyyy')
		}
		
		return title
	}
}
