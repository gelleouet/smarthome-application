/**
 * 
 */
package smarthome.application

import org.springframework.transaction.annotation.Transactional

import smarthome.automation.ChartTypeEnum
import smarthome.automation.Device
import smarthome.automation.House
import smarthome.automation.deviceType.Compteur
import smarthome.core.AbstractService
import smarthome.core.CompteurUtils
import smarthome.core.NumberUtils
import smarthome.core.QueryUtils
import smarthome.core.SmartHomeException
import smarthome.core.chart.GoogleChart
import smarthome.core.chart.GoogleDataTableCol
import smarthome.security.User

/**
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class DefiService extends AbstractService {


	/**
	 * Recherche des défis d'un admin (les défis qu'il a créé)
	 * 
	 * @param admin
	 * @param pagination
	 * @return
	 */
	List<Defi> listByAdmin(DefiCommand command, Map pagination) {
		return Defi.createCriteria().list(pagination) {
			eq 'user', command.user

			if (command.search) {
				ilike 'libelle', QueryUtils.decorateMatchAll(command.search)
			}

			order 'referenceDebut', 'desc'
		}
	}


	/**
	 * Les défis auxquels participent à user
	 * 
	 * @param command
	 * @param pagination
	 * @return
	 */
	List<Defi> listByUser(DefiCommand command, Map pagination) {
		return Defi.createCriteria().list(pagination) {
			equipes {
				participants {
					eq 'user', command.user
				}
			}
			order 'referenceDebut', 'desc'
		}
	}


	/**
	 * Les résultats d'une équipe
	 *
	 * @param defi
	 * @param user
	 * @return
	 */
	DefiEquipe findEquipeResultat(Defi defi, User user) {
		// on passe par une HQL pour éviter le fetch auto du criteria sur
		// les jointures
		def results = DefiEquipeParticipant.executeQuery("""\
			SELECT equipe
			FROM DefiEquipeParticipant dep
			JOIN dep.defiEquipe equipe
			WHERE dep.user = :user AND equipe.defi = :defi""",
				[defi: defi, user: user])

		return results ? results[0] : null
	}


	/**
	 * Les sous-résultats d'une équipe par profil d'utilisateur
	 * 
	 * @param defiEquipe
	 * @return
	 */
	List<DefiEquipeProfil> listEquipeProfilResultat(DefiEquipe defiEquipe) {
		DefiEquipeProfil.createCriteria().list() {
			eq 'defiEquipe', defiEquipe
			join 'profil'
		}
	}


	/**
	 * Les résultats individuels d'un user
	 * 
	 * @param defi
	 * @param user
	 * @return
	 */
	DefiEquipeParticipant findUserResultat(Defi defi, User user) {
		// on passe par une HQL pour éviter le fetch auto du criteria sur
		// les jointures
		def results = DefiEquipeParticipant.executeQuery("""\
			SELECT dep
			FROM DefiEquipeParticipant dep
			JOIN dep.defiEquipe equipe
			WHERE dep.user = :user AND equipe.defi = :defi""",
				[defi: defi, user: user])

		return results ? results[0] : null
	}


	/**
	 * Le nombre d'équipes pour le défi
	 *
	 * @param defi
	 * @return
	 */
	Long countEquipe(Defi defi) {
		return DefiEquipe.countByDefi(defi)
	}


	/**
	 * Le nombre de participants d'une équipe
	 * 
	 * @param defiEquipe
	 * @return
	 */
	Long countParticipantEquipe(DefiEquipe defiEquipe) {
		return DefiEquipeParticipant.countByDefiEquipe(defiEquipe)
	}


	/**
	 * Inscription d'un user à un défi et une équipe
	 * L'équipe est créé et associée au défi et son nom n'est pas trouvée
	 * Le statut du défi est checké pour savoir si toujours ouvert
	 * 
	 * @param user
	 * @param defiId
	 * @param equipeName
	 * 
	 * @return DefiParticipant
	 * 
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DefiEquipeParticipant inscription(User user, Long defiId, String equipeName) throws SmartHomeException {
		// vérifie le statut du défi
		Defi defi = Defi.read(defiId)

		if (!defi) {
			throw new SmartHomeException("Le défi n'existe pas !")
		}

		if (!defi.actif) {
			throw new SmartHomeException("Le ${defi.libelle} n'est plus ouvert aux inscriptions !")
		}

		// recherche d'une équipe associée au défi par son nom
		// si elle n'existe pas, elle est créé à la volée pour faciliter la gestion
		// des équipes
		DefiEquipe defiEquipe = DefiEquipe.findByDefiAndLibelle(defi, equipeName)

		if (!defiEquipe) {
			defiEquipe = new DefiEquipe(defi: defi, libelle: equipeName)
			super.save(defiEquipe)
		}

		// Association du user avec l'équipe du défi
		return super.save(new DefiEquipeParticipant(user: user, defiEquipe: defiEquipe))
	}


	/**
	 * Graphe comparatif total des périodes
	 * 
	 * @param defi
	 * @param consos données contenant 2 fields : reference et action
	 * 
	 * @return GoogleChart
	 */
	GoogleChart chartTotal(Defi defi, Map consos) {
		GoogleChart chart = new GoogleChart()

		chart.chartType = ChartTypeEnum.Column.factory
		chart.values = []

		if (consos.resultat?.canDisplay()) {
			chart.values = [
				[name: "Total", reference: consos.reference, action: consos.action]
			]
		}

		chart.colonnes << new GoogleDataTableCol(label: "Total", property: "name", type: "string")
		chart.colonnes << new GoogleDataTableCol(label: "Référence", property: "reference", type: "number")
		chart.colonnes << new GoogleDataTableCol(label: "Action", property: "action", type: "number")

		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.total, annotation: true]
		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.conso, annotation: true]

		chart.vAxis << [title: 'Consommation (kWh)']

		return chart
	}


	/**
	 * Graphe comparatif total des périodes par profil
	 *
	 * @param defi
	 * @param consos
	 * @param defiCompteur
	 *
	 * @return GoogleChart
	 */
	GoogleChart chartProfil(Defi defi, Map consos, DefiCompteurEnum defiCompteur) {
		GoogleChart chart = new GoogleChart()

		chart.chartType = ChartTypeEnum.Column.factory
		chart.values = []

		// regroupe les données par profil et pour chacun récupère le
		if (consos.resultat?.canDisplay()) {
			consos?.values?.sort { it.profil.libelle }?.each {
				chart.values << [name: it.profil.libelle,
					reference: it."reference_${ defiCompteur}"(),
					action: it."action_${ defiCompteur}"()]
			}
		}

		chart.colonnes << new GoogleDataTableCol(label: "Total", property: "name", type: "string")
		chart.colonnes << new GoogleDataTableCol(label: "Référence", property: "reference", type: "number")
		chart.colonnes << new GoogleDataTableCol(label: "Action", property: "action", type: "number")

		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.total, annotation: true]
		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.conso, annotation: true]

		chart.vAxis << [title: 'Consommation (kWh)']

		return chart
	}


	/**
	 * Graphe comparatif des consommations sur les 2 périodes pour un user en fonction d'un compteur
	 * L'abscisse est calculée à partir de la période de référence et affichée sous forme
	 * de n° de jour dans la semaine pour faire coincider les 2 séries/
	 * 
	 * IMPORTANT !!! il faut bien sur que les 2 périodes commencent le même jour
	 * de la semaine (ie lundi) et aient la même durée en jour
	 *
	 * @param defi
	 * @param consos données issues de la méthode #loadUserConso
	 *
	 * @return GoogleChart
	 */
	GoogleChart chartUserDay(Defi defi, Map consos) {
		GoogleChart chart = new GoogleChart()
		chart.chartType = ChartTypeEnum.Column.factory
		chart.hAxisFormat = "EE"
		chart.slantedText = true

		// réorganisation avec une liste de N jours correspondant à la période de référence
		// ensuite, les valeurs d'action sont injectée sur ces jours
		// on convertit aussi les consos en kWh
		chart.values = []

		if (consos.resultat?.canDisplay()) {
			for (def reference : consos.referenceValues) {
				chart.values << [dateValue: reference.dateValue,
					reference: CompteurUtils.convertWhTokWh(reference.value)]
			}

			// injecte les valeurs d'action en tenant compte de "trous"
			// l'index est calculé à partir du jour de départ de la période
			if (consos.actionValues) {
				Date debutAction = consos.actionValues[0].dateValue
				int index

				for (def action : consos.actionValues) {
					index = action.dateValue - debutAction

					if (index >= 0 && index < chart.values.size()) {
						chart.values[index].action = CompteurUtils.convertWhTokWh(action.value)
					}
				}
			}
		}

		chart.hAxisTicks = chart.values.collect {
			"new Date(${it.dateValue[Calendar.YEAR]}, ${it.dateValue[Calendar.MONTH]}, ${it.dateValue[Calendar.DAY_OF_MONTH]})"
		}.join(",")

		chart.colonnes << new GoogleDataTableCol(label: "Date", property: "dateValue", type: "datetime")
		chart.colonnes << new GoogleDataTableCol(label: "Référence", property: "reference", type: "number")
		chart.colonnes << new GoogleDataTableCol(label: "Action", property: "action", type: "number")

		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.total, annotation: true]
		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.conso, annotation: true]

		chart.vAxis << [title: 'Consommation (kWh)']

		return chart
	}


	/**
	 * Graphe comparatif des consommations sur les 2 périodes pour un user en fonction d'un compteur
	 * L'abscisse est en affichée en semaine. C'est utilisé surtout pour les
	 * compteurs avec saisie manuelle pour lesquels la saisie est enregistrée sur
	 * un jour quelconque de la semaine et qui fausse le résultat par jour
	 *
	 * IMPORTANT !!! il faut bien sur que les 2 périodes aient la même durée
	 *
	 * @param defi
	 * @param consos données issues de la méthode #loadUserConso
	 *
	 * @return GoogleChart
	 */
	GoogleChart chartUserWeek(Defi defi, Map consos) {
		GoogleChart chart = new GoogleChart()
		chart.chartType = ChartTypeEnum.Column.factory

		// réorganisation les valeurs de référence par semaine
		// on convertit aussi les consos en kWh
		chart.values = []

		if (consos.resultat?.canDisplay()) {
			consos.referenceValues?.groupBy {
				it.dateValue[Calendar.WEEK_OF_YEAR]
			}?.eachWithIndex { key, values, statut ->
				chart.values << [week: "S${statut + 1}", reference: CompteurUtils.convertWhTokWh(
					values.sum { it.value })]
			}

			// injecte les valeurs d'action en tenant compte de "trous"
			// l'index est calculé à partir du jour de départ de la période
			// les valeurs sont d'abord regroupées par semaine avant d'être injectées
			if (consos.actionValues) {
				int debutSemaine = consos.actionValues[0].dateValue[Calendar.WEEK_OF_YEAR]
				int index

				consos.actionValues.groupBy {
					it.dateValue[Calendar.WEEK_OF_YEAR]
				}.eachWithIndex { key, values, statut ->
					index = key - debutSemaine

					if (index >= 0 && index < chart.values.size()) {
						chart.values[index].action = CompteurUtils.convertWhTokWh(
								values.sum { it.value })
					}
				}
			}
		}

		int statut = 1
		chart.hAxisTicks = chart.values.collect { "'S${ statut++}'" }.join(",")

		chart.colonnes << new GoogleDataTableCol(label: "Semaine", property: "week", type: "string")
		chart.colonnes << new GoogleDataTableCol(label: "Référence", property: "reference", type: "number")
		chart.colonnes << new GoogleDataTableCol(label: "Action", property: "action", type: "number")

		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.total, annotation: true]
		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.conso, annotation: true]

		chart.vAxis << [title: 'Consommation (kWh)']

		return chart
	}


	/**
	 * Charge les consos d'un user sur les périodes
	 * La méthode calcul les consos par jour, calcule les totaux sur les 2 périodes
	 * et retourne le tout sous forme de Map.
	 * 
	 * @param defi
	 * @param house
	 * @param compteurType
	 * 
	 * @return Map avec les fields : reference, action, totalReference, totalAction
	 */
	Map loadUserConso(Defi defi, House house, DefiCompteurEnum compteurType) throws SmartHomeException {
		Map result = [:]

		Device device = house[compteurType.property]

		if (!device) {
			throw new SmartHomeException("Le compteur ${compteurType} n'est pas renseigné !")
		}

		Compteur compteur = device.newDeviceImpl()

		// garde une trace du type de compteur
		result.type = compteurType
		result.compteur = device

		// charge les consos sur les 2 périodes
		result.referenceValues = compteur.consommationTotalByDay(defi.referenceDebut, defi.referenceFin)
		result.actionValues = compteur.consommationTotalByDay(defi.actionDebut, defi.actionFin)

		// calcul temporaire des totaux et diff
		result.totalReference = CompteurUtils.convertWhTokWh(result.referenceValues.sum { it.value })
		result.totalAction = CompteurUtils.convertWhTokWh(result.actionValues.sum { it.value })
		result.totalDiff = result.totalAction - result.totalReference

		return result
	}


	/**
	 * Regroupe des consos chargées depuis la méthode #loadUserConso et calcule
	 * le consos globales
	 * 
	 * @param consos 1 ou plusieurs datas
	 * 
	 * @return
	 */
	Map groupConsos(Defi defi, Map... consos) {
		Map result = [referenceValues: [], actionValues: []]

		// garde une trace du type de compteur
		result.type = DefiCompteurEnum.global

		// calcul l'affichage approprié pour les consos globales détaillées
		// si gros écart de fréquence entre les périodes de référence entre les
		// compteurs, on passe sur une vue semaine sinon on peut rester sur une
		// vue journalière
		result.bestView = "day"

		int limitDuree = (defi.referenceFin - defi.referenceDebut) / 2

		// la liste peut contenir des valeurs nulles
		// ajoute toutes les consos des compteurs dans 2 buffers reference et action
		// A ce stade, il y a des doublons sur les dates
		consos?.each { conso ->
			if (conso) {
				result.referenceValues.addAll(conso.referenceValues)
				result.actionValues.addAll(conso.actionValues)

				if (conso.referenceValues.size() <= limitDuree) {
					result.bestView = "week"
				}
			}
		}

		// 2e passe, regroupement des valeurs par date et sum des valeurs trouvées
		// pour les 2 buffers
		result.referenceValues = result.referenceValues.groupBy { it.dateValue }.collect { key, values ->
			[dateValue: key, value: values.sum{ it.value }]
		}
		result.actionValues = result.actionValues.groupBy { it.dateValue }.collect { key, values ->
			[dateValue: key, value: values.sum{ it.value }]
		}

		// calcul temporaire des totaux et diff
		result.totalReference = CompteurUtils.convertWhTokWh(result.referenceValues.sum { it.value })
		result.totalAction = CompteurUtils.convertWhTokWh(result.actionValues.sum { it.value })
		result.totalDiff = result.totalAction - result.totalReference

		return result
	}
}
