package smarthome.automation.deviceType

import java.util.Date;
import java.util.List;
import java.util.Map;

import smarthome.automation.ChartTypeEnum;
import smarthome.automation.ChartViewEnum;
import smarthome.automation.DataModifierEnum;
import smarthome.automation.DeviceChartCommand;
import smarthome.automation.DeviceMetadata;
import smarthome.automation.DeviceTypeProvider;
import smarthome.automation.DeviceTypeProviderPrix;
import smarthome.automation.DeviceValue;
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
	 * @see smarthome.automation.deviceType.AbstractDeviceType#chartMetaNames()
	 */
	@Override
	public String chartMetaNames(DeviceChartCommand command) {
		if (command.viewMode == ChartViewEnum.day) {
			return "null,hcinst,hpinst"
		} else {
			return "null,hchc,hchp"
		}
	}


	/**
	 * important de surcharger cette boite car le graphe teleinfo a son propre builder de chart
	 * sinon le graphe sera créé une 1ere fois dans la méthode parent
	 * 
	 * @see smarthome.automation.deviceType.AbstractDeviceType#googleChart(smarthome.automation.DeviceChartCommand, java.util.List)
	 */
	@Override
	GoogleChart googleChart(DeviceChartCommand command, List<DeviceValue> values) {
		GoogleChart chart = new GoogleChart()
		chart.values = values
		return chart
	}
	
	
	/**
	 * Construction d'un graphe avec les tarifs
	 * 
	 * @param command
	 * @param values
	 * @return
	 */
	GoogleChart googleChartTarif(DeviceChartCommand command, List<DeviceValue> values) {
		GoogleChart chart = new GoogleChart()
		chart.values = []
		
		if (command.viewMode == ChartViewEnum.day) {
			chart.chartType = "LineChart"
			
			chart.aggregateValues = values.findAll { it.name == 'hcinst' || it.name == 'hpinst' }.collect {
				def kwh = it.value / 1000.0
				def prix = command.deviceImpl.calculTarif(it.name.substring(0, 2), kwh, it.dateValue[Calendar.YEAR])
				
				return [name: it.name == 'hcinst' ? 'HC' : 'HP', dateValue: it.dateValue, prix: prix, kwh: kwh]
			}
			chart.values = chart.aggregateValues.groupBy { it.dateValue }
			
			chart.colonnes = [
				new GoogleDataTableCol(label: "Date", type: "datetime", value: { deviceValue, index, currentChart ->
					deviceValue.key
				}),
				new GoogleDataTableCol(label: "Heures creuses (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find { it.name == 'HC' }?.prix
				}),
				new GoogleDataTableCol(label: "Heures pleines (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find { it.name == 'HP' }?.prix
				})
			]
		} else if (command.viewMode == ChartViewEnum.month) {
			chart.aggregateValues = values.findAll { it.name == 'hchc' || it.name == 'hchp' }.collect {
				def kwh = (it.max - it.min) / 1000.0
				def prix = command.deviceImpl.calculTarif(it.name.substring(0, 2), kwh, it.day[Calendar.YEAR])
				
				return [name: it.name == 'hchc' ? 'HC' : 'HP', dateValue: it.day, prix: prix, kwh: kwh]
			}
			chart.values = chart.aggregateValues.groupBy { it.dateValue }
			
			chart.colonnes = [
				new GoogleDataTableCol(label: "Date", type: "date", value: { deviceValue, index, currentChart ->
					deviceValue.key
				}),
				new GoogleDataTableCol(label: "Heures creuses (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find { it.name == 'HC' }?.prix
				}),
				new GoogleDataTableCol(label: "Heures pleines (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find { it.name == 'HP' }?.prix
				}),
				new GoogleDataTableCol(label: "Total (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.sum { it.prix ?: 0d }
				})
			]
		} else if (command.viewMode == ChartViewEnum.year) {
			chart.aggregateValues = values.findAll { it.name == 'hchc' || it.name == 'hchp' }.collect {
				Date dateValue = new Date().clearTime()
				dateValue[Calendar.DAY_OF_MONTH] = 1
				dateValue[Calendar.MONTH] = it.month - 1
				dateValue[Calendar.YEAR] = it.year
				def kwh = (it.max - it.min) / 1000.0
				def prix = command.deviceImpl.calculTarif(it.name.substring(0, 2), kwh, it.year)
				
				return [name: it.name == 'hchc' ? 'HC' : 'HP', dateValue: dateValue, prix: prix, kwh: kwh]
			}
			chart.values = chart.aggregateValues.groupBy { it.dateValue }
			
			chart.colonnes = [
				new GoogleDataTableCol(label: "Date", type: "date", value: { deviceValue, index, currentChart ->
					deviceValue.key
				}),
				new GoogleDataTableCol(label: "Heures creuses (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find { it.name == 'HC' }?.prix
				}),
				new GoogleDataTableCol(label: "Heures pleines (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.find { it.name == 'HP' }?.prix
				}),
				new GoogleDataTableCol(label: "Total (€)", type: "number", value: { deviceValue, index, currentChart ->
					deviceValue.value.sum { it.prix ?: 0d }
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
		// si le device n'existe pas encore, il n'y a donc pas d'anciennes valeurs
		// pour calculer la dernière conso
		if (device.id) {
			// calcul conso heure creuse sur la période
			def hc = device.metavalue("hchc")
			device.addMetavalue("hcinst", [value: "0", label: "Période heures creuses (Wh)", trace: true])
			
			if (hc) {
				// récupère la dernière valeur hchc
				def lastHC = DeviceValue.findAllByDeviceAndName(device, "hchc", [sort: "dateValue", order: "desc", max: 1])
				
				if (lastHC) {
					def conso = hc.value.toLong() - lastHC[0].value.toLong()
					device.addMetavalue("hcinst", [value: conso.toString()])
				}
			}
	
			// calcul conso heure pleine sur la période
			def hp = device.metavalue("hchp")
			device.addMetavalue("hpinst", [value: "0", label: "Période heures pleines (Wh)", trace: true])
			
			if (hp) {
				// récupère la dernièer valeur hchc
				def lastHP = DeviceValue.findAllByDeviceAndName(device, "hchp", [sort: "dateValue", order: "desc", max: 1])
						
				if (lastHP) {
					def conso = hp.value.toLong() - lastHP[0].value.toLong()
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
}
