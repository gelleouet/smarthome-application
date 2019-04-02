package smarthome.automation.deviceType

import groovy.time.TimeCategory;

import java.util.Date;
import java.util.List;
import java.util.Map;

import smarthome.automation.ChartTypeEnum;
import smarthome.automation.ChartViewEnum;
import smarthome.automation.DataModifierEnum;
import smarthome.automation.Device;
import smarthome.automation.DeviceChartCommand;
import smarthome.automation.DeviceMetadata;
import smarthome.automation.DeviceTypeProvider;
import smarthome.automation.DeviceTypeProviderPrix;
import smarthome.automation.DeviceValue;
import smarthome.automation.DeviceValueDay;
import smarthome.automation.DeviceValueMonth;
import smarthome.automation.HouseConso;
import smarthome.core.DateUtils;
import smarthome.core.SmartHomeException;
import smarthome.core.chart.GoogleChart;
import smarthome.core.chart.GoogleDataTableCol;

/**
 * Périphérique Télé-info EDF
 * 
 * @author gregory
 *
 */
class TeleInformation extends AbstractDeviceType {
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
		} else {
			values = super.values(command)
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
			} else {
				chart.colonnes << new GoogleDataTableCol(label: "Heures bases (Wh)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find{ it.name == "baseinst" }?.value
				})
			}
			
			chart.colonnes << new GoogleDataTableCol(label: "Puissance max (W)", type: "number", value: { deviceValue, index, currentChart ->
				(deviceValue.value.find{ !it.name }?.value ?: 0) * 220
			})
			
		} else {
			chart.values = values
			
			chart.colonnes = []
			chart.colonnes << new GoogleDataTableCol(label: "Date", type: "datetime", property: "key")
			
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
			} else {
				chart.colonnes << new GoogleDataTableCol(label: "Heures bases (kWh)", type: "number", value: { deviceValue, index, currentChart ->
					def value = deviceValue.value.find{ it.name == "basesum" }?.value
					if (value != null) {
						return (value / 1000d).round(1)
					} else {
						return null
					}
				})
			}
			
			if (!command.comparePreviousYear) {
				chart.colonnes << new GoogleDataTableCol(label: "Puissance max (W)", type: "number", value: { deviceValue, index, currentChart ->
					(deviceValue.value.find{ it.name == "max" }?.value ?:0) * 220
				})
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
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'creuses' : 'normales' } (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixHC"]
				})
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'pleines' : 'pointe mobile' } (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixHP"]
				})
			} else {
				chart.colonnes << new GoogleDataTableCol(label: "Heures bases (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixBASE"]
				})
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
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'creuses' : 'normales' } (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixHC"]
				})
				chart.colonnes << new GoogleDataTableCol(label: "Heures ${ opttarif == 'HC' ? 'pleines' : 'pointe mobile' } (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixHP"]
				})
				chart.colonnes <<  new GoogleDataTableCol(label: "Total (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value["prix"]
				})
			} else {
				chart.colonnes << new GoogleDataTableCol(label: "Heures bases (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixBASE"]
				})
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
	 * Retourne le fournisseur du contrat
	 *
	 * @return
	 */
	@Override
	DeviceTypeProvider getFournisseur() {
		if (fournisseurCache != null) {
			return fournisseurCache	
		}
		
		// cherche le fournisseur dans les metadonnées
		String libelle = device.metadata("fournisseur")?.value
		
		if (libelle) {
			fournisseurCache = DeviceTypeProvider.findByLibelle(libelle)
			return fournisseurCache
		}
		
		return null
	}


	/**
	 * Nom du contrat
	 */
	@Override
	String getContrat() {
		if (contratCache != null) {
			return contratCache
		}
		
		String optionTarifaire = device.metavalue("opttarif")?.value // base, hc, ...
		
		if (optionTarifaire) {
			// le isousc n'est pas toujours fourni (ie module sans fil TIC)
			String intensiteSouscrite = device.metavalue("isousc")?.value // 60A, 45A, ...
			
			if (intensiteSouscrite) {
				contratCache = "${optionTarifaire}_${intensiteSouscrite}".toUpperCase() // ex : HC_60
			} else {
				contratCache = "${optionTarifaire}".toUpperCase() // ex : HC_60
			}
			
			return contratCache
		}
		
		return null
	}
	
	
	/**
	 * Option tarifaire
	 * 
	 * @return
	 */
	String getOptTarif() {
		return device.metavalue("opttarif")?.value // base, hc, ...
	}
	
	
	/**
	 * Les consos du jour en map indexé par le type d'heure (HC, HP, BASE, etc.)
	 * 
	 * @return
	 */
	Map consosJour() {
		def consos = [optTarif: getOptTarif()]
		def currentDate = new Date()
		def currentYear = currentDate[Calendar.YEAR]
		
		if (consos.optTarif in ['HC', 'EJP']) {
			def first_hchp = DeviceValue.firstValueByDay(device, 'hchp')
			def last_hchp = DeviceValue.lastValueByDay(device, 'hchp') 
			def first_hchc = DeviceValue.firstValueByDay(device, 'hchc')
			def last_hchc = DeviceValue.lastValueByDay(device, 'hchc')
			consos.hchp = first_hchp?.value && last_hchp?.value ? (last_hchp.value - first_hchp.value) / 1000.0 : 0.0
			consos.hchc = first_hchc?.value && last_hchc?.value ? (last_hchc.value - first_hchc.value) / 1000.0 : 0.0
			consos.total = (consos.hchp + consos.hchc as Double).round(1)
			
			consos.tarifHP = calculTarif(consos.optTarif == 'HC' ? 'HP' : 'PM', consos.hchp, currentYear)
			consos.tarifHC = calculTarif(consos.optTarif == 'HC' ? 'HC' : 'HN', consos.hchc, currentYear)
			consos.tarifTotal = (consos.tarifHP != null || consos.tarifHC != null) ? (consos.tarifHP ?: 0.0) + (consos.tarifHC ?: 0.0) : null
		} else {
			def first_base = DeviceValue.firstValueByDay(device, 'base')
			def last_base = DeviceValue.lastValueByDay(device, 'base')
			consos.base = first_base?.value && last_base?.value ? (last_base.value - first_base.value) / 1000.0 : 0.0
			consos.total = (consos.base as Double).round(1)
			
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
	Map consosMois() {
		def consos = [optTarif: getOptTarif()]
		def currentDate = new Date()
		def currentYear = currentDate[Calendar.YEAR]
		
		if (consos.optTarif in ['HC', 'EJP']) {
			def first_hchp = DeviceValue.firstValueByMonth(device, 'hchp')
			def last_hchp = DeviceValue.lastValueByMonth(device, 'hchp')
			def first_hchc = DeviceValue.firstValueByMonth(device, 'hchc')
			def last_hchc = DeviceValue.lastValueByMonth(device, 'hchc')
			consos.hchp = first_hchp?.value && last_hchp?.value ? (last_hchp.value - first_hchp.value) / 1000.0 : 0.0
			consos.hchc = first_hchc?.value && last_hchc?.value ? (last_hchc.value - first_hchc.value) / 1000.0 : 0.0
			consos.total = (consos.hchp + consos.hchc as Double).round(1)
			
			consos.tarifHP = calculTarif(consos.optTarif == 'HC' ? 'HP' : 'PM', consos.hchp, currentYear)
			consos.tarifHC = calculTarif(consos.optTarif == 'HC' ? 'HC' : 'HN', consos.hchc, currentYear)
			consos.tarifTotal = (consos.tarifHP != null || consos.tarifHC != null) ? (consos.tarifHP ?: 0.0) + (consos.tarifHC ?: 0.0) : null
		} else {
			def first_base = DeviceValue.firstValueByMonth(device, 'base')
			def last_base = DeviceValue.lastValueByMonth(device, 'base')
			consos.base = first_base?.value && last_base?.value ? (last_base.value - first_base.value) / 1000.0 : 0.0
			consos.total = (consos.base as Double).round(1)
			
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
	Map consosAnnee() {
		def consos = [optTarif: getOptTarif()]
		def currentDate = new Date()
		def currentYear = currentDate[Calendar.YEAR]
		
		if (consos.optTarif in ['HC', 'EJP']) {
			def first_hchp = DeviceValue.firstValueByYear(device, 'hchp')
			def last_hchp = DeviceValue.lastValueByYear(device, 'hchp')
			def first_hchc = DeviceValue.firstValueByYear(device, 'hchc')
			def last_hchc = DeviceValue.lastValueByYear(device, 'hchc')
			consos.hchp = first_hchp?.value && last_hchp?.value ? (last_hchp.value - first_hchp.value) / 1000.0 : 0.0
			consos.hchc = first_hchc?.value && last_hchc?.value ? (last_hchc.value - first_hchc.value) / 1000.0 : 0.0
			consos.total = (consos.hchp + consos.hchc as Double).round(1)
			
			consos.tarifHP = calculTarif(consos.optTarif == 'HC' ? 'HP' : 'PM', consos.hchp, currentYear)
			consos.tarifHC = calculTarif(consos.optTarif == 'HC' ? 'HC' : 'HN', consos.hchc, currentYear)
			consos.tarifTotal = (consos.tarifHP != null || consos.tarifHC != null) ? (consos.tarifHP ?: 0.0) + (consos.tarifHC ?: 0.0) : null
		} else {
			def first_base = DeviceValue.firstValueByYear(device, 'base')
			def last_base = DeviceValue.lastValueByYear(device, 'base')
			consos.base = first_base?.value && last_base?.value ? (last_base.value - first_base.value) / 1000.0 : 0.0
			consos.total = (consos.base as Double).round(1)
			
			consos.tarifBASE = calculTarif('BASE', consos.base, currentYear)
			consos.tarifTotal = consos.tarifBASE
		}
		
		return consos
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
}
