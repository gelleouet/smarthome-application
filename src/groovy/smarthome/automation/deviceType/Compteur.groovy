package smarthome.automation.deviceType

import java.util.Date
import java.util.List
import java.util.Map

import groovy.time.TimeCategory
import smarthome.automation.ChartTypeEnum
import smarthome.automation.ChartViewEnum
import smarthome.automation.CompteurIndex
import smarthome.automation.DeviceChartCommand
import smarthome.automation.DeviceTypeProvider
import smarthome.automation.DeviceTypeProviderPrix
import smarthome.automation.DeviceValue
import smarthome.automation.DeviceValueCommand
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

	protected static final String AGGREGATE_METRIC_NAME = "sum"
	protected static final String META_METRIC_NAME = "conso"
	static final String DEFAULT_CONTRAT = "BASE"
	static final String DEFAULT_FOURNISSEUR = "Tarifs réglementés"

	protected static final Map CONTRATS = [
		(DEFAULT_CONTRAT): 'Heures de base'
	]

	public static final SERIES_COLOR = [
		'conso': '#47bac1',
		'total': '#d8dadc'
	]

	protected DeviceTypeProvider fournisseurCache
	protected String contratCache


	/**
	 * Liste des contrats
	 * 
	 * @return
	 */
	static Map contrats() {
		CONTRATS
	}


	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#viewChart()
	 */
	@Override
	String viewChart() {
		'/deviceType/compteur/compteurChart'
	}


	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#values()
	 */
	@Override
	List values(DeviceChartCommand command) throws SmartHomeException {
		def values = []

		// pour chaque vue, il n'y a qu'une seule métrique à charger
		// la metavalue "META_METRIC_NAME" avec la conso horaire entre chaque prise de mesure
		// pour les vues agrégées, la fonction de calcul repart des index (métrique principale
		// sans nom) pour recalculer les consos entre 2 index de la même période.
		// sans ces 2 vues, il n'y a donc qu'une métrique appelée "AGGREGATE_METRIC_NAME"

		if (command.viewMode == ChartViewEnum.day) {
			values = DeviceValue.values(command.device, command.dateDebut(), command.dateFin(),
					META_METRIC_NAME)
		} else if (command.viewMode == ChartViewEnum.month) {
			values = DeviceValueDay.values(command.device, command.dateDebut(), command.dateFin(),
					aggregateMetaName())
		} else if (command.viewMode == ChartViewEnum.year) {
			values = DeviceValueMonth.values(command.device, command.dateDebut(), command.dateFin(),
					aggregateMetaName())
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
		chart.title = device.label
		chart.chartType = ChartTypeEnum.Combo.factory
		chart.selectionField = "selectionConso"

		String unite = this.uniteByView(command.viewMode)

		chart.vAxis << [title: "Consommation (${ unite })"]

		chart.colonnes << new GoogleDataTableCol(label: "Date", type: "datetime", property: "dateValue")
		chart.colonnes << new GoogleDataTableCol(label: "Heures base ($unite)", type: "number", value: { deviceValue, index, currentChart ->
			valueByView(deviceValue.value, command.viewMode)
		})

		if (command.viewMode == ChartViewEnum.day) {
			chart.series << [type: 'steppedArea', color: SERIES_COLOR.conso]
		} else {
			chart.series << [type: 'bars', color: SERIES_COLOR.conso, annotation: true]
		}

		return chart
	}


	/**
	 * Construction d'un graphe avec les tarifs
	 * Les impls doivent formattées les valeurs en une map contenant en clé la date
	 * et différents champs pour les prix par période tarifaire. Celles par défaut 
	 * sont 'kwh' pour la conso et 'prix' . Grâce à ce format, les données seront
	 * compatibles pour s'afficher aussi en format Table avec tout le détail
	 *
	 * @param command
	 * @param values
	 * @return
	 */
	GoogleChart googleChartTarif(DeviceChartCommand command, def values) {
		GoogleChart chart = new GoogleChart()
		command.device.extrasToJson()
		chart.title = device.label
		chart.chartType = ChartTypeEnum.Combo.factory
		chart.selectionField = "selectionCout"
		def contrat = getContrat()

		chart.vAxis << [title: 'Coût (€)']

		// transforme les valeurs en Map. Avec cette impl, il n'y qu'une seule
		// métrique chargée. donc le regroupement de renvoit qu'une seule valeur
		// par groupe
		chart.values = values.groupBy { it.dateValue }.collectEntries { entry ->
			def conso = entry.value[0].value
			[(entry.key): [kwh: conso, prix: command.deviceImpl.calculTarif(contrat, conso, entry.key[Calendar.YEAR])]]
		}.sort { it.key }

		chart.colonnes << new GoogleDataTableCol(label: "Date", type: "datetime", property: "key")
		chart.colonnes << new GoogleDataTableCol(label: "Heures base", type: "number", pattern: "#.##", value: { deviceValue, index, currentChart ->
			deviceValue.value.prix
		})

		if (command.viewMode == ChartViewEnum.day) {
			chart.series << [type: 'steppedArea', color: SERIES_COLOR.conso]
		} else {
			chart.series << [type: 'bars', color: SERIES_COLOR.conso, annotation: true]
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
		}

		// recherche d'un fournisseur par défaut
		if (!fournisseurCache) {
			fournisseurCache = DeviceTypeProvider.findByLibelle(DEFAULT_FOURNISSEUR)
		}

		return fournisseurCache
	}


	/**
	 * Nom du contrat
	 */
	String getContrat() {
		if (contratCache != null) {
			return contratCache
		}

		String optionTarifaire = getOptTarif()

		if (optionTarifaire) {
			String intensiteSouscrite = getPuissanceSous()

			if (intensiteSouscrite) {
				contratCache = "${optionTarifaire}_${intensiteSouscrite}".toUpperCase() // ex : HC_60
			} else {
				contratCache = "${optionTarifaire}".toUpperCase() // ex : HC_60
			}
		} else {
			contratCache = DEFAULT_CONTRAT
		}

		return contratCache
	}


	/**
	 * Option tarifaire
	 *
	 * @return
	 */
	String getOptTarif() {
		return device.metavalue("opttarif")?.value ?: DEFAULT_CONTRAT // base, hc, ...
	}


	/**
	 * Puissance du contrat
	 * 
	 * @return
	 */
	String getPuissanceSous() {
		return device.metavalue("isousc")?.value // 60A, 45A, ...
	}


	/**
	 * Unité pour les widgets (peut être différent)
	 *
	 * @return
	 */
	String defaultUnite() {
		return device.metavalue(META_METRIC_NAME)?.unite
	}
	
	
	/**
	 * Les unités peuvent changer en fonction des vues
	 * 
	 * @param view
	 * @return
	 */
	String uniteByView(ChartViewEnum view) {
		defaultUnite()
	}
	
	
	/**
	 * Conversion des valeurs en fonction vue
	 * 
	 * @param value
	 * @param view
	 * @return
	 */
	Number valueByView(Number value, ChartViewEnum view) {
		value
	}
	
	
	/**
	 * Formattage avec unité en fonction vue
	 * 
	 * @param value
	 * @param view
	 * @return
	 */
	String formatByView(Number value, ChartViewEnum view) {
		if (value == null) {
			""
		} else {
			"${valueByView(value, view)}${uniteByView(view)}"
		}
	}


	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#aggregateValueDay(java.util.Date)
	 */
	@Override
	List aggregateValueDay(Date dateReference) {
		// calcule les consos à partir des consos par période
		def values = DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('day', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			sum(deviceValue.value) as ${AGGREGATE_METRIC_NAME})
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			AND deviceValue.name in (:metaNames)
			GROUP BY date_trunc('day', deviceValue.dateValue), deviceValue.name""", [device: device,
					dateDebut: DateUtils.firstTimeInDay(dateReference),
					dateFin: DateUtils.lastTimeInDay(dateReference), metaNames: [META_METRIC_NAME]])
		
		return values
	}



	/**
	 * (non-Javadoc)
	 * @see smarthome.automation.deviceType.AbstractDeviceType#aggregateValueMonth(java.util.Date)
	 */
	@Override
	List aggregateValueMonth(Date dateReference) {
		// calcule les consos à partir des consos par période
		def values = DeviceValue.executeQuery("""\
			SELECT new map(date_trunc('month', deviceValue.dateValue) as dateValue, deviceValue.name as name,
			sum(deviceValue.value) as ${AGGREGATE_METRIC_NAME})
			FROM DeviceValue deviceValue
			WHERE deviceValue.device = :device
			AND deviceValue.dateValue BETWEEN :dateDebut AND :dateFin
			AND deviceValue.name in (:metaNames)
			GROUP BY date_trunc('month', deviceValue.dateValue), deviceValue.name""", [device: device,
					dateDebut: DateUtils.firstDayInMonth(dateReference),
					dateFin: DateUtils.lastTimeInDay(DateUtils.lastDayInMonth(dateReference)),
					metaNames: [META_METRIC_NAME]])
		
		
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
				addDefaultMetas()

				// récupère la dernière valeur principale (le dernier index)
				def lastIndex = DeviceValue.lastValueInPeriod(device, dateInf, device.dateValue)

				if (lastIndex) {
					def conso = device.value.toLong() - lastIndex.value.toLong()
					device.addMetavalue(META_METRIC_NAME, [value: conso.toString()])
				}
			}
		}
	}


	/**
	 * Ajout des metavalues et metadatas par défaut pour initialiser le
	 * device correctement
	 */
	protected void addDefaultMetas() {
		device.addMetavalue(META_METRIC_NAME, [value: "0", label: "Période consommation", trace: true])
	}


	/**
	 * La clé des données aggrégées
	 * Par défaut : les consos sont ajoutées pour trouver les valeurs aggrégées
	 * 
	 * @return
	 */
	protected String aggregateMetaName() {
		"${META_METRIC_NAME}${AGGREGATE_METRIC_NAME}"
	}


	/**
	 * Les noms des metavalue associées aux consos
	 * 
	 * @return
	 */
	protected List consoAggregateMetanames() {
		[aggregateMetaName()]
	}

	/**
	 * Les consos du jour
	 *
	 * @return
	 */
	Map consosJour(Date currentDate = null) {
		def consos = [optTarif: getOptTarif()]
		currentDate = currentDate ?: new Date()
		def currentYear = currentDate[Calendar.YEAR]
		Date firstHour = DateUtils.firstTimeInDay(currentDate)
		Date lastHour = DateUtils.lastTimeInDay(currentDate)

		consos.base = ((DeviceValue.values(device, firstHour, lastHour, META_METRIC_NAME).sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
		consos.total = consos.base

		consos.tarifBASE = calculTarif(DEFAULT_CONTRAT, consos.base, currentYear)
		consos.tarifTotal = consos.tarifBASE

		return consos
	}


	/**
	 * Consommation moyenne par jour sur une période
	 *
	 * @param dateStart
	 * @param dateEnd
	 * @return
	 */
	Double consoMoyenneJour(Date dateStart, Date dateEnd) {
		Double consoMoyenne
		def consos = [optTarif: getOptTarif()]
		dateStart = dateStart.clearTime()
		dateEnd = dateEnd.clearTime()
		int duree = (dateEnd - dateStart) + 1

		if (duree) {
			def base = ((DeviceValueDay.values(device, dateStart, dateEnd, aggregateMetaName()).sum { it.value } ?: 0.0) / 1000.0 as Double).round(1)
			consoMoyenne = (base / duree as Double).round(1)
		}

		return consoMoyenne
	}


	/**
	 * Les consos du mois en map indexé par le type d'heure (HC, HP, BASE, etc.)
	 *
	 * @return
	 */
	Map consosMois(Date currentDate = null) {
		def consos = [optTarif: getOptTarif()]
		currentDate = currentDate ?: new Date()
		def currentYear = currentDate[Calendar.YEAR]
		Date firstDayMonth = DateUtils.firstDayInMonth(currentDate)
		Date lastDayMonth = DateUtils.lastDayInMonth(currentDate)

		consos.base = (valueByView(DeviceValueDay.values(device, firstDayMonth, lastDayMonth, aggregateMetaName()).sum { it.value } ?: 0.0, ChartViewEnum.month) as Double).round(1)
		consos.total = consos.base

		consos.tarifBASE = calculTarif(DEFAULT_CONTRAT, consos.base, currentYear)
		consos.tarifTotal = consos.tarifBASE

		return consos
	}


	/**
	 * Les consos du mois en map indexé par le type d'heure (HC, HP, BASE, etc.)
	 *
	 * @return
	 */
	Map consosAnnee(Date currentDate = null) {
		def consos = [optTarif: getOptTarif()]
		currentDate = currentDate ?: new Date()
		def currentYear = currentDate[Calendar.YEAR]
		Date firstDayYear = DateUtils.firstDayInYear(currentDate)
		Date lastDayYear = DateUtils.lastDayInYear(currentDate)

		consos.base = (valueByView(DeviceValueMonth.values(device, firstDayYear, lastDayYear, aggregateMetaName()).sum { it.value } ?: 0.0, ChartViewEnum.year) as Double).round(1)
		consos.total = consos.base

		consos.tarifBASE = calculTarif(DEFAULT_CONTRAT, consos.base, currentYear)
		consos.tarifTotal = consos.tarifBASE

		return consos
	}


	/**
	 * Parse une saisie d'index par un user pour mettre à jour l'objet device associé
	 * Par défaut l'index est enregisré dans la valeur principale
	 * 
	 * @param index
	 * @throws SmartHomeException
	 */
	void parseIndex(CompteurIndex index) throws SmartHomeException {
		// met à jour la valeur principale
		device.value = (index.index1 as Long).toString()

		// essaie de calculer une conso sur la période si un ancien index est trouvé
		DeviceValue lastIndex = lastIndex()
		addDefaultMetas()

		if (lastIndex) {
			def conso = (index.index1 - lastIndex.value) as Long
			device.addMetavalue(META_METRIC_NAME, [value: conso.toString()])
		}
	}


	/**
	 * Le dernier index enregistré sur le device
	 * Par défaut, la value principale (sans name) est associé à l'index
	 * 
	 * @return
	 */
	DeviceValue lastIndex() {
		// si aucune date sur le device, alors aucune value
		if (device.dateValue) {
			// on balaye max sur 1 an pour éviter de scanner toute la base
			// même si index sur les champs requetes
			return DeviceValue.createCriteria().get {
				eq 'device', device
				ge 'dateValue', DateUtils.incYear(new Date(), -1)
				isNull 'name'
				maxResults 1
				order 'dateValue', 'desc'
			}
		} else {
			return null
		}
	}


	/**
	 * Les consos journalières totales sur une période
	 * Si le contrat a plusieurs tarifs, les consos sont ajoutées pour avoir
	 * le total à la journée
	 * 
	 * @param dateDebut
	 * @param dateFin
	 * @return
	 */
	List consommationTotalByDay(Date dateDebut, Date dateFin) {
		return DeviceValueDay.createCriteria().list() {
			between 'dateValue', dateDebut, dateFin
			eq 'device', device
			'in' 'name', consoAggregateMetanames()
			projections {
				sum 'value', 'value'
				groupProperty 'dateValue', 'dateValue'
			}
			order 'dateValue'
			resultTransformer org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP
		}
	}
	
	
	/**
	 * Charge les index d'un compteur du plus récent au plus vieux
	 * @see Compteur#parseIndex : par défaut, il est enregistré dans la valeur principale (name = null)
	 * 
	 * @param command
	 * @return
	 */
	List listIndex(DeviceValueCommand command) throws SmartHomeException {
		return DeviceValue.createCriteria().list(command.pagination) {
			eq 'device', device
			lt 'dateValue', command.dateIndex + 1
			isNull 'name'
			order 'dateValue', 'desc'
		}
		
	}

	
	/**
	 * Conversion et validation d'un nouvel index
	 * Faire uniquement les controles liés au compteur car le command
	 * fait déjà des controles de base (index nouveau > ancien, valeur négative)
	 * 
	 * @param command
	 */
	void bindCompteurIndex(CompteurIndex command) throws SmartHomeException {
		
	}
	
	
	/**
	 * Prépare l'objet pour édition dans formulaire
	 * 
	 * @param command
	 */
	void prepareForEdition(CompteurIndex command) {
		
	}
	
	
	/**
	 * Formattage HTML (mise en forme) d'un index
	 * 
	 * @param index
	 * @return
	 */
	String formatHtmlIndex(Double index) {
		"<span>${ (index as Long).toString() }</span>"
	}
}
