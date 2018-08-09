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
				"null,hcinst,hpinst")
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
		
		if (command.viewMode == ChartViewEnum.day) {
			chart.values = values.groupBy { it.dateValue }
			
			chart.colonnes = [
				new GoogleDataTableCol(label: "Date", type: "datetime", property: "key"),
				new GoogleDataTableCol(label: "Heures creuses (Wh)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find{ it.name == "hcinst" }?.value
				}),
				new GoogleDataTableCol(label: "Heures pleines (Wh)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find{ it.name == "hpinst" }?.value
				}),
				new GoogleDataTableCol(label: "Intensité (A)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find{ !it.name }?.value
				}),
			]
		} else {
			chart.values = values
			
			chart.colonnes = [
				new GoogleDataTableCol(label: "Date", type: "datetime", property: "key"),
				new GoogleDataTableCol(label: "Heures creuses (kWh)", type: "number", value: { deviceValue, index, currentChart -> 
					def value = deviceValue.value.find{ it.name == "hchcsum" }?.value
					if (value != null) {
						return (value / 1000d).round(1) 
					} else {
						return null
					}
				}),
				new GoogleDataTableCol(label: "Heures pleines (kWh)", type: "number", value: { deviceValue, index, currentChart ->
					def value = deviceValue.value.find{ it.name == "hchpsum" }?.value 
					if (value != null) {
						return (value / 1000d).round(1)
					} else {
						return null
					}
				}),
				new GoogleDataTableCol(label: "Intensité max (A)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find{ it.name == "max" }?.value 
				}),
			]
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
		
		if (command.viewMode == ChartViewEnum.day) {
			chart.chartType = "SteppedAreaChart"
			
			chart.values = values.collectEntries { entry ->
				Map resultValues = [:]
				entry.value.each { deviceValue ->
					if (deviceValue.name == 'hcinst' || deviceValue.name == 'hpinst') {
						def name = deviceValue.name == 'hcinst' ? 'HC' : 'HP'
						def kwh = deviceValue.value / 1000.0
						resultValues["kwh${name}"] = kwh
						resultValues["prix${name}"] = command.deviceImpl.calculTarif(name, kwh, deviceValue.dateValue[Calendar.YEAR])
					}
				}
				resultValues["kwh"] = (resultValues["kwhHC"]?:0d) + (resultValues["kwhHP"]?:0d)
				resultValues["prix"] = (resultValues["prixHC"]?:0d) + (resultValues["prixHP"]?:0d)
				[(entry.key): resultValues]
			}
			
			chart.colonnes = [
				new GoogleDataTableCol(label: "Date", type: "datetime", value: { deviceValue, index, currentChart ->
					deviceValue.key
				}),
				new GoogleDataTableCol(label: "Heures creuses (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixHC"]
				}),
				new GoogleDataTableCol(label: "Heures pleines (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixHP"]
				})
			]
		} else {
			chart.values = values.collectEntries { entry ->
				Map resultValues = [:]
				entry.value.each { deviceValue ->
					if (deviceValue.name == 'hchcsum' || deviceValue.name == 'hchpsum') {
						def name = deviceValue.name == 'hchcsum' ? 'HC' : 'HP'
						def kwh = deviceValue.value / 1000.0
						resultValues["kwh${name}"] = kwh
						resultValues["prix${name}"] = command.deviceImpl.calculTarif(name, kwh, deviceValue.dateValue[Calendar.YEAR])
					}
				}
				resultValues["kwh"] = (resultValues["kwhHC"]?:0d) + (resultValues["kwhHP"]?:0d)
				resultValues["prix"] = (resultValues["prixHC"]?:0d) + (resultValues["prixHP"]?:0d)
				[(entry.key): resultValues]
			}
			
			chart.colonnes = [
				new GoogleDataTableCol(label: "Date", type: "date", value: { deviceValue, index, currentChart ->
					deviceValue.key
				}),
				new GoogleDataTableCol(label: "Heures creuses (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixHC"]
				}),
				new GoogleDataTableCol(label: "Heures pleines (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value["prixHP"]
				}),
				new GoogleDataTableCol(label: "Total (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value["prix"]
				})
			]
		}
		
		return chart
	}



	/**
	 * @see smarthome.automation.deviceType.AbstractDeviceType.prepateMetaValuesForSave()
	 */
	@Override
	def prepareMetaValuesForSave() {
		Date dateInf
		
		use (TimeCategory) {
			dateInf = device.dateValue - 30.minutes
		}
		
		// si le device n'existe pas encore, il n'y a donc pas d'anciennes valeurs
		// pour calculer la dernière conso
		if (device.id) {
			// calcul conso heure creuse sur la période
			def hc = device.metavalue("hchc")
			device.addMetavalue("hcinst", [value: "0", label: "Période heures creuses (Wh)", trace: true])
			
			if (hc) {
				// récupère la dernière valeur hchc
				def lastHC = DeviceValue.lastValueInPeriod(device, dateInf, device.dateValue, "hchc") 
				
				if (lastHC) {
					def conso = hc.value.toLong() - lastHC.value.toLong()
					device.addMetavalue("hcinst", [value: conso.toString()])
				}
			}
	
			// calcul conso heure pleine sur la période
			def hp = device.metavalue("hchp")
			device.addMetavalue("hpinst", [value: "0", label: "Période heures pleines (Wh)", trace: true])
			
			if (hp) {
				// récupère la dernière valeur hchp
				def lastHP = DeviceValue.lastValueInPeriod(device, dateInf, device.dateValue, "hchp")  
						
				if (lastHP) {
					def conso = hp.value.toLong() - lastHP.value.toLong()
					device.addMetavalue("hpinst", [value: conso.toString()])
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
		String intensiteSouscrite = device.metavalue("isousc")?.value // 60A, 45A, ...
		
		if (optionTarifaire && intensiteSouscrite) {
			contratCache = "${optionTarifaire}_${intensiteSouscrite}".toUpperCase() // ex : HC_60
			return contratCache
		}
		
		return null
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
				metaNames: ['hchp', 'hchc']]))
		
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
				metaNames: ['hchp', 'hchc']]))
		
		return values
	}
}
