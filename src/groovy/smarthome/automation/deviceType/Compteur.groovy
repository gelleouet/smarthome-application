package smarthome.automation.deviceType

import java.util.Date
import java.util.List
import java.util.Map

import smarthome.automation.ChartViewEnum
import smarthome.automation.DeviceChartCommand
import smarthome.automation.DeviceTypeProvider
import smarthome.automation.DeviceTypeProviderPrix
import smarthome.automation.DeviceValue
import smarthome.automation.DeviceValueDay
import smarthome.automation.DeviceValueMonth
import smarthome.automation.HouseConso
import smarthome.core.DateUtils
import smarthome.core.SmartHomeException
import smarthome.core.chart.GoogleChart
import smarthome.core.chart.GoogleDataTableCol

/**
 * Compteur
 * 
 * @author gregory
 *
 */
class Compteur extends AbstractDeviceType {
	protected DeviceTypeProvider fournisseurCache
	protected String contratCache


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

		chart.colonnes = [new GoogleDataTableCol(label: "Date", type: "datetime", property: "dateValue"), new GoogleDataTableCol(label: "Index", property: "value", type: "number")]

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
	 * Charge les prix pour une année donnée et les renvoit indexés dans une map en
	 * fonction de l'option tarifaire
	 *
	 * @param annee
	 * @return
	 */
	final Map listTarifAnnee(int annee) {
		if (tarifCache != null) {
			return tarifCache
		}

		tarifCache = [:]
		DeviceTypeProvider provider = getFournisseur()

		if (provider) {
			String contrat = getContrat()

			if (contrat) {
				DeviceTypeProviderPrix.findAllByDeviceTypeProviderAndContratAndAnnee(provider, contrat, annee).each {
					tarifCache.put(it.period, it.prixUnitaire)
				}
			}
		}

		return tarifCache
	}


	/**
	 * Calcul d'un tarif pour une période donnée
	 *
	 * @param period
	 * @param quantite
	 * @param annee
	 * @return
	 */
	Double calculTarif(String period, double quantite, int annee) {
		Double prixUnitaire = listTarifAnnee(annee)?.get(period.toUpperCase())

		if (prixUnitaire != null) {
			return ((prixUnitaire * quantite) as Double).round(2)
		}

		return null
	}


	/**
	 * Retourne le fournisseur du contrat
	 *
	 * @return
	 */
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
	String getContrat() {
		return "BASE"
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
	 * Unité pour les widgets (peut être différent)
	 *
	 * @return
	 */
	String defaultUnite() {

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


	/**
	 * @see smarthome.automation.deviceType.AbstractDeviceType.prepateMetaValuesForSave()
	 */
	@Override
	def prepareMetaValuesForSave(def datas) {
		Date dateInf = device.dateValue - 1

		// si le device n'existe pas encore, il n'y a donc pas d'anciennes valeurs
		// pour calculer la dernière conso
		if (device.id) {
			// les consos intermédiaires sont désormais calculées par l'agent (à cause du offline)
			// ce test sert à calculer la conso si pas envoyée par un ancien agent
			if (!datas.metavalues?.conso) {
				device.addMetavalue("conso", [value: "0", label: "Période consommation", trace: true])

				// récupère la dernière valeur principale (le dernier index)
				def lastIndex = DeviceValue.lastValueInPeriod(device, dateInf, device.dateValue)

				if (lastIndex) {
					def conso = device.value.toLong() - lastIndex.value.toLong()
					device.addMetavalue("conso", [value: conso.toString()])
				}
			}
		}
	}
}
