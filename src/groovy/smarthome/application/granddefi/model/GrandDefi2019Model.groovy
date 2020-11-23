package smarthome.application.granddefi.model

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.TransactionDefinition

import groovyx.gpars.GParsPool
import smarthome.application.Defi
import smarthome.application.DefiCommand
import smarthome.application.DefiCompteurEnum
import smarthome.application.DefiEquipe
import smarthome.application.DefiEquipeParticipant
import smarthome.application.DefiEquipeProfil
import smarthome.application.DefiProfil
import smarthome.application.DefiService
import smarthome.application.granddefi.converter.ElecDefiConsoConverter
import smarthome.automation.House
import smarthome.automation.HouseService
import smarthome.core.ClassUtils
import smarthome.core.Config
import smarthome.core.ConfigService
import smarthome.core.NumberUtils
import smarthome.core.SmartHomeException
import smarthome.security.Profil

/**
 * !! IMPORTANT : ne pas hériter d'un autre model car sion ils ne peuvent pas évoluer
 * indépendamment
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class GrandDefi2019Model extends AbstractDefiModel {

	protected static final int MAX_CLASSEMENT = 3
	
	@Autowired
	DefiService defiService
	
	@Autowired
	ConfigService configService
	
	@Autowired
	HouseService houseService
	
	
	@Override
	Map modelResultatsDefi(DefiCommand command, Map model) throws SmartHomeException {
		model.global.classement = []
		
		// récupère le nombre d'entrées dans le classement (par défaut MAX_CLASSEMENT)
		// construction des équipes "à la volée" et association avec une équipe
		// et le grand défi en cours
		String configMaxClassement = configService.value(Config.GRAND_DEFI_MAX_CLASSEMENT)
		int maxClassement = configMaxClassement ? configMaxClassement.toInteger() : MAX_CLASSEMENT

		if (model.currentDefi) {
			model.equipe.profils = defiService.listEquipeProfilResultat(model.equipe)

			// charge les résultats globals du défi
			model.global.consos.values = defiService.listDefiProfilResultat(model.currentDefi)
			model.currentDefi.injectResultat(model.global.consos, DefiCompteurEnum.global)

			model.profils = defiService.listDistinctProfil(model.currentDefi)

			// charge les classements des équipes en global et pour chaque profil
			model.global.classement = defiService.classementEquipe(model.currentDefi,
					[max: maxClassement])
			defiService.addEquipeClassement(model.global.classement, model.equipe)

			for (Profil profil : model.profils) {
				model["profil${ profil.id }"] = [:]
				model["profil${ profil.id }"].classement = defiService.classementEquipeProfil(
						model.currentDefi, profil, [max: maxClassement])
				defiService.addEquipeProfilClassement(model["profil${ profil.id }"].classement,
						model.equipe, profil)
			}
		}

		// création des charts dans tous les cas pour ne pas avoir de page vide
		// pour le global on force le type compteur pour personnaliser l'affichage (unité + conversion)
		model.global.chartTotal = defiService.chartTotal(model.currentDefi, model.global.consos,
			DefiCompteurEnum.electricite.converter)
		model.global.chartConso = defiService.chartProfil(model.currentDefi, model.global.consos,
			DefiCompteurEnum.electricite.converter)

		model.global.chartClassement = defiService.chartClassement(model.currentDefi,
				model.global.classement, model.equipe)

		for (Profil profil : model.profils) {
			model["profil${ profil.id }"].chartClassement = defiService.chartClassement(model.currentDefi,
					model["profil${ profil.id }"].classement, model.equipe)
		}
		
		return model
	}


	@Override
	Map modelResultatsEquipe(DefiCommand command, Map model) throws SmartHomeException {
		// charge les données uniquement si défi activé
		if (model.currentDefi) {
			model.global.consos.values = defiService.listEquipeProfilResultat(model.equipe)
			model.equipe.injectResultat(model.global.consos, DefiCompteurEnum.global)

			model.electricite.consos.values = model.global.consos.values
			model.equipe.injectResultat(model.electricite.consos, DefiCompteurEnum.electricite)

			model.gaz.consos.values = model.global.consos.values
			model.equipe.injectResultat(model.gaz.consos, DefiCompteurEnum.gaz)
		}

		// lance dans tous les cas, les charts pour avoir des vues un minimum
		// complétées (sinon page vide et c'est moins fun)
		// pour le global on force le type compteur pour personnaliser l'affichage (unité + conversion)
		model.global.chartTotal = defiService.chartTotal(model.currentDefi, model.global.consos,
			DefiCompteurEnum.electricite.converter)
		model.global.chartConso = defiService.chartProfil(model.currentDefi, model.global.consos,
			DefiCompteurEnum.electricite.converter)

		model.electricite.chartTotal = defiService.chartTotal(model.currentDefi, model.electricite.consos)
		model.electricite.chartConso = defiService.chartProfil(model.currentDefi, model.electricite.consos)

		model.gaz.chartTotal = defiService.chartTotal(model.currentDefi, model.gaz.consos)
		model.gaz.chartConso = defiService.chartProfil(model.currentDefi, model.gaz.consos)
		
		return model
	}


	@Override
	Map modelMesResultats(DefiCommand command, Map model) throws SmartHomeException {
		// charge les données elec et construit les graphes
		// on passe dans des transactions séparées car le service appelé peut
		// déclencher une SmartHomeException qui va péter un rollabck alors que
		// lecture seule sur la transaction principale. Même avec un @NotTransaction
		// ca ne marche pas
		try {
			Defi.withTransaction([readOnly: true, propagationBehavior: TransactionDefinition.PROPAGATION_REQUIRES_NEW]) {
				// un seul chargement de données qui est passé ensuite aux graphes
				// pour ne pas devoir charger à chaque graphe
				if (model.currentDefi) {
					model.electricite.consos = defiService.loadUserConso(model.currentDefi,
							model.house, DefiCompteurEnum.electricite)
					model.participant?.injectResultat(model.electricite.consos, DefiCompteurEnum.electricite)
				}

				model.electricite.chartTotal = defiService.chartTotal(model.currentDefi, model.electricite.consos)
				model.electricite.chartConso = defiService.chartUserDay(model.currentDefi, model.electricite.consos)
			}
		} catch (SmartHomeException ex) {
			model.electricite.error = ex.message
		}


		// charge les données gaz et construit les graphes
		// on passe dans des transactions séparées car le service appelé peut
		// déclencher une SmartHomeException qui va péter un rollabck alors que
		// lecture seule sur la transaction principale. Même avec un @NotTransaction
		// ca ne marche pas
		try {
			Defi.withTransaction([readOnly: true, propagationBehavior: TransactionDefinition.PROPAGATION_REQUIRES_NEW]) {
				// un seul chargement de données qui est passé ensuite aux graphes
				// pour ne pas devoir charger à chaque graphe
				if (model.currentDefi) {
					model.gaz.consos = defiService.loadUserConso(model.currentDefi,
							model.house, DefiCompteurEnum.gaz)
					model.participant?.injectResultat(model.gaz.consos, DefiCompteurEnum.gaz)
				}

				model.gaz.chartTotal = defiService.chartTotal(model.currentDefi, model.gaz.consos)
				model.gaz.chartConso = defiService.chartUserDay(model.currentDefi, model.gaz.consos)
			}
		} catch (SmartHomeException ex) {
			model.gaz.error = ex.message
		}

		// passer toutes les consos de chaque compteur dans cette méthode
		// pour calculer les consos globales et créer les chart correspondants
		if (model.currentDefi) {
			model.global.consos = defiService.groupConsos(model.currentDefi, new ElecDefiConsoConverter(),
					model.electricite.consos, model.gaz.consos)
			model.participant?.injectResultat(model.global.consos, DefiCompteurEnum.global)
		}

		// force le type compteur pour affichage unité et conversion valeur
		model.global.chartTotal = defiService.chartTotal(model.currentDefi, model.global.consos,
			DefiCompteurEnum.electricite.converter)
		// le graphe détaillé est différent selon la granularité des consos
		// entre les différents compteurs. si un compteur elec est connecté
		// et le connecteur gaz non, alors beaucoup plus de valeurs sur l'élec et
		// les consos gaz seront toutes enregistrées sur une seule date. donc
		// le graphe à la journée ne sera pas représentatif. On passe dans ce
		// cas à un graphe à la semaine
		if (model.global.consos.bestView == "day") {
			model.global.chartConso = defiService.chartUserDay(model.currentDefi,
					model.global.consos, DefiCompteurEnum.electricite.converter)
		} else {
			model.global.chartConso = defiService.chartUserWeek(model.currentDefi,
					model.global.consos, DefiCompteurEnum.electricite.converter)
		}
		
		return model
	}


	@Override
	Map modelParticipants(DefiCommand command, Map model) throws SmartHomeException {
		if (model.currentDefi) {
			model.participants = defiService.listParticipantResultat(
					new DefiCommand(defi: model.currentDefi, profilPublic: true), [:])
		}
		
		return model
	}
	
	
	@Override
	Defi calculerConsommations(Defi defi) throws SmartHomeException {
		// precision des consos en config
		int precision = 0
		String configPrecision = configService.value(Config.GRAND_DEFI_CONSOMMATION_PRECISION)

		if (configPrecision) {
			precision = configPrecision.toInteger()
		}

		// charge tous les profils défi et reset sur la liste du défi
		// pour au final détecter les orphelins
		List<DefiProfil> defiProfils = defiService.listDefiProfilResultat(defi)
		defi.profils = []

		// charge tous les participants "à plat" et les réorganise par équipe
		// pour une structure en arbre
		List<DefiEquipeParticipant> participants = defiService.listParticipantResultat(new DefiCommand(defi: defi), [:])
		List<DefiEquipe> defiEquipes = defiService.extractDefiEquipe(participants)

		// charge les résultats au niveau des profils (défi + équipe)
		// ces listes seront complétées si des éléments manquent au moment des
		// enregistrements de résultat
		List<DefiEquipeProfil> defiEquipeProfilList = defiService.listEquipeProfilResultat(defi)

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
							consos = defiService.loadUserConso(defi, house, DefiCompteurEnum.electricite)
							participant.reference_electricite = NumberUtils.round(consos.totalReference, precision)
							participant.action_electricite = NumberUtils.round(consos.totalAction, precision)
						}

						// calcul conso gaz
						if (house[DefiCompteurEnum.gaz.property]) {
							consos = defiService.loadUserConso(defi, house, DefiCompteurEnum.gaz)
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
				defiEquipe.action_electricite = NumberUtils.round(defiEquipe.participants.sum { it.action_electricite ?: 0 }, precision)
				defiEquipe.reference_gaz = NumberUtils.round(defiEquipe.participants.sum { it.reference_gaz ?: 0 }, precision)
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
					defiEquipeProfil.action_electricite = NumberUtils.round(entry.value.sum { it.action_electricite ?: 0 }, precision)
					defiEquipeProfil.reference_gaz = NumberUtils.round(entry.value.sum { it.reference_gaz ?: 0 }, precision)
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
				defiProfil.action_electricite = NumberUtils.round(entry.value.sum { it.action_electricite ?: 0 }, precision)
				defiProfil.reference_gaz = NumberUtils.round(entry.value.sum { it.reference_gaz ?: 0 }, precision)
				defiProfil.action_gaz = NumberUtils.round(entry.value.sum { it.action_gaz ?: 0 }, precision)
			}
		} // GPars.pool

		// dernière passe pour le calcul total du défi
		defi.cleanResultat()
		defi.reference_electricite = NumberUtils.round(defiEquipes.sum { it.reference_electricite ?: 0 }, precision)
		defi.action_electricite = NumberUtils.round(defiEquipes.sum { it.action_electricite ?: 0 }, precision)
		defi.reference_gaz = NumberUtils.round(defiEquipes.sum { it.reference_gaz ?: 0 }, precision)
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

		return defi
	}
	
}
