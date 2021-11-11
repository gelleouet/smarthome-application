package smarthome.automation.deviceType

import java.util.Date
import java.util.List
import java.util.Map

import org.codehaus.groovy.grails.commons.GrailsApplication

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
	// 3 pour au moins faire des saisies tous les 2 mois, au dela, ce n'est plus significatif
	protected static final int MAX_MONTH_SEARCH_INDEX = 3
	static final String DEFAULT_CONTRAT = "BASE"
	static final String DEFAULT_FOURNISSEUR = "Tarif moyen"
	

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
		chart.colonnes << new GoogleDataTableCol(label: "Consommation ($unite)", type: "number", value: { deviceValue, index, currentChart ->
			valueByView(deviceValue.value, command.viewMode)
		})

		if (command.viewMode == ChartViewEnum.day) {
			chart.series << [type: 'steppedArea', color: SERIES_COLOR.conso]
		} else {
			chart.series << [type: 'steppedArea', color: SERIES_COLOR.conso, annotation: true]
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
			def conso = this.convertValueForCalculPrix(entry.value[0].value)
			
			// !! IMPORTANT : ne pas passre dans la key kwh la valeur convertie car en fonction affichage, elle peut
			// peut être encore convertie par la vue
			[(entry.key): [kwh: entry.value[0].value, prix: command.deviceImpl.calculTarif(contrat, conso, entry.key[Calendar.YEAR])]]
		}.sort { it.key }

		chart.colonnes << new GoogleDataTableCol(label: "Date", type: "datetime", property: "key")
		chart.colonnes << new GoogleDataTableCol(label: "Consommation (€)", type: "number", pattern: "#.##", value: { deviceValue, index, currentChart ->
			deviceValue.value.prix
		})

		if (command.viewMode == ChartViewEnum.day) {
			chart.series << [type: 'steppedArea', color: SERIES_COLOR.conso]
		} else {
			chart.series << [type: 'steppedArea', color: SERIES_COLOR.conso, annotation: true]
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
			fournisseurCache = DeviceTypeProvider.findByLibelleAndDeviceType(libelle, device.deviceType)
		}

		// recherche d'un fournisseur par défaut
		if (!fournisseurCache) {
			fournisseurCache = DeviceTypeProvider.findByLibelleAndDeviceType(defaultFournisseur(), device.deviceType)
		}

		return fournisseurCache
	}
	
	
	/**
	 * Le nom du fournisseur par défaut si pas renseigné sur le device
	 * Peut-être surchargé par les impl
	 * 
	 * @return
	 */
	protected String defaultFournisseur() {
		DEFAULT_FOURNISSEUR
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
		defaultMetaConsoUnite()
	}
	
	
	/**
	 * Unité par défaut pour la meta conso
	 * 
	 * @return
	 */
	String defaultMetaConsoUnite() {
		""
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
		device.addMetavalue(META_METRIC_NAME, [value: "0", label: "Période consommation",
			trace: true, unite: defaultMetaConsoUnite()])
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
	void parseIndex(CompteurIndex compteurIndex) throws SmartHomeException {
		// met à jour la valeur principale
		device.value = (compteurIndex.index1 as Long).toString()

		addDefaultMetas()
		
		// essaie de calculer une conso sur la période si un ancien index est trouvé
		// on vérifie aussi que le nouvel index est bien > à l'ancien (ex si changement de compteur)
		// !! si pas de nouvelles consos, i lfaut bien penser à reset la meta conso à 0
		// sinon au moment de l'historisation, c'est l'ancienne valeur qui va être prise en compte
		// mais c'est géré dans "addDefaultMetas"
		DeviceValue lastIndex = lastIndex(compteurIndex.dateIndex)

		if (lastIndex) {
			Long conso = calculConsoBetweenIndex(compteurIndex.index1, lastIndex.value, compteurIndex.param1) 
			device.addMetavalue(META_METRIC_NAME, [value: conso.toString()])
		}
	}
	
	
	/**
	 * Calcul la conso entre 2 index
	 * 
	 * @param newIndex
	 * @param lastIndex
	 * @param param1
	 * @return
	 */
	protected Long calculConsoBetweenIndex(double newIndex, double lastIndex, String param1) throws SmartHomeException {
		if (newIndex > lastIndex) {
			(newIndex - lastIndex) as Long
		} else {
			0
		}
	}
	
	
	/**
	 * En cas d'ajout d'un index, on doit modifier son voisins plus récent
	 * pour recalculer sa conso périodique car elle dépendait du précédent index et
	 * maintenant elle doit tenir de l'index courant
	 * 
	 * @param compteurIndex
	 * @return Collection<DeviceValue>
	 * @throws SmartHomeException
	 */
	Collection<DeviceValue> refactoringNextIndex(CompteurIndex compteurIndex) throws SmartHomeException {
		DeviceValue nextConso = refactoringNextMetaConso(compteurIndex.dateIndex, compteurIndex.index1, null, compteurIndex.param1)
		return nextConso ? [nextConso] : []
	}

	
	/**
	 * Refactoring d'une meta sélectionnée par son nom
	 * 	
	 * @param dateIndex
	 * @param valeurIndex
	 * @param metaNameIndex
	 * @param param1
	 * @return
	 * @throws SmartHomeException
	 */
	protected DeviceValue refactoringNextMetaConso(Date dateIndex, double valeurIndex, String metaNameIndex, String param1) throws SmartHomeException {
		DeviceValue nextIndex = nextIndex(dateIndex, metaNameIndex)
		DeviceValue nextConso
		
		// si index suivant trouvé, on vérifie si une conso avait été calculée
		// cette conso doit être recherchée sur la même date que l'index
		if (nextIndex) {
			// si index postérieur, logiquement sa valeur doit être plus grande
			if (valeurIndex > nextIndex.value) {
				throw new SmartHomeException("L'index ${valeurIndex} du ${DateUtils.formatDateUser(dateIndex)} est supérieur à l'index ${nextIndex.value} du ${DateUtils.formatDateUser(nextIndex.dateValue)} !")
			}
			
			nextConso = DeviceValue.findByDate(device, nextIndex.dateValue, getConsoNameByIndexName(metaNameIndex))
			
			if (!nextConso ) {
				nextConso = new DeviceValue(device: device, dateValue: nextIndex.dateValue,
					name: getConsoNameByIndexName(metaNameIndex))
			}
			
			// mise à jour de la conso périodique
			nextConso.value = calculConsoBetweenIndex(nextIndex.value, valeurIndex, param1)
		}
		
		return nextConso
	} 


	/**
	 * Modification d'un index existant et de l'index suivant s'il existe
	 * 
	 * @param deviceValue
	 * @return values qui ont changé et doivent être modifiées
	 * @throws SmartHomeException
	 */
	Collection<DeviceValue> updateIndex(DeviceValue deviceValue) throws SmartHomeException {
		// par défaut, l'index sera à persister
		List values = [deviceValue]
		String consoName = getConsoNameByIndexName(deviceValue.name)
		
		// recherche de la meta conso associée
		DeviceValue consoValue = DeviceValue.findByDate(device, deviceValue.dateValue, consoName)
		
		// création si pas déjà fait
		if (!consoValue) {
			consoValue = new DeviceValue(device: device, dateValue: deviceValue.dateValue,
				name: consoName)
		}
		
		// on la référence pour enregistrement des modifs
		values << consoValue
		
		// recalcul de la conso avec l'index antérieur (du même nom)
		DeviceValue lastIndex = lastIndex(deviceValue.dateValue, deviceValue.name)

		if (lastIndex) {
			// blocage si l'index est inférieure à l'index précédent
			if (deviceValue.value < lastIndex.value) {
				throw new SmartHomeException("L'index ${deviceValue.value} du ${DateUtils.formatDateUser(deviceValue.dateValue)} est inférieur à l'index ${lastIndex.value} du ${DateUtils.formatDateUser(lastIndex.dateValue)} !")
			}
			
			consoValue.value = calculConsoBetweenIndex(deviceValue.value, lastIndex.value, null)
		}
		
		// mise à jour du suivant
		values << refactoringNextMetaConso(deviceValue.dateValue, deviceValue.value, deviceValue.name, null)
		
		// suppression éventuelle des valeurs nulles
		return values.findAll()
	}
	
	
	/**
	 * Donne le nom de la méta conso associé à un index
	 * 
	 * @param indexName
	 * @return
	 */
	protected String getConsoNameByIndexName(String indexName) {
		META_METRIC_NAME
	}
	
	
	/**
	 * Le dernier index enregistré sur le device
	 * Par défaut, la value principale (sans name) est associé à l'index
	 * 
	 * @param dateTo la date max pour rechercher l'index (ie la date de l'index
	 * 	en cours de création) (exclusif)
	 * @param nom de la meta à rechercher. null par défaut
	 * 
	 * @return
	 */
	DeviceValue lastIndex(Date dateTo, String metaName = null) {
		// si aucune date sur le device, alors aucune value
		if (device.dateValue) {
			DeviceValue.lastValueInPeriod(device, lastIndexDateFrom(dateTo), dateTo, metaName)
		} else {
			return null
		}
	}
	
	
	/**
	 * Recherche de l'index suivant
	 *  
	 * @param dateFrom
	 * @param metaName
	 * @return
	 */
	DeviceValue nextIndex(Date dateFrom, String metaName = null) {
		// si aucune date sur le device, alors aucune value
		if (device.dateValue) {
			DeviceValue.firstValueAfter(device, dateFrom, metaName)
		} else {
			return null
		}
	}
	
	
	/**
	 * Calcul de la date de départ pour la recherche d'un ancien index à partir
	 * de la date fin. Recherche maxi sur 1 mois.
	 * 
	 * 
	 * @param dateTo
	 * @return
	 */
	protected Date lastIndexDateFrom(Date dateTo) {
		DateUtils.incMonth(dateTo, -1 * MAX_MONTH_SEARCH_INDEX)
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
	
	
	/**
	 * Conversion des valeurs enregistrés pour le calcul des prix
	 * 
	 * @param value
	 * @return
	 */
	Double convertValueForCalculPrix(Double value) {
		value
	}
	
	
	/**
	 * Vrai si compteur connecté (et donc donnée auto)
	 * 
	 * @param grailsApplication
	 * @return
	 */
	boolean isConnected(GrailsApplication grailsApplication) {
		false	
	} 
}
