/**
 * 
 */
package smarthome.application

import java.text.DecimalFormat

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional

import groovyx.gpars.GParsPool
import smarthome.automation.ChartTypeEnum
import smarthome.automation.Device
import smarthome.automation.House
import smarthome.automation.HouseService
import smarthome.automation.deviceType.Compteur
import smarthome.core.AbstractService
import smarthome.core.Chronometre
import smarthome.core.CompteurUtils
import smarthome.core.Config
import smarthome.core.ConfigService
import smarthome.core.NumberUtils
import smarthome.core.QueryUtils
import smarthome.core.SmartHomeException
import smarthome.core.chart.GoogleChart
import smarthome.core.chart.GoogleDataTableCol
import smarthome.core.query.HQL
import smarthome.rule.DefiCalculRuleService
import smarthome.security.Profil
import smarthome.security.Role
import smarthome.security.User

/**
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class DefiService extends AbstractService {

	HouseService houseService
	DefiCalculRuleService defiCalculRuleService
	ConfigService configService


	/**
	 * Edition d'un défi
	 * 
	 * @param defi
	 * @return
	 */
	//@PreAuthorize("hasPermission(#defi, 'OWNER')")
	Defi edit(Defi defi) {
		return defi
	}


	/**
	 * Liste les défis du catalogue (uniquement les défis publiques et ouverts)
	 * 
	 * @param command
	 * @param pagination
	 * @return
	 */
	List<Defi> listCatalogue(DefiCommand command, Map pagination) {
		return Defi.createCriteria().list(pagination) {
			eq 'publique', true
			eq 'actif', true
			
			if (command.search) {
				ilike 'libelle', QueryUtils.decorateMatchAll(command.search)
			}
			if (command.organisation) {
				ilike 'organisation', QueryUtils.decorateMatchAll(command.organisation)
			}

			join 'user'
			
			order 'referenceDebut', 'desc'
		}
	}
	
	
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
	 * Les défis de l'utilisateur connecté
	 * 
	 * @param command
	 * @param pagination
	 * @return
	 */
	List<Defi> listByAuthenticatedUser(Map pagination) {
		User user = User.read(springSecurityService.principal.id)
		return listByUser(new DefiCommand(user: user), pagination)
	}


	/**
	 * Liste les profils disctincts des participants
	 * 
	 * @param defi
	 * @return
	 */
	List<Profil> listDistinctProfil(Defi defi) {
		return DefiEquipeParticipant.executeQuery("""\
			SELECT distinct profil
			FROM DefiEquipeParticipant dep
			JOIN dep.defiEquipe equipe
			JOIN dep.user user
			JOIN user.profil profil
			WHERE equipe.defi = :defi""", [defi: defi])
	}


	/**
	 * Le classement général des équipes d'un défi
	 * 
	 * @param defi
	 * @param pagination
	 * @return
	 */
	List<Map> classementEquipe(Defi defi, Map pagination) {
		return DefiEquipe.executeQuery("""\
			SELECT new map(defiEquipe.libelle as libelle, defiEquipe.economie_global as economie,
			defiEquipe as resultat, defiEquipe.classement_global as classement)
			FROM DefiEquipe defiEquipe
			WHERE defiEquipe.defi = :defi
			AND defiEquipe.classement_global is not null
			ORDER BY defiEquipe.classement_global""", [defi: defi], pagination)
	}


	/**
	 * Ajoute une équipe dans le classement si pas présente.
	 * Le classement retourné doit être à nouveau trié
	 * L'équipe doit être classée pour pouvoir être insérée
	 * 
	 * @param classement
	 * @param defiEquipe
	 * @return
	 */
	List<Map> addEquipeClassement(List<Map> classement, DefiEquipe defiEquipe) {
		if (defiEquipe?.classement_global) {
			def existEquipe = classement.find { it.libelle == defiEquipe.libelle }

			if (!existEquipe) {
				classement << [libelle: defiEquipe.libelle, economie: defiEquipe.economie_global,
					resultat: defiEquipe, classement: defiEquipe.classement_global]
			}
		}

		return classement
	}


	/**
	 * Ajoute une équipe/profil dans le classement si pas présente.
	 * Le classement retourné doit être à nouveau trié
	 * L'équipe doit être classée pour pouvoir être insérée
	 *
	 * @param classement
	 * @param defiEquipe
	 * @param profil
	 * @return
	 */
	List<Map> addEquipeProfilClassement(List<Map> classement, DefiEquipe defiEquipe, Profil profil) {
		def equipeProfil = defiEquipe?.profils?.find { it.profil == profil }

		if (equipeProfil?.classement_global) {
			def existEquipe = classement.find { it.libelle == defiEquipe.libelle }

			if (!existEquipe) {
				classement << [libelle: defiEquipe.libelle, economie: equipeProfil.economie_global,
					resultat: equipeProfil, classement: equipeProfil.classement_global]
			}
		}

		return classement
	}


	/**
	 * Le classement général des équipes par profil  d'un défi
	 *
	 * @param defi
	 * @param profil
	 * @param pagination
	 * @return
	 */
	List<Map> classementEquipeProfil(Defi defi, Profil profil, Map pagination) {
		return DefiEquipeProfil.executeQuery("""\
			SELECT new map(defiEquipe.libelle as libelle, defiEquipeProfil.economie_global as economie,
			defiEquipeProfil as resultat, defiEquipeProfil.classement_global as classement)
			FROM DefiEquipeProfil defiEquipeProfil
			JOIN defiEquipeProfil.defiEquipe defiEquipe
			WHERE defiEquipe.defi = :defi AND defiEquipeProfil.profil = :profil
			AND defiEquipeProfil.classement_global is not null
			ORDER BY defiEquipeProfil.classement_global""",
				[defi: defi, profil: profil], pagination)
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
	 * Liste tous les participants d'un défi
	 * 
	 * @param command
	 * @param pagination
	 * @return
	 */
	List<DefiEquipeParticipant> listParticipantResultat(DefiCommand command, Map pagination) {
		def results = []
		def hql = new HQL("participant, house", """
			FROM DefiEquipeParticipant participant, House house
			JOIN FETCH participant.defiEquipe defiEquipe
			JOIN FETCH participant.user user
			JOIN FETCH user.profil profil
			LEFT JOIN FETCH house.chauffage
		""")

		hql.addCriterion("defiEquipe.defi = :defi", [defi: command.defi])
		hql.addCriterion("house is null OR (house.user = user AND house.defaut = true)")

		if (command.defiEquipe) {
			hql.addCriterion("defiEquipe.id = :defiEquipeId", [defiEquipeId: command.defiEquipe.id])
		}

		if (command.profilPublic) {
			hql.addCriterion("user.profilPublic = :profilPublic", [profilPublic: true])
		}

		hql.addOrder("user.prenom")
		hql.addOrder("user.nom")

		DefiEquipeParticipant.withSession { session ->
			results = hql.list(session, pagination)
		}

		results.collect {
			it[0].house = it[1]
			it[0]
		}
		/*DefiEquipeParticipant.createCriteria().list(pagination) {
		 defiEquipe {
		 eq 'defi', command.defi
		 if (command.defiEquipe) {
		 eq 'id', command.defiEquipe.id
		 }
		 }
		 user {
		 profil {
		 }
		 order "prenom"
		 order "nom"
		 }
		 }*/
	}


	/**
	 * Les sous-résultats d'une équipe par profil d'utilisateur
	 * 
	 * @param defiEquipe
	 * @return
	 */
	List<DefiEquipeProfil> listEquipeProfilResultat(DefiEquipe defiEquipe) {
		return DefiEquipeProfil.createCriteria().list() {
			eq 'defiEquipe', defiEquipe
			join 'profil'
		}
	}


	/**
	 * Les sous-résultats de toutes les équipes d'un défi
	 *
	 * @param defi
	 * @return
	 */
	List<DefiEquipeProfil> listEquipeProfilResultat(Defi defi) {
		// passe par le hql pour ne pas activer les fetch auto du criteria
		return DefiEquipeProfil.executeQuery("""\
			SELECT defiEquipeProfil
			FROM DefiEquipeProfil defiEquipeProfil
			JOIN FETCH defiEquipeProfil.defiEquipe defiEquipe
			JOIN FETCH defiEquipeProfil.profil profil
			WHERE defiEquipe.defi = :defi""", [defi: defi])
	}


	/**
	 * Les sous-résultats du défi par profil d'utilisateur
	 *
	 * @param defi
	 * @return
	 */
	List<DefiProfil> listDefiProfilResultat(Defi defi) {
		return DefiProfil.createCriteria().list() {
			eq 'defi', defi
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
		
		// déjà inscript
		DefiEquipeParticipant existParticipant = this.findUserResultat(defi, user)
		
		if (existParticipant) {
			throw new SmartHomeException("Vous êtes déjà inscrit à ce défi !")
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
	 * Désinscription d'un défi
	 * 
	 * @param user
	 * @param defiId
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void desinscription(User user, Long defiId) throws SmartHomeException {
		// vérifie le statut du défi
		Defi defi = Defi.read(defiId)
		
		if (!defi) {
			throw new SmartHomeException("Le défi n'existe pas !")
		}
		
		DefiEquipeParticipant existParticipant = this.findUserResultat(defi, user)
		
		if (!existParticipant) {
			throw new SmartHomeException("Vous n'êtes pas inscrit à ce défi !")
		}
		
		desinscription(existParticipant)
	}
	
	
	/**
	 * Désinscription d'un défi
	 *
	 * @param participant
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void desinscription(DefiEquipeParticipant participant) throws SmartHomeException {
		DefiEquipe equipe = participant.defiEquipe
		
		// supprime le user de la liste des participants
		equipe.participants.removeAll { it.id == participant.id }
		boolean hasOtherParticipant = equipe.participants.size() 
		
		super.delete(participant)
		
		// suppression de l'équipe si pas d'autres participants
		if (!hasOtherParticipant) {
			equipe.profils*.delete()
			super.delete(equipe)
		}
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
		chart.colonnes << new GoogleDataTableCol(label: "Référence (kWh)", property: "reference", type: "number")
		chart.colonnes << new GoogleDataTableCol(label: "Action (kWh)", property: "action", type: "number")

		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.total, annotation: true]
		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.conso, annotation: true]

		chart.vAxis << [title: 'Consommation (kWh)', minValue: 0]

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
		chart.colonnes << new GoogleDataTableCol(label: "Référence (kWh)", property: "reference", type: "number")
		chart.colonnes << new GoogleDataTableCol(label: "Action (kWh)", property: "action", type: "number")

		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.total, annotation: true]
		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.conso, annotation: true]

		chart.vAxis << [title: 'Consommation (kWh)', minValue: 0]

		return chart
	}


	/**
	 * Graphe classement des équipes
	 *
	 * @param defi
	 * @param classement
	 * @param currentEquipe
	 *
	 * @return GoogleChart
	 */
	GoogleChart chartClassement(Defi defi, List classement, DefiEquipe currentEquipe) {
		GoogleChart chart = new GoogleChart()

		chart.chartType = ChartTypeEnum.Bar.factory
		chart.hAxisTitle = 'Economie (%)'
		chart.values = []

		// regroupe les données par profil et pour chacun récupère le
		if (defi?.canDisplay()) {
			classement?.each {
				chart.values << [equipe: "${it.classement} - ${it.libelle}", economie: it.economie]
			}
		}

		DecimalFormat format = new DecimalFormat("0.#'%'")
		String libelleEquipe = "${currentEquipe?.classement_global} - ${ currentEquipe?.libelle}"

		chart.colonnes << new GoogleDataTableCol(label: "Equipe", property: "equipe", type: "string")
		chart.colonnes << new GoogleDataTableCol(label: "Economie", property: "economie", type: "number")
		chart.colonnes << new GoogleDataTableCol(role: "style", type: "string", value: { deviceValue, index, currentChart ->
			"color:${ deviceValue.equipe == libelleEquipe ? Compteur.SERIES_COLOR.conso : (index == 0 ? '#ff9900' : Compteur.SERIES_COLOR.total) }"
		})
		chart.colonnes << new GoogleDataTableCol(role: "annotation", type: "string", value: { deviceValue, index, currentChart ->
			deviceValue.economie == null ? "" : format.format(deviceValue.economie)
		})

		chart.series << [type: 'bars', minValue: 0]
		chart.series << [type: 'bars']
		chart.series << [type: 'bars']

		return chart
	}


	/**
	 * Représentation graphique du défi avec les équipes et les participants
	 * 
	 * @param defi
	 * @return
	 */
	GoogleChart chartOrganization(Defi defi) {
		GoogleChart chart = new GoogleChart()
		chart.chartType = ChartTypeEnum.Org.factory

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

		chart.colonnes << new GoogleDataTableCol(label: "Date", property: "dateValue", type: "date")
		chart.colonnes << new GoogleDataTableCol(label: "Référence (kWh)", property: "reference", type: "number")
		chart.colonnes << new GoogleDataTableCol(label: "Action (kWh)", property: "action", type: "number")

		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.total, annotation: true]
		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.conso, annotation: true]

		chart.vAxis << [title: 'Consommation (kWh)', minValue: 0]

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
		chart.colonnes << new GoogleDataTableCol(label: "Référence (kWh)", property: "reference", type: "number")
		chart.colonnes << new GoogleDataTableCol(label: "Action (kWh)", property: "action", type: "number")

		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.total, annotation: true]
		chart.series << [type: 'bars', color: Compteur.SERIES_COLOR.conso, annotation: true]

		chart.vAxis << [title: 'Consommation (kWh)', minValue: 0]

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


	/**
	 * Efface les résultats d'un participant
	 * 
	 * @param participant
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DefiEquipeParticipant effacerParticipant(DefiEquipeParticipant participant) throws SmartHomeException {
		participant.cleanResultat()
		return super.save(participant)
	}


	/**
	 * Efface les résultats entiers d'une équipe
	 *
	 * @param equipe
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DefiEquipe effacerEquipe(DefiEquipe equipe) throws SmartHomeException {
		equipe.cleanResultat()

		equipe.participants?.each {
			effacerParticipant(it)
		}

		equipe.profils?.each {
			it.cleanResultat()
		}

		return super.save(equipe)
	}


	/**
	 * Extrait les objets équipe à partir des résultats des participants
	 * et réinjecte le participant dans la liste de l'équipe pour reformer l'arbo
	 * 
	 * @param participants
	 * @return
	 */
	private List<DefiEquipe> extractDefiEquipe(List<DefiEquipeParticipant> participants) {
		List<DefiEquipe> defiEquipeList = []

		participants.each {
			if (!defiEquipeList.contains(it.defiEquipe)) {
				it.defiEquipe.participants = []
				defiEquipeList << it.defiEquipe
			}

			// réinjecte le participant dans son équipe
			it.defiEquipe.participants << it
		}

		return defiEquipeList
	}


	/**
	 * Calcul des résultats à partir des consommations.
	 * La méthode calculConsommations doit être appelée avant pour enregistrer
	 * les consos et surtourt pour charger tous les éléments nécessaires et les
	 * organiser dans les associations
	 * Le calcul, comme il peut évoluer, va être délégué à une règle métier
	 * 
	 * @param defi
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Defi calculerResultats(Defi defi) throws SmartHomeException {
		// pour faciliter le calcul, remet "à plat" la liste des participants
		// pour calcul éventuellement refaire des groupements
		List<DefiEquipeParticipant> participants = []

		defi.equipes.each { DefiEquipe equipe ->
			equipe.participants.each { participant ->
				participants << participant
			}
		}

		defiCalculRuleService.execute(defi, true, [participants: participants])

		return super.save(defi)
	}


	/**
	 * Le groupe d'un participant
	 * 
	 * @param defiEquipeParticipant
	 * @return
	 */
	String groupKeyParticipant(DefiEquipeParticipant defiEquipeParticipant) {
		return defiCalculRuleService.executeMethod(defiEquipeParticipant, "groupKeyParticipant", true, null)
	}


	/**
	 * Calcul des consommations du défi à chacun des niveaux
	 * Supprime également tous les résultats car ceux-ci dépendent des consos
	 * 
	 * @param defi
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Defi calculerConsommations(Defi defi) throws SmartHomeException {
		Chronometre chrono = new Chronometre()

		// precision des consos en config
		int precision = 0
		String configPrecision = configService.value(Config.GRAND_DEFI_CONSOMMATION_PRECISION)

		if (configPrecision) {
			precision = configPrecision.toInteger()
		}

		// charge tous les profils défi et reset sur la liste du défi
		// pour au final détecter les orphelins
		List<DefiProfil> defiProfils = listDefiProfilResultat(defi)
		defi.profils = []

		// charge tous les participants "à plat" et les réorganise par équipe
		// pour une structure en arbre
		List<DefiEquipeParticipant> participants = listParticipantResultat(new DefiCommand(defi: defi), [:])
		List<DefiEquipe> defiEquipes = extractDefiEquipe(participants)

		// charge les résultats au niveau des profils (défi + équipe)
		// ces listes seront complétées si des éléments manquent au moment des
		// enregistrements de résultat
		List<DefiEquipeProfil> defiEquipeProfilList = listEquipeProfilResultat(defi)

		// parallélise le traitement de la liste (ie 4 cores)
		// gpars rajoute aux méthodes d'itération des collection le suffixe Parallel
		GParsPool.withPool(4) {

			// 1ère passe, calcul simple des consos par participant
			// on passe sur la liste entière sans tenir compte des équipes car cette étape
			// nécessite de charger les consos en base. C'est l'étape la plus gourmande
			// en temps et ressource
			// le traitement de la liste est partagé entre 4 threads
			participants.eachParallel { DefiEquipeParticipant participant ->
				participant.cleanResultat()

				// le traitement est threadé avec gpars, donc hors thread courant
				// sur lequel est associé la session par défaut. il faut donc la
				// gérer manuellement. Il y en aura autant que de pool alloué
				DefiEquipeParticipant.withNewSession {
					// on charge la maison principale avec le chauffage car il sera
					// utilisé pour le calcul des notes. Cela évitera d'autres
					// requêtes en activant l'association
					House house = houseService.findDefaultByUserFetch(participant.user, ['chauffage'])
					Map consos

					if (house) {
						participant.house = house

						// calcul conso elec
						if (house[DefiCompteurEnum.electricite.property]) {
							consos = loadUserConso(defi, house, DefiCompteurEnum.electricite)
							participant.reference_electricite = NumberUtils.round(consos.totalReference, precision)
							participant.action_electricite = NumberUtils.round(consos.totalAction, precision)
						}

						// calcul conso gaz
						if (house[DefiCompteurEnum.gaz.property]) {
							consos = loadUserConso(defi, house, DefiCompteurEnum.gaz)
							participant.reference_gaz = NumberUtils.round(consos.totalReference, precision)
							participant.action_gaz = NumberUtils.round(consos.totalAction, precision)
						}
					}
				}
			}

			// on a toutes les données au niveau le plus bas. on peut aggréger les
			// données aux niveaux supérieurs, ie aux équipes à cette étape
			// le traitement de la liste est partagé entre 4 threads
			defiEquipes.eachParallel { DefiEquipe defiEquipe ->
				// reset des profils pour détecter les orphelins
				defiEquipe.profils = []
				defiEquipe.cleanResultat()

				// calcul total au niveau équipe
				defiEquipe.reference_electricite = NumberUtils.round(defiEquipe.participants.sum { it.reference_electricite ?: 0 }, precision)
				defiEquipe.reference_gaz = NumberUtils.round(defiEquipe.participants.sum { it.reference_gaz ?: 0 }, precision)
				defiEquipe.action_electricite = NumberUtils.round(defiEquipe.participants.sum { it.action_electricite ?: 0 }, precision)
				defiEquipe.action_gaz = NumberUtils.round(defiEquipe.participants.sum { it.action_gaz ?: 0 }, precision)

				// calcul au niveau équipe-profil. ajout des éléments manquans
				// en fonction des groupes calculés au niveau des participants
				defiEquipe.participants.groupBy { it.user.profil }.each { entry ->
					// recherche du profil équipe existant
					DefiEquipeProfil defiEquipeProfil = defiEquipeProfilList.find {
						it.defiEquipe == defiEquipe && it.profil == entry.key
					}

					// création à la volée d'un nouvel élément et ajout dans la liste de l'équipe
					if (!defiEquipeProfil) {
						defiEquipeProfil = new DefiEquipeProfil(profil: entry.key, defiEquipe: defiEquipe)
					}

					// ajout systématique dans la liste de l'équipe pour la reconstruire
					defiEquipe.profils << defiEquipeProfil

					defiEquipeProfil.cleanResultat()
					defiEquipeProfil.reference_electricite = NumberUtils.round(entry.value.sum { it.reference_electricite ?: 0 }, precision)
					defiEquipeProfil.reference_gaz = NumberUtils.round(entry.value.sum { it.reference_gaz ?: 0 }, precision)
					defiEquipeProfil.action_electricite = NumberUtils.round(entry.value.sum { it.action_electricite ?: 0 }, precision)
					defiEquipeProfil.action_gaz = NumberUtils.round(entry.value.sum { it.action_gaz ?: 0 }, precision)
				}
			}

			// calcul au niveau global profil
			participants.groupByParallel { it.user.profil }.each { entry ->
				// attention !! le groupBy est threadé mais pas le each.
				// donc on peut sans souci (car un seul thread) modifier l'instance
				// défi depuis cette closure
				DefiProfil defiProfil = defiProfils.find { it.profil == entry.key }

				// création à la volée d'un nouvel élément et ajout dans la liste du défi
				if (!defiProfil) {
					defiProfil = new DefiProfil(defi: defi, profil: entry.key)
				}

				// ajout systématique dans la liste du défi pour la reconstruire
				defi.profils << defiProfil

				defiProfil.cleanResultat()
				defiProfil.reference_electricite = NumberUtils.round(entry.value.sum { it.reference_electricite ?: 0 }, precision)
				defiProfil.reference_gaz = NumberUtils.round(entry.value.sum { it.reference_gaz ?: 0 }, precision)
				defiProfil.action_electricite = NumberUtils.round(entry.value.sum { it.action_electricite ?: 0 }, precision)
				defiProfil.action_gaz = NumberUtils.round(entry.value.sum { it.action_gaz ?: 0 }, precision)
			}
		}

		// dernière passe pour le calcul total du défi avec les sous-résultats par équipe
		defi.cleanResultat()
		defi.reference_electricite = NumberUtils.round(defiEquipes.sum { it.reference_electricite ?: 0 }, precision)
		defi.reference_gaz = NumberUtils.round(defiEquipes.sum { it.reference_gaz ?: 0 }, precision)
		defi.action_electricite = NumberUtils.round(defiEquipes.sum { it.action_electricite ?: 0 }, precision)
		defi.action_gaz = NumberUtils.round(defiEquipes.sum { it.action_gaz ?: 0 }, precision)

		// gestion des orphelins : équipe sans participant, ancien profil équipe
		// ou défi d'un ancien calcul et après modification des participants du
		// défi
		// les orphelins se trouvent dans les listes chargés en début de méthode
		(defiProfils - defi.profils).each { it.delete() }
		// pour les équipes on doit toutes les passer en revue pour gérer les profils
		defiEquipes.each { defiEquipe ->
			defiEquipeProfilList.removeAll(defiEquipe.profils)
		}
		defiEquipeProfilList.each { it.delete() }
		// équipes orphelines + réassociation de la bonne liste sur le défi
		(defi.equipes - defiEquipes).each { it.delete() }
		defi.equipes = defiEquipes

		log.info "Calcul défi : ${chrono.stop()}ms"

		// comme tout est réorganisé en arbre (chaque objet parent contient les enfants
		// défi -> equipe -> participant, et bien on peut lancer l'enregistrement
		// depuis le défi et le cascade fait le reste
		return super.save(defi)
	}


	/**
	 * Enregistrement défi équipe sur défi
	 * 
	 * @param DefiEquipe
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DefiEquipe saveDefiEquipe(DefiEquipe defiEquipe) throws SmartHomeException {
		return super.save(defiEquipe)
	}


	/**
	 * Enregistrement participant sur défi équipe
	 * Création auto des équipes
	 * 
	 * @param command
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DefiParticipantCommand saveDefiParticipant(DefiParticipantCommand command) throws SmartHomeException {
		if (!command.defiEquipeName) {
			throw new SmartHomeException("Veuillez saisir un nom d'équipe !", command)
		}

		// recherche de l'équipe en fonction du nom et création auto
		DefiEquipe defiEquipe = DefiEquipe.findByDefiAndLibelle(command.defi, command.defiEquipeName)

		if (!defiEquipe) {
			defiEquipe = new DefiEquipe(defi: command.defi, libelle: command.defiEquipeName)
			super.save(defiEquipe)
		}

		// cas modif d'un participant : transfert dans une autre équipe
		if (command.defiParticipant) {
			command.defiParticipant.defiEquipe = defiEquipe
		} else if (command.participants) {
			// les participants proposés ne sont pas enregistrés dans ce défi
			// donc il faut créer un nouvel objet sans vérifier si existe déjà
			command.participants.each { userId ->
				DefiEquipeParticipant participant = new DefiEquipeParticipant(user: User.read(userId),
				defiEquipe: defiEquipe)
				super.save(participant)
			}
		} else {
			throw new SmartHomeException("Veuillez sélectionner au moins un participant !", command)
		}

		return command
	}


	/**
	 * Liste tous les users avec role GRAND_DEFI qui ne sont pas encore associés
	 * à ce défi
	 * 
	 * @param defi
	 * @return
	 */
	List<User> listAvailableParticipants(Defi defi) {
		return User.executeQuery("""SELECT user
		FROM User user
		JOIN FETCH user.profil profil
		WHERE user.id not in (
			SELECT user1.id
			FROM DefiEquipeParticipant participant
			JOIN participant.user user1
			JOIN participant.defiEquipe defiEquipe
			WHERE defiEquipe.defi = :defi
		)
		ORDER BY user.prenom, user.nom""", [defi: defi])
	}
}
