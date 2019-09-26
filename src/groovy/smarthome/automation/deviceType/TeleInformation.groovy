package smarthome.automation.deviceType

import groovy.time.TimeCategory

import java.util.Date
import java.util.List
import java.util.Map

import smarthome.automation.ChartTypeEnum
import smarthome.automation.ChartViewEnum
import smarthome.automation.DataModifierEnum
import smarthome.automation.Device
import smarthome.automation.DeviceChartCommand
import smarthome.automation.DeviceMetadata
import smarthome.automation.DeviceTypeProvider
import smarthome.automation.DeviceTypeProviderPrix
import smarthome.automation.DeviceValue
import smarthome.automation.DeviceValueDay
import smarthome.automation.DeviceValueMonth
import smarthome.automation.HouseConso
import smarthome.automation.SaisieIndexCommand
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException
import smarthome.core.chart.GoogleChart
import smarthome.core.chart.GoogleDataTableCol

/**
 * Périphérique Télé-info EDF
 * 
 * @author gregory
 *
 */
class TeleInformation extends Compteur {

	public static final SERIES_COLOR = [
		'hc': '#47bac1',
		'hp': '#dc3912',
		'base': '#47bac1',
		'puissance': '#ff9900', //'#a180da',
		'total': '#e8eaed'
	]

	/**
	 * @see smarthome.automation.deviceType.AbstractDeviceType.chartDataTemplate()
	 */
	@Override
	def chartDataTemplate() {
		'/deviceType/teleInformation/teleInformationChartDatas'
	}



	/** 
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#viewChart()
	 */
	@Override
	String viewChart() {
		'/deviceType/teleInformation/teleInformationChart'
	}



	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#values()
	 */
	@Override
	List values(DeviceChartCommand command) throws SmartHomeException {
		def values = []

		if (command.viewMode == ChartViewEnum.day) {
			values = DeviceValue.values(command.device, command.dateDebut(), command.dateFin(),
					command.metaName ?: "null,hcinst,hpinst,baseinst")
		} else if (command.viewMode == ChartViewEnum.month) {
			// en fonction du contrat, il peut y avoir plusieurs métriques
			// au minium il y en a 2 : la conso et la puissance
			values = DeviceValueDay.values(command.device, command.dateDebut(), command.dateFin(), command.metaName).groupBy {
				it.dateValue
			}.collect { it }
		} else if (command.viewMode == ChartViewEnum.year) {
			// en fonction du contrat, il peut y avoir plusieurs métriques
			// au minium il y en a 2 : la conso et la puissance
			values = DeviceValueMonth.values(command.device, command.dateDebut(), command.dateFin(), command.metaName).groupBy {
				it.dateValue
			}.collect { it }
		}

		return values
	}


