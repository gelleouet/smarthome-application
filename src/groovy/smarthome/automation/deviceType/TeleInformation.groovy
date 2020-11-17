package smarthome.automation.deviceType

import groovy.time.TimeCategory

import java.util.Date
import java.util.List
import java.util.Map

import smarthome.automation.ChartTypeEnum
import smarthome.automation.ChartViewEnum
import smarthome.automation.CompteurIndex
import smarthome.automation.DataModifierEnum
import smarthome.automation.Device
import smarthome.automation.DeviceChartCommand
import smarthome.automation.DeviceMetadata
import smarthome.automation.DeviceTypeProvider
import smarthome.automation.DeviceTypeProviderPrix
import smarthome.automation.DeviceValue
import smarthome.automation.DeviceValueCommand
import smarthome.automation.DeviceValueDay
import smarthome.automation.DeviceValueMonth
import smarthome.automation.HouseConso
import smarthome.core.CompteurUtils
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
		'total': '#d8dadc' //'#e8eaed'
	]

	protected static final Map CONTRATS = [
		(DEFAULT_CONTRAT): 'Heures de base',
		'HC': 'Heures creuses / pleines',
		'EJP': 'Heures pointe mobile / normales'
	]



	/**
	 * Liste des contrats
	 *
	 * @return
	 */
	static Map contrats() {
		return CONTRATS
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
		chart.title = device.label
		chart.chartType = ChartTypeEnum.Combo.factory
		chart.selectionField = "selectionConso"

		String unite = this.uniteByView(command.viewMode)
		
		if (command.viewMode == ChartViewEnum.day) {
			chart.values = values.groupBy { it.dateValue }

			chart.colonnes = []
			chart.colonnes << new GoogleDataTableCol(label: "Date", type: "datetime", property: "key")

			if (isDoubleTarification(opttarif)) {
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'creuses' : 'normales' } ($unite)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find{ it.name == "hcinst" }?.value
				})
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'pleines' : 'pointe mobile' } ($unite)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find{ it.name == "hpinst" }?.value
				})

				chart.series << [type: 'steppedArea', color: SERIES_COLOR.hc, targetAxisIndex: 0]
				chart.series << [type: 'steppedArea', color: SERIES_COLOR.hp, targetAxisIndex: 0]
			} else {
				chart.colonnes << new GoogleDataTableCol(label: "Heures base ($unite)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find{ it.name == "baseinst" }?.value
				})
				chart.series << [type: 'steppedArea', color: SERIES_COLOR.base, targetAxisIndex: 0]
			}

			chart.colonnes << new GoogleDataTableCol(label: "Puissance max (W)", type: "number", value: { deviceValue, index, currentChart ->
				(deviceValue.value.find{ !it.name }?.value ?: 0) * coefPuissance
			})
			chart.series << [type: 'line', color: SERIES_COLOR.puissance, targetAxisIndex: 1]

			chart.vAxis << [title: "Consommation ($unite)"]
			chart.vAxis << [title: 'Puissance (W)']
		} else {
			chart.values = values
			chart.isStacked = true

			chart.colonnes = []
			chart.colonnes << new GoogleDataTableCol(label: "Date", type: "date", property: "key")

			if (isDoubleTarification(opttarif)) {
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'creuses' : 'normales' } ($unite)", type: "number", value: { deviceValue, index, currentChart ->
					def value = deviceValue.value.find{ it.name == "hchcsum" }?.value
					if (value != null) {
						return (value / 1000d).round(1)
					} else {
						return null
					}
				})
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'pleines' : 'pointe mobile' } ($unite)", type: "number", value: { deviceValue, index, currentChart ->
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
				chart.colonnes << new GoogleDataTableCol(label: "Heures base ($unite)", type: "number", value: { deviceValue, index, currentChart ->
					def value = deviceValue.value.find{ it.name == "basesum" }?.value
					if (value != null) {
						return (value / 1000d).round(1)
					} else {
						return null
					}
				})
				chart.series << [type: 'bars', color: SERIES_COLOR.base, targetAxisIndex: 0, annotation: true]
			}

			chart.vAxis << [title: "Consommation ($unite)"]

			if (!command.comparePreviousYear) {
				chart.colonnes << new GoogleDataTableCol(label: "Puissance max (W)", type: "number", value: { deviceValue, index, currentChart ->
					(deviceValue.value.find{ it.name == "max" }?.value ?:0) * coefPuissance
				})

				chart.series << [type: 'line', color: SERIES_COLOR.puissance, targetAxisIndex: 1, annotation: command.viewMode == ChartViewEnum.year]

				chart.vAxis << [title: 'Puissance (W)']
			} else {
				chart.chartType = ChartTypeEnum.Column.factory
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
		chart.title = device.label
		chart.chartType = ChartTypeEnum.Combo.factory
		chart.selectionField = "selectionCout"


		chart.vAxis << [title: 'Coût (€)']

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
						def kwh = deviceValue.value / 1000
						resultValues["kwh${name}"] = deviceValue.value
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

			if (isDoubleTarification(opttarif)) {
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
						resultValues["kwh${name}"] = deviceValue.value
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

			if (isDoubleTarification(opttarif)) {
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
	 * 
	 */
	@Override
	public String uniteByView(ChartViewEnum view) {
		if (view == ChartViewEnum.day) {
			"Wh"
		} else {
			"KWh"
		}
	}
	
	
	/**
	 *
	 */
	@Override
	public Number valueByView(Number value, ChartViewEnum view) {
		if (view == ChartViewEnum.day) {
			value
		} else {
			CompteurUtils.convertWhTokWh(value)
		}
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
		// pour les saisies manuelles, si un seul index par jour le mode de calcul par diff des index
		// ne peut pas marcher car une seule valeur et elle va s'annuler
		// on bascule avec la somme des consos qui est moins précise lorsque le téléinfo
		// est branché car si une coupure pendant un certain temps, il y a une perte d'info
		if (device.metadata('aggregate')?.value == 'sum-conso') {
			// ATTENTION !!! retransformer le name pour rester valide avec l'autre requete
			// et le chargement des données dans la méthode @see values
			values.addAll(DeviceValue.executeQuery("""\
				SELECT new map(date_trunc('day', deviceValue.dateValue) as dateValue,
				(case when deviceValue.name = 'hpinst' then 'hchp' when deviceValue.name = 'hcinst' then 'hchc' else 'base' end) as name,
				sum(deviceValue.value) as sum)
				FROM DeviceValue deviceValue
				WHERE deviceValue.device = :device
				AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
				AND deviceValue.name in (:metaNames)
				GROUP BY deviceValue.name, date_trunc('day', deviceValue.dateValue)""", [device: device,
						dateDebut: DateUtils.firstTimeInDay(dateReference), dateFin: DateUtils.lastTimeInDay(dateReference),
						metaNames: ['hpinst', 'hcinst', 'baseinst']]))
		} else {
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
		}

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
		// pour les saisies manuelles, si un seul index par jour le mode de calcul par diff des index
		// ne peut pas marcher car une seule valeur et elle va s'annuler
		// on bascule avec la somme des consos qui est moins précise lorsque le téléinfo
		// est branché car si une coupure pendant un certain temps, il y a une perte d'info
		if (device.metadata('aggregate')?.value == 'sum-conso') {
			// ATTENTION !!! retransformer le name pour rester valide avec l'autre requete
			// et le chargement des données dans la méthode @see values
			values.addAll(DeviceValue.executeQuery("""\
				SELECT new map(date_trunc('month', deviceValue.dateValue) as dateValue,
				(case when deviceValue.name = 'hpinst' then 'hchp' when deviceValue.name = 'hcinst' then 'hchc' else 'base' end) as name,
				sum(deviceValue.value) as sum)
				FROM DeviceValue deviceValue
				WHERE deviceValue.device = :device
				AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
				AND deviceValue.name in (:metaNames)
				GROUP BY deviceValue.name, date_trunc('month', deviceValue.dateValue)""", [device: device,
						dateDebut: DateUtils.firstDayInMonth(dateReference), dateFin: DateUtils.lastTimeInDay(DateUtils.lastDayInMonth(dateReference)),
						metaNames: ['hpinst', 'hcinst', 'baseinst']]))
		} else {
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
		}

		return values
	}


	/**
	 * (non-Javadoc)
	 *
	 * @see smarthome.automation.deviceType.Compteur#parseIndex(smarthome.automation.CompteurIndex)
	 */
	@Override
	void parseIndex(CompteurIndex index) throws SmartHomeException {
		// la valeur principale est la puissance instantanée. ici on ne la connait pas
		// mais on met une fausse valeur 0 car si null, le device ne sera pas historisé
		device.value = "0"

		// contrat double index
		if (isDoubleTarification()) {
			if (!index.index1 || !index.index2) {
				throw new SmartHomeException("Les 2 index sont nécessaires pour les contrats double tarif !")
			}

			Long indexHP = index.index1 as Long
			Long indexHC = index.index2 as Long
			
			// conserve les index
			device.addMetavalue("hchp", [value: indexHP.toString(), label: "Total heures pleines", trace: true, unite: "Wh"])
			device.addMetavalue("hchc", [value: indexHC.toString(), label: "Total heures creuses", trace: true, unite: "Wh"])

			// insère les metavalue pour conso période
			device.addMetavalue("hpinst", [value: "0", label: "Période heures pleines", trace: true, unite: "Wh"])
			device.addMetavalue("hcinst", [value: "0", label: "Période heures creuses", trace: true, unite: "Wh"])

			// essaie de calculer une conso sur la période si un ancien index est trouvé
			DeviceValue lastIndexHP = lastIndexHP()

			if (lastIndexHP) {
				def conso = (indexHP - lastIndexHP.value) as Long
				device.addMetavalue("hpinst", [value: conso.toString()])
			}

			DeviceValue lastIndexHC = lastIndexHC()

			if (lastIndexHC) {
				def conso = (indexHC - lastIndexHC.value) as Long
				device.addMetavalue("hcinst", [value: conso.toString()])
			}
		} else {
			Long indexBase = index.index1 as Long
			
			// conserve les index
			device.addMetavalue("base", [value: indexBase.toString(), label: "Total toutes heures", trace: true, unite: "Wh"])

			// insère les metavalue pour conso période
			device.addMetavalue("baseinst", [value: "0", label: "Période toutes heures", trace: true, unite: "Wh"])

			// essaie de calculer une conso sur la période si un ancien index est trouvé
			DeviceValue lastIndex = lastIndex()

			if (lastIndex) {
				def conso = (indexBase - lastIndex.value) as Long
				device.addMetavalue("baseinst", [value: conso.toString()])
			}
		}
	}


	/**
	 * Cas spécial pour la double tarification : l'index "principal" est le tarif 
	 * normal (heure pleine)
	 *
	 * @see smarthome.automation.deviceType.Compteur#lastIndex()
	 */
	@Override
	DeviceValue lastIndex() {
		if (device.dateValue) {
			// on balaye max sur 1 an pour éviter de scanner toute la base
			// même si index sur les champs requetes
			if (isDoubleTarification()) {
				return DeviceValue.createCriteria().get {
					eq 'device', device
					ge 'dateValue', DateUtils.incYear(new Date(), -1)
					eq 'name', 'hchp'
					maxResults 1
					order 'dateValue', 'desc'
				}
			} else {
				return DeviceValue.createCriteria().get {
					eq 'device', device
					ge 'dateValue', DateUtils.incYear(new Date(), -1)
					eq 'name', 'base'
					maxResults 1
					order 'dateValue', 'desc'
				}
			}
		} else {
			return null
		}
	}


	/**
	 * Pour les compteurs double tarif, renvoit l'index heure plein (ie l'index
	 * principal donc le même que lastIndex())
	 *
	 * @return
	 */
	DeviceValue lastIndexHP() {
		return lastIndex()
	}


	/**
	 * Pour les compteurs double tarif, renvoit l'index heure creuse
	 * 
	 * @return
	 */
	DeviceValue lastIndexHC() {
		if (device.dateValue && isDoubleTarification()) {
			// on balaye max sur 1 an pour éviter de scanner toute la base
			// même si index sur les champs requetes
			return DeviceValue.createCriteria().get {
				eq 'device', device
				ge 'dateValue', DateUtils.incYear(new Date(), -1)
				eq 'name', 'hchc'
				maxResults 1
				order 'dateValue', 'desc'
			}
		} else {
			return null
		}
	}


	/**
	 * Contrat double tarif ? (ie HC ou EJP)
	 * 
	 * @return
	 */
	boolean isDoubleTarification() {
		String opttarif = this.getOptTarif()
		return isDoubleTarification(opttarif)
	}



	/**
	 * Contrat double tarif ? (ie HC ou EJP)
	 * 
	 * @param opttarif
	 * @return
	 */
	boolean isDoubleTarification(String opttarif) {
		return opttarif in ["HC", "EJP"]
	}


	/**
	 * Les consos du jour en map indexé par le type d'heure (HC, HP, BASE, etc.)
	 *
	 * @return
	 */
	@Override
	Map consosJour(Date currentDate = null) {
		def consos = [optTarif: getOptTarif()]
		currentDate = currentDate ?: new Date()
		def currentYear = currentDate[Calendar.YEAR]
		Date firstHour = DateUtils.firstTimeInDay(currentDate)
		Date lastHour = DateUtils.lastTimeInDay(currentDate)

		if (isDoubleTarification()) {
			consos.hchp = ((DeviceValue.values(device, firstHour, lastHour, 'hpinst').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.hchc = ((DeviceValue.values(device, firstHour, lastHour, 'hcinst').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.total = (consos.hchp + consos.hchc as Double).round(1)

			consos.tarifHP = calculTarif(consos.optTarif == 'HC' ? 'HP' : 'PM', consos.hchp, currentYear)
			consos.tarifHC = calculTarif(consos.optTarif == 'HC' ? 'HC' : 'HN', consos.hchc, currentYear)
			consos.tarifTotal = (consos.tarifHP != null || consos.tarifHC != null) ? (consos.tarifHP ?: 0.0) + (consos.tarifHC ?: 0.0) : null
		} else {
			consos.base = ((DeviceValue.values(device, firstHour, lastHour, 'baseinst').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.total = consos.base

			consos.tarifBASE = calculTarif('BASE', consos.base, currentYear)
			consos.tarifTotal = consos.tarifBASE
		}

		return consos
	}


	/**
	 * Consommation moyenne par jour sur une période
	 *
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 */
	@Override
	Double consoMoyenneJour(Date dateStart, Date dateEnd) {
		Double consoMoyenne
		def consos = [optTarif: getOptTarif()]
		dateStart = dateStart.clearTime()
		dateEnd = dateEnd.clearTime()
		int duree = (dateEnd - dateStart) + 1

		if (duree) {
			if (isDoubleTarification()) {
				def hchp = ((DeviceValueDay.values(device, dateStart, dateEnd, 'hchpsum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
				def hchc = ((DeviceValueDay.values(device, dateStart, dateEnd, 'hchcsum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
				consoMoyenne = ((hchp + hchc) / duree as Double).round(1)
			} else {
				def base = ((DeviceValueDay.values(device, dateStart, dateEnd, 'basesum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
				consoMoyenne = (base / duree as Double).round(1)
			}
		}

		return consoMoyenne
	}


	/**
	 * Les consos du mois en map indexé par le type d'heure (HC, HP, BASE, etc.)
	 *
	 * @return
	 */
	@Override
	Map consosMois(Date currentDate = null) {
		def consos = [optTarif: getOptTarif()]
		currentDate = currentDate ?: new Date()
		def currentYear = currentDate[Calendar.YEAR]
		Date firstDayMonth = DateUtils.firstDayInMonth(currentDate)
		Date lastDayMonth = DateUtils.lastDayInMonth(currentDate)

		if (isDoubleTarification()) {
			consos.hchp = ((DeviceValueDay.values(device, firstDayMonth, lastDayMonth, 'hchpsum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.hchc = ((DeviceValueDay.values(device, firstDayMonth, lastDayMonth, 'hchcsum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.total = (consos.hchp + consos.hchc as Double).round(1)

			consos.tarifHP = calculTarif(consos.optTarif == 'HC' ? 'HP' : 'PM', consos.hchp, currentYear)
			consos.tarifHC = calculTarif(consos.optTarif == 'HC' ? 'HC' : 'HN', consos.hchc, currentYear)
			consos.tarifTotal = (consos.tarifHP != null || consos.tarifHC != null) ? (consos.tarifHP ?: 0.0) + (consos.tarifHC ?: 0.0) : null
		} else {
			consos.base = ((DeviceValueDay.values(device, firstDayMonth, lastDayMonth, 'basesum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.total = consos.base

			consos.tarifBASE = calculTarif('BASE', consos.base, currentYear)
			consos.tarifTotal = consos.tarifBASE
		}

		return consos
	}


	/**
	 * Les consos du mois en map indexé par le type d'heure (HC, HP, BASE, etc.)
	 *
	 * @return
	 */
	@Override
	Map consosAnnee(Date currentDate = null) {
		def consos = [optTarif: getOptTarif()]
		currentDate = currentDate ?: new Date()
		def currentYear = currentDate[Calendar.YEAR]
		Date firstDayYear = DateUtils.firstDayInYear(currentDate)
		Date lastDayYear = DateUtils.lastDayInYear(currentDate)

		if (isDoubleTarification()) {
			consos.hchp = ((DeviceValueMonth.values(device, firstDayYear, lastDayYear, 'hchpsum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.hchc = ((DeviceValueMonth.values(device, firstDayYear, lastDayYear, 'hchcsum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.total = (consos.hchp + consos.hchc as Double).round(1)

			consos.tarifHP = calculTarif(consos.optTarif == 'HC' ? 'HP' : 'PM', consos.hchp, currentYear)
			consos.tarifHC = calculTarif(consos.optTarif == 'HC' ? 'HC' : 'HN', consos.hchc, currentYear)
			consos.tarifTotal = (consos.tarifHP != null || consos.tarifHC != null) ? (consos.tarifHP ?: 0.0) + (consos.tarifHC ?: 0.0) : null
		} else {
			consos.base = ((DeviceValueMonth.values(device, firstDayYear, lastDayYear, 'basesum').sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consos.total = consos.base

			consos.tarifBASE = calculTarif('BASE', consos.base, currentYear)
			consos.tarifTotal = consos.tarifBASE
		}

		return consos
	}


	/**
	 * Les index sont saisis en kWh, il faut les convertir en Wh
	 */
	@Override
	void bindCompteurIndex(CompteurIndex command) throws SmartHomeException {
		if (command.highindex1 != null) {
			command.index1 = command.highindex1 * 1000
		}
		if (command.highindex2 != null) {
			command.index2 = command.highindex2 * 1000
		}
	}
	
	
	/**
	 * Prépare l'objet pour édition dans formulaire
	 * On convertit les index enregitrés en Wh en Kwh
	 *
	 * @param command
	 */
	@Override
	void prepareForEdition(CompteurIndex command) {
		if (command.index1 != null) {
			command.highindex1 = command.index1 / 1000
		} 
		if (command.index2 != null) {
			command.highindex2 = command.index2 / 1000
		}
	}


	/**
	 * Les index sont enregistrés en Wh.
	 * On formatte l'index en kWh
	 *
	 */
	@Override
	String formatHtmlIndex(Double index) {
		"""<span class="index-elec-text">${ (index / 1000).round() }</span>"""
	}
	
	
	/** 
	 * (non-Javadoc)
	 *
	 * @see smarthome.automation.deviceType.Compteur#consoAggregateMetanames()
	 */
	@Override
	protected List consoAggregateMetanames() {
		['basesum', 'hchcsum', 'hchpsum']
	}


	/**
	 * 
	 */
	@Override
	List listIndex(DeviceValueCommand command) throws SmartHomeException {
		List nameList
		
		if (isDoubleTarification()) {
			nameList = ['hchp', 'hchc']
		} else {
			nameList = ['base']
		}
		
		return DeviceValue.createCriteria().list(command.pagination) {
			eq 'device', device
			lt 'dateValue', command.dateIndex + 1
			'in' 'name', nameList
			order 'dateValue', 'desc'
			order 'name', 'asc'
		}
	}
	
}
