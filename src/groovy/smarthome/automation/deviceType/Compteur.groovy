package smarthome.automation.deviceType

import java.util.Date;
import java.util.List;

import smarthome.automation.ChartViewEnum;
import smarthome.automation.DeviceChartCommand;
import smarthome.automation.DeviceTypeProvider;
import smarthome.automation.DeviceValue;
import smarthome.automation.DeviceValueDay;
import smarthome.automation.DeviceValueMonth;
import smarthome.core.DateUtils;
import smarthome.core.SmartHomeException;
import smarthome.core.chart.GoogleChart;
import smarthome.core.chart.GoogleDataTableCol;

/**
 * Compteur
 * 
 * @author gregory
 *
 */
class Compteur extends AbstractDeviceType {
	/**
	 * @see smarthome.automation.deviceType.AbstractDeviceType.chartDataTemplate()
	 */
	@Override
	def chartDataTemplate() {
		'/deviceType/compteur/compteurChartDatas'
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
				"conso")
		} else if (command.viewMode == ChartViewEnum.month) {
			values = DeviceValueDay.values(command.device, command.dateDebut(), command.dateFin())
		} else if (command.viewMode == ChartViewEnum.year) {
			values = DeviceValueMonth.values(command.device, command.dateDebut(), command.dateFin())
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
		command.device.extrasToJson()
		chart.values = values
		
		chart.colonnes = [
			new GoogleDataTableCol(label: "Date", type: "datetime", property: "dateValue"),
			new GoogleDataTableCol(label: "Index", property: "value", type: "number")
		]
		
		// Convertit les valeurs si besoin
		def coefConversion = command.device.metadata('coefConversion')
		
		if (coefConversion?.value) {
			chart.values.each {
				it.value = it.value * coefConversion.value.toDouble()
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
		return "BASE"
	}



	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#aggregateValueDay(java.util.Date)
	 */
	@Override
	List aggregateValueDay(Date dateReference) {
		// calcule la conso du jour en repartant des index
		def values = DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('day', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			(max(deviceValue.value) - min(deviceValue.value)) as sum)
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			AND deviceValue.name is null
			GROUP BY deviceValue.name, date_trunc('day', deviceValue.dateValue)""", [device: device,
				dateDebut: DateUtils.firstTimeInDay(dateReference), dateFin: DateUtils.lastTimeInDay(dateReference)])
		
		return values
	}



	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#aggregateValueMonth(java.util.Date)
	 */
	@Override
	List aggregateValueMonth(Date dateReference) {
		// calcule la conso du mois en repartant des index
		def values = DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('month', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			(max(deviceValue.value) - min(deviceValue.value)) as sum)
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			AND deviceValue.name is null
			GROUP BY deviceValue.name, date_trunc('month', deviceValue.dateValue)""", [device: device,
				dateDebut: DateUtils.firstDayInMonth(dateReference), dateFin: DateUtils.lastTimeInDay(DateUtils.lastDayInMonth(dateReference))])
		
		return values
	}
}