	/**
	 * important de surcharger cette boite car le graphe teleinfo a son propre builder de chart
	 * sinon le graphe sera créé une 1ere fois dans la méthode parent
	 * 
	 * @see smarthome.automation.deviceType.AbstractDeviceType#googleChart(smarthome.automation.DeviceChartCommand, java.util.List)
	 */
	@Override
	GoogleChart googleChart(DeviceChartCommand command, List values) {
		GoogleChart chart = new GoogleChart()
		def opttarif = command.device.metavalue("opttarif")?.value
		int coefPuissance = command.device.unite == "W" ? 1 : 220


		if (command.viewMode == ChartViewEnum.day) {
			chart.values = values.groupBy { it.dateValue }

			chart.colonnes = []
			chart.colonnes << new GoogleDataTableCol(label: "Date", type: "datetime", property: "key")

			if (opttarif in ["HC", "EJP"]) {
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'creuses' : 'normales' } (Wh)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find{ it.name == "hcinst" }?.value
				})
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'pleines' : 'pointe mobile' } (Wh)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find{ it.name == "hpinst" }?.value
				})

				chart.series << [type: 'steppedArea', color: SERIES_COLOR.hc, targetAxisIndex: 0]
				chart.series << [type: 'steppedArea', color: SERIES_COLOR.hp, targetAxisIndex: 0]
			} else {
				chart.colonnes << new GoogleDataTableCol(label: "Heures base (Wh)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find{ it.name == "baseinst" }?.value
				})
				chart.series << [type: 'steppedArea', color: SERIES_COLOR.base, targetAxisIndex: 0]
			}

			chart.colonnes << new GoogleDataTableCol(label: "Puissance max (W)", type: "number", value: { deviceValue, index, currentChart ->
				(deviceValue.value.find{ !it.name }?.value ?: 0) * coefPuissance
			})
			chart.series << [type: 'line', color: SERIES_COLOR.puissance, targetAxisIndex: 1]
		} else {
			chart.values = values

			chart.colonnes = []
			chart.colonnes << new GoogleDataTableCol(label: "Date", type: "date", property: "key")

			if (opttarif in ["HC", "EJP"]) {
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'creuses' : 'normales' } (kWh)", type: "number", value: { deviceValue, index, currentChart ->
					def value = deviceValue.value.find{ it.name == "hchcsum" }?.value
					if (value != null) {
						return (value / 1000d).round(1)
					} else {
						return null
					}
				})
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'pleines' : 'pointe mobile' } (kWh)", type: "number", value: { deviceValue, index, currentChart ->
					def value = deviceValue.value.find{ it.name == "hchpsum" }?.value
					if (value != null) {
						return (value / 1000d).round(1)
					} else {
						return null
					}
				})

				chart.series << [type: 'bars', color: SERIES_COLOR.hc, targetAxisIndex: 0, annotation: true]
				chart.series << [type: 'bars', color: SERIES_COLOR.hp, targetAxisIndex: 0, annotation: true]
			} else {
				chart.colonnes << new GoogleDataTableCol(label: "Heures base (kWh)", type: "number", value: { deviceValue, index, currentChart ->
					def value = deviceValue.value.find{ it.name == "basesum" }?.value
					if (value != null) {
						return (value / 1000d).round(1)
					} else {
						return null
					}
				})
				chart.series << [type: 'bars', color: SERIES_COLOR.base, targetAxisIndex: 0, annotation: true]
			}

			if (!command.comparePreviousYear) {
				chart.colonnes << new GoogleDataTableCol(label: "Puissance max (W)", type: "number", value: { deviceValue, index, currentChart ->
					(deviceValue.value.find{ it.name == "max" }?.value ?:0) * coefPuissance
				})

				chart.series << [type: 'line', color: SERIES_COLOR.puissance, targetAxisIndex: 1, annotation: command.viewMode == ChartViewEnum.year]
			}
		}

		return chart
	}


	/**
	 * Construction d'un graphe avec les tarifs
	 * 
	 * @param command
	 * @param values
	 * @return
	 */
	GoogleChart googleChartTarif(DeviceChartCommand command, def values) {
		GoogleChart chart = new GoogleChart()
		def opttarif = command.device.metavalue("opttarif")?.value

		//chart.vAxis << [title: 'Coût (€)']

		if (command.viewMode == ChartViewEnum.day) {
			chart.chartType = "SteppedAreaChart"

			chart.values = values.collectEntries { entry ->
				Map resultValues = [:]
				entry.value.each { deviceValue ->
					if (deviceValue.name in ['hcinst', 'hpinst', 'baseinst']) {
						def name

						if (deviceValue.name == 'hcinst') {
							name = 'HC'
						} else if (deviceValue.name == 'hpinst') {
							name = 'HP'
						} else {
							name = 'BASE'
						}
						def kwh = deviceValue.value / 1000.0
						resultValues["kwh${name}"] = kwh
						resultValues["prix${name}"] = command.deviceImpl.calculTarif(name, kwh, deviceValue.dateValue[Calendar.YEAR])
					}
				}
				resultValues["kwh"] = (resultValues["kwhHC"]?:0d) + (resultValues["kwhHP"]?:0d) + (resultValues["kwhBASE"]?:0d)
				resultValues["prix"] = (resultValues["prixHC"]?:0d) + (resultValues["prixHP"]?:0d) + (resultValues["prixBASE"]?:0d)
				[(entry.key): resultValues]
			}

			chart.colonnes = []
			chart.colonnes << new GoogleDataTableCol(label: "Date", type: "datetime", value: { deviceValue, index, currentChart ->
				deviceValue.key
			})

			if (opttarif in ["HC", "EJP"]) {
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'creuses' : 'normales' } (€)", type: "number", pattern: "#.##", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixHC"]
				})
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'pleines' : 'pointe mobile' } (€)", type: "number", pattern: "#.##", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixHP"]
				})

				chart.series << [type: 'steppedArea', color: SERIES_COLOR.hc]
				chart.series << [type: 'steppedArea', color: SERIES_COLOR.hp]
			} else {
				chart.colonnes << new GoogleDataTableCol(label: "Heures base (€)", type: "number", pattern: "#.##", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixBASE"]
				})
				chart.series << [type: 'steppedArea', color: SERIES_COLOR.base]
			}
		} else {
			chart.values = values.collectEntries { entry ->
				Map resultValues = [:]
				entry.value.each { deviceValue ->
					if (deviceValue.name in ['hchcsum', 'hchpsum', 'basesum']) {
						def name

						if (deviceValue.name == 'hchcsum') {
							name = 'HC'
						} else if (deviceValue.name == 'hchpsum') {
							name = 'HP'
						} else {
							name = 'BASE'
						}
						def kwh = deviceValue.value / 1000.0
						resultValues["kwh${name}"] = kwh
						resultValues["prix${name}"] = command.deviceImpl.calculTarif(name, kwh, deviceValue.dateValue[Calendar.YEAR])
					}
				}
				resultValues["kwh"] = (resultValues["kwhHC"]?:0d) + (resultValues["kwhHP"]?:0d) + (resultValues["kwhBASE"]?:0d)
				resultValues["prix"] = (resultValues["prixHC"]?:0d) + (resultValues["prixHP"]?:0d) + (resultValues["prixBASE"]?:0d)
				[(entry.key): resultValues]
			}

			chart.colonnes = []
			chart.colonnes << new GoogleDataTableCol(label: "Date", type: "date", value: { deviceValue, index, currentChart ->
				deviceValue.key
			})

			if (opttarif in ["HC", "EJP"]) {
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'creuses' : 'normales' } (€)", type: "number", pattern: "#", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixHC"]
				})
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'pleines' : 'pointe mobile' } (€)", type: "number", pattern: "#", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixHP"]
				})
				chart.colonnes <<  new GoogleDataTableCol(label: "Total (€)", type: "number", pattern: "#", value: { deviceValue, index, currentChart ->
					deviceValue.value["prix"]
				})

				chart.series << [type: 'bars', color: SERIES_COLOR.hc]
				chart.series << [type: 'bars', color: SERIES_COLOR.hp]
				chart.series << [type: 'bars', color: SERIES_COLOR.total, annotation: true]
			} else {
				chart.colonnes << new GoogleDataTableCol(label: "Heures base (€)", type: "number", pattern: "#", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixBASE"]
				})
				chart.series << [type: 'bars', color: SERIES_COLOR.base, annotation: true]
			}
		}

		return chart
	}



	/**
	 * @see smarthome.automation.deviceType.AbstractDeviceType.prepateMetaValuesForSave()
	 */
	@Override
	def prepareMetaValuesForSave(def datas) {
		Date dateInf

		use (TimeCategory) {
			dateInf = device.dateValue - 30.minutes
		}

		// si le device n'existe pas encore, il n'y a donc pas d'anciennes valeurs
		// pour calculer la dernière conso
		if (device.id) {
			// calcul conso heure creuse sur la période
			def hc = device.metavalue("hchc")

			// les metavalues sur la période sont désormais gérées par le controller (à cause du offline)
			// mais pour les anciennes versions, il faut les ajouter ici manuellement
			if (hc && !datas.metavalues?.hcinst) {
				device.addMetavalue("hcinst", [value: "0", label: "Période heures creuses",
					trace: true, unite: "Wh"])
				// récupère la dernière valeur hchc
				def lastHC = DeviceValue.lastValueInPeriod(device, dateInf, device.dateValue, "hchc")

				if (lastHC) {
					def conso = hc.value.toLong() - lastHC.value.toLong()
					device.addMetavalue("hcinst", [value: conso.toString()])
				}
			}

			// calcul conso heure pleine sur la période
			def hp = device.metavalue("hchp")

			if (hp && !datas.metavalues?.hpinst) {
				device.addMetavalue("hpinst", [value: "0", label: "Période heures pleines",
					trace: true, unite: "Wh"])
				// récupère la dernière valeur hchp
				def lastHP = DeviceValue.lastValueInPeriod(device, dateInf, device.dateValue, "hchp")

				if (lastHP) {
					def conso = hp.value.toLong() - lastHP.value.toLong()
					device.addMetavalue("hpinst", [value: conso.toString()])
				}
			}

			// calcul conso toute heure sur la période
			def base = device.metavalue("base")

			if (base && !datas.metavalues?.baseinst) {
				device.addMetavalue("baseinst", [value: "0", label: "Période toutes heures",
					trace: true, unite: "Wh"])
				// récupère la dernière valeur base
				def lastBase = DeviceValue.lastValueInPeriod(device, dateInf, device.dateValue, "base")

				if (lastBase) {
					def conso = base.value.toLong() - lastBase.value.toLong()
					device.addMetavalue("baseinst", [value: conso.toString()])
				}
			}
		}
	}


	/**
	 * Unité pour les widgets (peut être différent)
	 * 
	 * @return
	 */
	@Override
	String defaultUnite() {
		"kWh"
	}





	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#aggregateValueDay(java.util.Date)
	 */
	@Override
	List aggregateValueDay(Date dateReference) {
		def values = []

		// traite d'abord l'intensité max
		values.addAll(DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('day', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			max(deviceValue.value) as max)
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			AND deviceValue.name is null
			GROUP BY deviceValue.name, date_trunc('day', deviceValue.dateValue)""", [device: device,
					dateDebut: DateUtils.firstTimeInDay(dateReference), dateFin: DateUtils.lastTimeInDay(dateReference)]))

		// traite ensuite les index
		values.addAll(DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('day', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			(max(deviceValue.value) - min(deviceValue.value)) as sum)
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			AND deviceValue.name in (:metaNames)
			GROUP BY deviceValue.name, date_trunc('day', deviceValue.dateValue)""", [device: device,
					dateDebut: DateUtils.firstTimeInDay(dateReference), dateFin: DateUtils.lastTimeInDay(dateReference),
					metaNames: ['hchp', 'hchc', 'base']]))

		return values
	}


	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#aggregateValueMonth(java.util.Date)
	 */
	@Override
	List aggregateValueMonth(Date dateReference) {
		def values = []

		// traite d'abord l'intensité max
		values.addAll(DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('month', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			max(deviceValue.value) as max)
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			AND deviceValue.name is null
			GROUP BY deviceValue.name, date_trunc('month', deviceValue.dateValue)""", [device: device,
					dateDebut: DateUtils.firstDayInMonth(dateReference), dateFin: DateUtils.lastTimeInDay(DateUtils.lastDayInMonth(dateReference))]))

		// traite ensuite les index
		values.addAll(DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('month', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			(max(deviceValue.value) - min(deviceValue.value)) as sum)
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			AND deviceValue.name in (:metaNames)
			GROUP BY deviceValue.name, date_trunc('month', deviceValue.dateValue)""", [device: device,
					dateDebut: DateUtils.firstDayInMonth(dateReference), dateFin: DateUtils.lastTimeInDay(DateUtils.lastDayInMonth(dateReference)),
					metaNames: ['hchp', 'hchc', 'base']]))

		return values
	}


	/** (non-Javadoc)
	 *
	 * @see smarthome.automation.deviceType.Compteur#parseIndex(smarthome.automation.SaisieIndexCommand)
	 */
	@Override
	void parseIndex(SaisieIndexCommand command) throws SmartHomeException {

	}

}
