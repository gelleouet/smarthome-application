package smarthome.automation.deviceType

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;

import smarthome.automation.ChartTypeEnum;
import smarthome.automation.ChartViewEnum;
import smarthome.automation.DataModifierEnum;
import smarthome.automation.Device;
import smarthome.automation.DeviceChartCommand;
import smarthome.automation.DeviceValue;
import smarthome.automation.LevelAlertEnum;
import smarthome.automation.WorkflowEvent;
import smarthome.automation.WorkflowEventParameter;
import smarthome.automation.WorkflowEventParameters;
import smarthome.core.ApplicationUtils;
import smarthome.core.ClassUtils;
import smarthome.core.chart.GoogleChart;
import smarthome.core.chart.GoogleChartProcessor;
import smarthome.core.chart.GoogleDataTableCol;

abstract class AbstractDeviceType {
	Device device
	String name = simpleName()
	
	/**
	 * Son nom simple pour le système de convention
	 * (recherche des vues, des ressources icones, etc...)
	 * 
	 * @return
	 */
	final def simpleName() {
		StringUtils.uncapitalize(this.getClass().simpleName)
	}
	
	/**
	 * Le nom de la vue pour affichage en grille
	 * 
	 * @return
	 */
	final def viewGrid() {
		"/deviceType/${name}/${name}"
	}
	
	
	
	/**
	 * Nom de la vue pour saisie des options
	 * 
	 * @return
	 */
	final def viewForm() {
		"/deviceType/${name}/${name}Form"
	}

	
	/**
	 * Nom de l'image à associer au device
	 * 
	 * @return
	 */
	def icon() {
		"/deviceType/${name}.png"
	}
	
	
	def isQualitatif() {
		return true
	} 
	
	
	/**
	 * La liste des actions disponibles pour ce device
	 * toutes les méthodes annotées WorkflowEvent
	 * 
	 * @return
	 */
	final def events() {
		def events = []
		
		getClass().getMethods()?.each { method ->
			if (method.getAnnotation(WorkflowEvent)) {
				events << method.name
			}
		}
		
		return events.sort()
	}
	
	
	/**
	 * Liste des paramètres pour la méthode demandée
	 * 
	 * @param actionName
	 * @return
	 */
	final List eventParameters(String actionName) {
		List parameters = []
		
		getClass().getMethods()?.each { method ->
			if (method.name == actionName) {
				WorkflowEventParameters workflowEventParameters = method.getAnnotation(WorkflowEventParameters)
				
				if (workflowEventParameters) {
					for (WorkflowEventParameter workflowEventParameter : workflowEventParameters.value()) {
						parameters << workflowEventParameter
					}
				}
			}
		}
		return parameters	
	}
	
	
	/**
	 * Prépare les méta données avant enregistrement de celles-ci
	 * Permet à un type device de transformer ou d'ajouter de nouvelles 
	 * meta données calculées par exemple. Les nouvelles metadonnées doivent quand
	 * même être définies dans "metaValuesName" pour être utilisées 
	 * 
	 * @return
	 */
	def prepareMetaValuesForSave() {
		
	}
	
	
	/**
	 * Retourne le type de graphique par défaut
	 *
	 * @return
	 */
	ChartTypeEnum defaultChartType() {
		if (isQualitatif()) {
			ChartTypeEnum.Line
		} else {
			ChartTypeEnum.Bubble
		}
	}
	
	
	/**
	 * Le template par défaut pour préparer les données du chart
	 *
	 * @return
	 */
	def chartDataTemplate() {
		if (isQualitatif()) {
			'/chart/datas/chartQualitatifDatas'
		} else {
			'/chart/datas/chartQuantitatifDatas'
		}
	}
	
	
	/**
	 * Utilisation d'un google chart préparé
	 * 
	 * @param command
	 * @param values
	 * @return
	 */
	GoogleChart googleChart(DeviceChartCommand command, List<DeviceValue> values) {
		if (!isQualitatif()) {
			return null
		}
		
		command.device.extrasToJson()
		
		GoogleChart chart = new GoogleChart()
		chart.values = values
		
		if (command.viewMode == ChartViewEnum.day) {
			chart.colonnes = [
				new GoogleDataTableCol(label: "Date", property: "dateValue", type: "datetime"),
				new GoogleDataTableCol(label: "Valeur", property: "value", type: "number"),
				new GoogleDataTableCol(type: "boolean", role: "scope", value: { deviceValue, index, currentChart ->
					deviceValue.alertLevel ? false : true}),
			]
			
			// série par défaut en bleu
			chart.series << [color: '#3572b0', type: 'area']
			
			// affichage des alertes
			command.device.levelAlerts?.findAll({ it.level != LevelAlertEnum.monitoring })?.sort({ it.level })?.each {
				chart.colonnes << new GoogleDataTableCol(label: "${it.mode} ${it.value}°", staticValue: it.value, type: "number")
				chart.series << [color: (it.level == LevelAlertEnum.warning ? '#ff9f00' : '#dc3912'), lineStyle: "dash"]
			}
		} else if (command.viewMode == ChartViewEnum.month) {
			chart.colonnes = [
				new GoogleDataTableCol(label: "Date", property: "day", type: "date"),
				new GoogleDataTableCol(label: "Min", property: "min", type: "number"),
				new GoogleDataTableCol(label: "Max", property: "max", type: "number"),
				new GoogleDataTableCol(label: "Moyenne", property: "avg", type: "number"),
			]
			
			chart.series = [
			    [color: '#ff9f00'],					// min
			    [color: '#dc3912'],					// max
				[color: '#3572b0', type: 'area']	// moyenne
			]
		} else if (command.viewMode == ChartViewEnum.year) {
			chart.colonnes = [
				new GoogleDataTableCol(label: "Date", type: "date", value: { deviceValue, index, currentChart ->
					new Date().clearTime().copyWith([date: 1, month: deviceValue.month-1, year: deviceValue.year])	
				}),
				new GoogleDataTableCol(label: "Min", property: "min", type: "number"),
				new GoogleDataTableCol(label: "Max", property: "max", type: "number"),
				new GoogleDataTableCol(label: "Moyenne", property: "avg", type: "number"),
			]
			
			chart.series = [
				[color: '#ff9f00'],					// min
				[color: '#dc3912'],					// max
				[color: '#3572b0', type: 'area']	// moyenne
			]
		}
		
		if (command.device.extrasJson.googleChartProcessor) {
			GoogleChartProcessor processor = ClassUtils.forNameInstance(command.device.extrasJson.googleChartProcessor)
			ApplicationUtils.autowireBean(processor)
			processor.process(chart, [command: command])
		}
		
		return chart
	}
}
