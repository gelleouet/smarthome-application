package smarthome.automation.deviceType

import org.apache.commons.lang.StringUtils

import groovy.time.TimeCategory

import java.lang.reflect.Method
import java.util.Map

import smarthome.automation.ChartTypeEnum
import smarthome.automation.ChartViewEnum
import smarthome.automation.DataModifierEnum
import smarthome.automation.Device
import smarthome.automation.DeviceChartCommand
import smarthome.automation.DeviceValue
import smarthome.automation.DeviceValueDay
import smarthome.automation.DeviceValueMonth
import smarthome.automation.LevelAlertEnum
import smarthome.automation.SeriesTypeEnum
import smarthome.automation.WorkflowEvent
import smarthome.automation.WorkflowEventParameter
import smarthome.automation.WorkflowEventParameters
import smarthome.core.ApplicationUtils
import smarthome.core.ClassUtils
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException
import smarthome.core.chart.GoogleChart
import smarthome.core.chart.GoogleChartProcessor
import smarthome.core.chart.GoogleDataTableCol

abstract class AbstractDeviceType implements Serializable {
	Device device
	String name = simpleName()
	protected Map tarifCache
	protected Map viewParams = [:]


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
	 * Style du widget
	 * 
	 * @return
	 */
	String cssStyle() {
		""
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
	 * Une vue chart spécifique à un device.
	 * Par défaut, rien : affichage du graphique normal
	 * 
	 * @return
	 */
	String viewChart() {
	}


	/**
	 * Nom de l'image à associer au device
	 * 
	 * @return
	 */
	String icon() {
		"/deviceType/${name}.png"
	}


	/**
	 * Indique si device qualitatif ou quantitatif
	 * 
	 * @return
	 */
	boolean isQualitatif() {
		return device.deviceType.qualitatif
	}


	/**
	 * Indique s'il faut historiser la valeur
	 * Par défaut, historise tout le temps si device qualitatif, sinon
	 * pour les quantitatif, seulement les positifs
	 * 
	 * @param value
	 * @return
	 */
	boolean isTraceValue(Double value) {
		if (isQualitatif()) {
			return value != null
		} else {
			return value
		}
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
	 * @param datas original datas
	 * 
	 * @return
	 */
	def prepareMetaValuesForSave(def datas) {
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
	GoogleChart googleChart(DeviceChartCommand command, List values) {
		GoogleChart chart = new GoogleChart()
		command.device.extrasToJson()

		/**
		 * Graphe qualitatif : valeur importante
		 */
		if (isQualitatif()) {
			chart.values = values

			if (command.viewMode == ChartViewEnum.day) {
				chart.colonnes = [new GoogleDataTableCol(label: "Date", property: "dateValue", type: "datetime"), new GoogleDataTableCol(label: "Valeur", property: "value", type: "number"), new GoogleDataTableCol(type: "boolean", role: "scope", value: { deviceValue, index, currentChart ->
						deviceValue.alertLevel ? false : true
					}),]

				// série par défaut en bleu
				chart.series << [color: '#3572b0', type: SeriesTypeEnum.area.toString()]

				// affichage des alertes
				command.device.levelAlerts?.findAll({ it.level != LevelAlertEnum.monitoring })?.sort({ it.level })?.each {
					chart.colonnes << new GoogleDataTableCol(label: "${it.mode} ${it.value}°", staticValue: it.value, type: "number")
					chart.series << [color: (it.level == LevelAlertEnum.warning ? '#ff9f00' : '#dc3912'), lineStyle: "dash"]
				}
			} else {
				chart.colonnes = [
					new GoogleDataTableCol(label: "Date", type: "date", property: "key"),
					new GoogleDataTableCol(label: "Min", type: "number", value: { deviceValue, index, currentChart -> deviceValue.value.find{ it.name == "min" }?.value }),
					new GoogleDataTableCol(label: "Max", type: "number", value: { deviceValue, index, currentChart -> deviceValue.value.find{ it.name == "max" }?.value }),
					new GoogleDataTableCol(label: "Moyenne", type: "number", value: { deviceValue, index, currentChart -> deviceValue.value.find{ it.name == "avg" }?.value }),
				]

				chart.series = [[color: '#ff9f00'], // min
					[color: '#dc3912'], // max
					[color: '#3572b0', type: SeriesTypeEnum.area.toString()]	// moyenne
				]
			}
		}
		/**
		 * Graphe quantitatif : nombre d'évévement
		 */
		else {
			if (command.viewMode == ChartViewEnum.day) {
				// regroupement des valeurs sur l'heure de la journée
				chart.values = values.groupBy { DateUtils.copyTruncHour(it.dateValue) }

				chart.colonnes = [
					new GoogleDataTableCol(label: "ID", type: "string", staticValue: ""),
					new GoogleDataTableCol(label: "Date", type: "datetime", property: "key"),
					new GoogleDataTableCol(label: "Heure", type: "number", value: { deviceValue, index, currentChart ->
						deviceValue.key[Calendar.HOUR_OF_DAY]
					}),
					new GoogleDataTableCol(label: "Objet", type: "string", staticValue: command.device.label),
					new GoogleDataTableCol(label: "Quantité", type: "number", value: { deviceValue, index, currentChart ->
						deviceValue.value.size()
					})
				]
			} else {
				// les données sont déjà regroupées comme il faut
				chart.values = values

				chart.colonnes = [
					new GoogleDataTableCol(label: "ID", type: "string", staticValue: ""),
					new GoogleDataTableCol(label: "Date", type: "date", value: { deviceValue, index, currentChart ->
						DateUtils.copyTruncDay(deviceValue.key)
					}),
					new GoogleDataTableCol(label: "Heure", type: "number", value: { deviceValue, index, currentChart ->
						deviceValue.key[Calendar.HOUR_OF_DAY]
					}),
					new GoogleDataTableCol(label: "Objet", type: "string", staticValue: command.device.label),
					new GoogleDataTableCol(label: "Quantité", type: "number", value: { deviceValue, index, currentChart ->
						deviceValue.value.find{ it.name == "count" }?.value
					})
				]
			}
		}

		if (command.device.extrasJson.googleChartProcessor) {
			GoogleChartProcessor processor = ClassUtils.forNameInstance(command.device.extrasJson.googleChartProcessor)
			ApplicationUtils.autowireBean(processor)
			processor.process(chart, [command: command])
		}

		return chart
	}


	/**
	 * Préparation des données pour la vue
	 * Cette méthode est appelée par le service et permet de charger des éléments
	 * indispensables à la vue.
	 * 
	 * Pour les impl, surcharger la méthode implPrepareForView
	 * 
	 */
	final void prepareForView() {
		viewParams.lastAlert = device.lastDeviceAlert()
		implPrepareForView()
	}


	/**
	 * Implémentation prepareForView à surcharger par les impl deviceType
	 */
	void implPrepareForView() {

	}


	/**
	 * Charge les données sur une période donnée (en général pour les graphes)
	 * C'est défini dans chaque impl car les données ne sont pas forcément les mêmes
	 * et la représentatin non plus
	 * 
	 * @param command
	 * @return List ou Map
	 * 
	 * @throws SmartHomeException
	 */
	def values(DeviceChartCommand command) throws SmartHomeException {
		def values = []

		if (command.viewMode == ChartViewEnum.day) {
			values = DeviceValue.values(command.device, command.dateDebut(), command.dateFin(), command.metaName)
		} else if (command.viewMode == ChartViewEnum.month) {
			// on ne sait pas si le device a plusieurs métriques, donc on fait un
			// regroupement sur la date pour avoir une abcisse homogène
			values = DeviceValueDay.values(command.device, command.dateDebut(), command.dateFin(), command.metaName).groupBy {
				it.dateValue
			}.collect { it }
		} else if (command.viewMode == ChartViewEnum.year) {
			// on ne sait pas si le device a plusieurs métriques, donc on fait un
			// regroupement sur la date pour avoir une abcisse homogène
			values = DeviceValueMonth.values(command.device, command.dateDebut(), command.dateFin(), command.metaName).groupBy {
				it.dateValue
			}.collect { it }
		}

		return values
	}


	/**
	 * Aggrège les données du device d'un jour 
	 * Uniquement la valeur principale
	 * 
	 * @param dateReference
	 */
	List aggregateValueDay(Date dateReference) {
		def values

		if (isQualitatif()) {
			// qualitatif : min, max, avg par date (sans heure)
			values = DeviceValue.executeQuery("""\
				SELECT new map(date_trunc('day', deviceValue.dateValue) as dateValue, deviceValue.name as name,
				min(deviceValue.value) as min, max(deviceValue.value) as max, avg(deviceValue.value) as avg)
				FROM DeviceValue deviceValue
				WHERE deviceValue.device = :device AND deviceValue.name is null
				AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
				GROUP BY deviceValue.name, date_trunc('day', deviceValue.dateValue)""", [device: device,
						dateDebut: DateUtils.firstTimeInDay(dateReference), dateFin: DateUtils.lastTimeInDay(dateReference)])
		} else {
			// quantitatif : count par heure de la journée
			values = DeviceValue.executeQuery("""\
				SELECT new map(date_trunc('hour', deviceValue.dateValue) as dateValue, deviceValue.name as name,
				count(deviceValue.value) as count)
				FROM DeviceValue deviceValue
				WHERE deviceValue.device = :device AND deviceValue.name is null
				AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
				GROUP BY deviceValue.name, date_trunc('hour', deviceValue.dateValue)""", [device: device,
						dateDebut: DateUtils.firstTimeInDay(dateReference), dateFin: DateUtils.lastTimeInDay(dateReference)])
		}

		return values
	}


	/**
	 * Aggrège les données du device d'un mois
	 * Uniquement la valeur principale
	 *
	 * @param dateReference
	 */
	List aggregateValueMonth(Date dateReference) {
		def values

		if (isQualitatif()) {
			// qualitatif : min, max, avg  par date (sans heure)
			values = DeviceValue.executeQuery("""\
				SELECT new map(date_trunc('month', deviceValue.dateValue) as dateValue, deviceValue.name as name,
				min(deviceValue.value) as min, max(deviceValue.value) as max, avg(deviceValue.value) as avg)
				FROM DeviceValue deviceValue
				WHERE deviceValue.device = :device AND deviceValue.name is null
				AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
				GROUP BY deviceValue.name, date_trunc('month', deviceValue.dateValue)""", [device: device,
						dateDebut: DateUtils.firstDayInMonth(dateReference), dateFin: DateUtils.lastTimeInDay(DateUtils.lastDayInMonth(dateReference))])
		} else {
			// quantitatif : count par heure de la journée
			values = DeviceValue.executeQuery("""\
				SELECT new map(date_trunc('month', deviceValue.dateValue) as dateValue, extract('hour' from deviceValue.dateValue) as hourOfDay,
				count(deviceValue.value) as count, deviceValue.name as name)
				FROM DeviceValue deviceValue
				WHERE deviceValue.device = :device AND deviceValue.name is null
				AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
				GROUP BY deviceValue.name, date_trunc('month', deviceValue.dateValue), extract('hour' from deviceValue.dateValue)""", [device: device,
						dateDebut: DateUtils.firstDayInMonth(dateReference), dateFin: DateUtils.lastTimeInDay(DateUtils.lastDayInMonth(dateReference))])
					.collect { deviceValue ->
						use (TimeCategory) {
							[dateValue: deviceValue.dateValue + deviceValue.hourOfDay.hours, name: deviceValue.name, count: deviceValue.count]
						}
					}
		}

		return values
	}


	/**
	 * Renvoit les clés disponibles pour la construction d'un plannning
	 * 
	 * @return
	 */
	List<String> planningKeys() {
		return []}


	/**
	 * Renvoit les valeurs possibles sur le planning
	 * Map indexée sur la valeur avec les infos associées (label, couleur, etc)
	 * 
	 * @return
	 */
	Map planningValues() {
		return [:]
	}
}
