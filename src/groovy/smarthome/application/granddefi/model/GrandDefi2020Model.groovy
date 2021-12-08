package smarthome.application.granddefi.model

import javax.servlet.ServletResponse

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.TransactionDefinition
import java.awt.Color
import groovyx.gpars.GParsPool
import smarthome.application.AbstractDefiResultat
import smarthome.application.Defi
import smarthome.application.DefiCommand
import smarthome.application.DefiCompteurEnum
import smarthome.application.DefiEquipe
import smarthome.application.DefiEquipeParticipant
import smarthome.application.DefiEquipeProfil
import smarthome.application.DefiProfil
import smarthome.application.DefiService
import smarthome.automation.House
import smarthome.automation.HouseService
import smarthome.core.Config
import smarthome.core.ConfigService
import smarthome.core.NumberUtils
import smarthome.core.SmartHomeException
import smarthome.security.Profil
import smarthome.core.ExcelUtils
import smarthome.core.MimeTypeEnum

import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.VerticalAlignment

/**
 * !! IMPORTANT : ne pas hériter d'un autre model car sion ils ne peuvent pas évoluer
 * indépendamment
 * 
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class GrandDefi2020Model extends AbstractDefiModel {
	
	protected static final int MAX_CLASSEMENT = 3
	protected static final long PARTICULIER_PROFIL_ID = 1L
	protected static final long COMMERCE_PROFIL_ID = 2L
	protected static final long BATIMENT_PROFIL_ID = 3L
	
	@Autowired
	DefiService defiService
	
	@Autowired
	ConfigService configService
	
	@Autowired
	HouseService houseService
	
	
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
		
		
		// charge les données eau et construit les graphes
		// on passe dans des transactions séparées car le service appelé peut
		// déclencher une SmartHomeException qui va péter un rollabck alors que
		// lecture seule sur la transaction principale. Même avec un @NotTransaction
		// ca ne marche pas
		try {
			Defi.withTransaction([readOnly: true, propagationBehavior: TransactionDefinition.PROPAGATION_REQUIRES_NEW]) {
				// un seul chargement de données qui est passé ensuite aux graphes
				// pour ne pas devoir charger à chaque graphe
				if (model.currentDefi) {
					model.eau.consos = defiService.loadUserConso(model.currentDefi,
							model.house, DefiCompteurEnum.eau)
					model.participant?.injectResultat(model.eau.consos, DefiCompteurEnum.eau)
				}

				model.eau.chartTotal = defiService.chartTotal(model.currentDefi, model.eau.consos)
				model.eau.chartConso = defiService.chartUserDay(model.currentDefi, model.eau.consos)
			}
		} catch (SmartHomeException ex) {
			model.eau.error = ex.message
		}

		
		// passer toutes les consos de chaque compteur dans cette méthode
		// pour calculer les consos globales
		if (model.currentDefi) {
			model.participant?.injectResultat(model.global.consos, DefiCompteurEnum.global)
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
			
			model.eau.consos.values = model.global.consos.values
			model.equipe.injectResultat(model.eau.consos, DefiCompteurEnum.eau)
		}

		// lance dans tous les cas, les charts pour avoir des vues un minimum
		// complétées (sinon page vide et c'est moins fun)
		model.electricite.chartTotal = defiService.chartTotal(model.currentDefi, model.electricite.consos)
		model.electricite.chartConso = defiService.chartProfil(model.currentDefi, model.electricite.consos)

		model.gaz.chartTotal = defiService.chartTotal(model.currentDefi, model.gaz.consos)
		model.gaz.chartConso = defiService.chartProfil(model.currentDefi, model.gaz.consos)
		
		model.eau.chartTotal = defiService.chartTotal(model.currentDefi, model.eau.consos)
		model.eau.chartConso = defiService.chartProfil(model.currentDefi, model.eau.consos)
		
		return model
	}
	
	
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
			
			model.energie.consos.values = model.global.consos.values
			model.currentDefi.injectResultat(model.energie.consos, DefiCompteurEnum.energie)
			model.eau.consos.values = model.global.consos.values
			model.currentDefi.injectResultat(model.eau.consos, DefiCompteurEnum.eau)
			

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
		model.energie.chartTotal = defiService.chartTotal(model.currentDefi, model.energie.consos)
		model.energie.chartConso = defiService.chartProfil(model.currentDefi, model.energie.consos)
		model.eau.chartTotal = defiService.chartTotal(model.currentDefi, model.eau.consos)
		model.eau.chartConso = defiService.chartProfil(model.currentDefi, model.eau.consos)

		model.global.chartClassement = defiService.chartClassement(model.currentDefi,
				model.global.classement, model.equipe)

		for (Profil profil : model.profils) {
			model["profil${ profil.id }"].chartClassement = defiService.chartClassement(model.currentDefi,
					model["profil${ profil.id }"].classement, model.equipe)
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
						
						// calcul conso eau
						if (house[DefiCompteurEnum.eau.property]) {
							consos = defiService.loadUserConso(defi, house, DefiCompteurEnum.eau)
							participant.reference_eau = NumberUtils.round(consos.totalReference, precision)
							participant.action_eau = NumberUtils.round(consos.totalAction, precision)
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
				defiEquipe.reference_eau = NumberUtils.round(defiEquipe.participants.sum { it.reference_eau ?: 0 }, precision)
				defiEquipe.action_eau = NumberUtils.round(defiEquipe.participants.sum { it.action_eau ?: 0 }, precision)

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
					defiEquipeProfil.reference_eau = NumberUtils.round(entry.value.sum { it.reference_eau ?: 0 }, precision)
					defiEquipeProfil.action_eau = NumberUtils.round(entry.value.sum { it.action_eau ?: 0 }, precision)
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
				defiProfil.reference_eau = NumberUtils.round(entry.value.sum { it.reference_eau ?: 0 }, precision)
				defiProfil.action_eau = NumberUtils.round(entry.value.sum { it.action_eau ?: 0 }, precision)
			}
		} // GPars.pool

		// dernière passe pour le calcul total du défi
		defi.cleanResultat()
		defi.reference_electricite = NumberUtils.round(defiEquipes.sum { it.reference_electricite ?: 0 }, precision)
		defi.action_electricite = NumberUtils.round(defiEquipes.sum { it.action_electricite ?: 0 }, precision)
		defi.reference_gaz = NumberUtils.round(defiEquipes.sum { it.reference_gaz ?: 0 }, precision)
		defi.action_gaz = NumberUtils.round(defiEquipes.sum { it.action_gaz ?: 0 }, precision)
		defi.reference_eau = NumberUtils.round(defiEquipes.sum { it.reference_eau ?: 0 }, precision)
		defi.action_eau = NumberUtils.round(defiEquipes.sum { it.action_eau ?: 0 }, precision)

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
	
	
	@Override
	void export(DefiCommand command, Map modeles, ServletResponse response) throws SmartHomeException {
		MimeTypeEnum mimeType = MimeTypeEnum.EXCEL2007
		ExcelUtils excelUtils = new ExcelUtils().createWorkbook(mimeType).build()
		Sheet sheet = excelUtils.createSheet("Résultats")
		AbstractDefiResultat resultatBatiment 
		AbstractDefiResultat resultatCommerce
		AbstractDefiResultat resultatParticulier
		
		Map styleNormal = [name: "styleNormal", border: BorderStyle.THIN]
		
		// BLOC MES RESULTATS
		// ---------------------------------------------------------------------
		
		Map styleMesResultats = [name: "styleMesResultats", color: new Color(255, 242, 204), hAlign: HorizontalAlignment.CENTER,
			vAlign: VerticalAlignment.CENTER, border: BorderStyle.MEDIUM]
		
		Map userGlobal = modeles.modelMesResultats.global.consos
		Map userElec = modeles.modelMesResultats.electricite.consos
		Map userGaz = modeles.modelMesResultats.gaz.consos
		Map userEau = modeles.modelMesResultats.eau.consos
		
		excelUtils.mergeCell(sheet, 0, 0, 0, 3, "MES RESULTATS", styleMesResultats)
		
		excelUtils.mergeCell(sheet, 1, 2, 0, 1, "Consommations globales", styleMesResultats)
		excelUtils.createOrGetCell(sheet, 1, 2, styleNormal).setCellValue("Mes économies (%)")
		excelUtils.createOrGetCell(sheet, 1, 3, styleNormal).setCellValue(userGlobal.economie)
		excelUtils.createOrGetCell(sheet, 2, 2, styleNormal).setCellValue("Mon classement dans le Grand Défi Energie & Eau")
		excelUtils.createOrGetCell(sheet, 2, 3, styleNormal).setCellValue("${userGlobal.classement} / ${userGlobal.total}")
		
		excelUtils.mergeCell(sheet, 3, 8, 0, 1, "Consommations d'électricité", styleMesResultats)
		excelUtils.createOrGetCell(sheet, 3, 2, styleNormal).setCellValue("Référence (kWh)")
		excelUtils.createOrGetCell(sheet, 3, 3, styleNormal).setCellValue(userElec.reference)
		excelUtils.createOrGetCell(sheet, 4, 2, styleNormal).setCellValue("Action (kWh)")
		excelUtils.createOrGetCell(sheet, 4, 3, styleNormal).setCellValue(userElec.action)
		excelUtils.createOrGetCell(sheet, 5, 2, styleNormal).setCellValue("Différence (kWh)")
		excelUtils.createOrGetCell(sheet, 5, 3, styleNormal).setCellValue(userElec.difference)
		excelUtils.createOrGetCell(sheet, 6, 2, styleNormal).setCellValue("Evolution de mes consommations (%)")
		excelUtils.createOrGetCell(sheet, 6, 3, styleNormal).setCellValue(userElec.evolution)
		excelUtils.createOrGetCell(sheet, 7, 2, styleNormal).setCellValue("Moyenne des évolutions des consommations (%)")
		excelUtils.createOrGetCell(sheet, 7, 3, styleNormal).setCellValue(userElec.economie)
		excelUtils.createOrGetCell(sheet, 8, 2, styleNormal).setCellValue("Mes économies (%)")
		excelUtils.createOrGetCell(sheet, 8, 3, styleNormal).setCellValue(userElec.economie)
		
		excelUtils.mergeCell(sheet, 9, 14, 0, 1, "Consommations de gaz", styleMesResultats)
		excelUtils.createOrGetCell(sheet, 9, 2, styleNormal).setCellValue("Référence (kWh)")
		excelUtils.createOrGetCell(sheet, 9, 3, styleNormal).setCellValue(userGaz.reference)
		excelUtils.createOrGetCell(sheet, 10, 2, styleNormal).setCellValue("Action (kWh)")
		excelUtils.createOrGetCell(sheet, 10, 3, styleNormal).setCellValue(userGaz.action)
		excelUtils.createOrGetCell(sheet, 11, 2, styleNormal).setCellValue("Différence (kWh)")
		excelUtils.createOrGetCell(sheet, 11, 3, styleNormal).setCellValue(userGaz.difference)
		excelUtils.createOrGetCell(sheet, 12, 2, styleNormal).setCellValue("Evolution de mes consommations (%)")
		excelUtils.createOrGetCell(sheet, 12, 3, styleNormal).setCellValue(userGaz.evolution)
		excelUtils.createOrGetCell(sheet, 13, 2, styleNormal).setCellValue("Moyenne des évolutions des consommations (%)")
		excelUtils.createOrGetCell(sheet, 13, 3, styleNormal).setCellValue(userGaz.moyenne)
		excelUtils.createOrGetCell(sheet, 14, 2, styleNormal).setCellValue("Mes économies (%)")
		excelUtils.createOrGetCell(sheet, 14, 3, styleNormal).setCellValue(userGaz.economie)
		
		excelUtils.mergeCell(sheet, 15, 20, 0, 1, "Consommations d'eau", styleMesResultats)
		excelUtils.createOrGetCell(sheet, 15, 2, styleNormal).setCellValue("Référence (L)")
		excelUtils.createOrGetCell(sheet, 15, 3, styleNormal).setCellValue(userEau.reference)
		excelUtils.createOrGetCell(sheet, 16, 2, styleNormal).setCellValue("Action (L)")
		excelUtils.createOrGetCell(sheet, 16, 3, styleNormal).setCellValue(userEau.action)
		excelUtils.createOrGetCell(sheet, 17, 2, styleNormal).setCellValue("Différence (L)")
		excelUtils.createOrGetCell(sheet, 17, 3, styleNormal).setCellValue(userEau.difference)
		excelUtils.createOrGetCell(sheet, 18, 2, styleNormal).setCellValue("Evolution de mes consommations (%)")
		excelUtils.createOrGetCell(sheet, 18, 3, styleNormal).setCellValue(userEau.evolution)
		excelUtils.createOrGetCell(sheet, 19, 2, styleNormal).setCellValue("Moyenne des évolutions des consommations (%)")
		excelUtils.createOrGetCell(sheet, 19, 3, styleNormal).setCellValue(userEau.moyenne)
		excelUtils.createOrGetCell(sheet, 20, 2, styleNormal).setCellValue("Mes économies (%)")
		excelUtils.createOrGetCell(sheet, 20, 3, styleNormal).setCellValue(userEau.economie)
		
		// BLOC RESULTATS EQUIPE
		// ---------------------------------------------------------------------
		
		Map styleResultatsEquipe = [name: "styleResultatsEquipe", color: new Color(226, 239, 218), hAlign: HorizontalAlignment.CENTER,
			vAlign: VerticalAlignment.CENTER, border: BorderStyle.MEDIUM]
		
		Map equipeGlobal = modeles.modelResultatsEquipe.global.consos
		Map equipeElec = modeles.modelResultatsEquipe.electricite.consos
		Map equipeGaz = modeles.modelResultatsEquipe.gaz.consos
		Map equipeEau = modeles.modelResultatsEquipe.eau.consos
		resultatBatiment = findResultatByProfil(equipeGlobal.values, BATIMENT_PROFIL_ID)
		resultatCommerce = findResultatByProfil(equipeGlobal.values, COMMERCE_PROFIL_ID)
		resultatParticulier = findResultatByProfil(equipeGlobal.values, PARTICULIER_PROFIL_ID)
		
		excelUtils.mergeCell(sheet, 21, 21, 0, 3, "RESULTATS EQUIPE", styleResultatsEquipe)
		
		excelUtils.mergeCell(sheet, 22, 26, 0, 1, "Consommations globales", styleResultatsEquipe)
		excelUtils.createOrGetCell(sheet, 22, 2, styleNormal).setCellValue("Economies Bâtiments publics (%)")
		excelUtils.createOrGetCell(sheet, 22, 3, styleNormal).setCellValue(resultatBatiment?.economie_global())
		excelUtils.createOrGetCell(sheet, 23, 2, styleNormal).setCellValue("Economies Commerce (%)")
		excelUtils.createOrGetCell(sheet, 23, 3, styleNormal).setCellValue(resultatCommerce?.economie_global())
		excelUtils.createOrGetCell(sheet, 24, 2, styleNormal).setCellValue("Economies Particuliers (%)")
		excelUtils.createOrGetCell(sheet, 24, 3, styleNormal).setCellValue(resultatParticulier?.economie_global())
		excelUtils.createOrGetCell(sheet, 25, 2, styleNormal).setCellValue("Economies Equipe (%)")
		excelUtils.createOrGetCell(sheet, 25, 3, styleNormal).setCellValue(equipeGlobal.economie)
		excelUtils.createOrGetCell(sheet, 26, 2, styleNormal).setCellValue("Classement de mon équipe dans le Grand Défi Energie et Eau")
		excelUtils.createOrGetCell(sheet, 26, 3, styleNormal).setCellValue("${equipeGlobal.classement} / ${equipeGlobal.total}")
		
		excelUtils.mergeCell(sheet, 27, 30, 0, 1, "Consommations d'électricité", styleResultatsEquipe)
		excelUtils.createOrGetCell(sheet, 27, 2, styleNormal).setCellValue("Référence (kWh)")
		excelUtils.createOrGetCell(sheet, 27, 3, styleNormal).setCellValue(equipeElec.reference)
		excelUtils.createOrGetCell(sheet, 28, 2, styleNormal).setCellValue("Action (kWh)")
		excelUtils.createOrGetCell(sheet, 28, 3, styleNormal).setCellValue(equipeElec.action)
		excelUtils.createOrGetCell(sheet, 29, 2, styleNormal).setCellValue("Différence (kWh)")
		excelUtils.createOrGetCell(sheet, 29, 3, styleNormal).setCellValue(equipeElec.difference)
		excelUtils.createOrGetCell(sheet, 30, 2, styleNormal).setCellValue("Economies Equipe (%)")
		excelUtils.createOrGetCell(sheet, 30, 3, styleNormal).setCellValue(equipeElec.economie)
		
		excelUtils.mergeCell(sheet, 31, 34, 0, 1, "Consommations de gaz", styleResultatsEquipe)
		excelUtils.createOrGetCell(sheet, 31, 2, styleNormal).setCellValue("Référence (kWh)")
		excelUtils.createOrGetCell(sheet, 31, 3, styleNormal).setCellValue(equipeGaz.reference)
		excelUtils.createOrGetCell(sheet, 32, 2, styleNormal).setCellValue("Action (kWh)")
		excelUtils.createOrGetCell(sheet, 32, 3, styleNormal).setCellValue(equipeGaz.action)
		excelUtils.createOrGetCell(sheet, 33, 2, styleNormal).setCellValue("Différence (kWh)")
		excelUtils.createOrGetCell(sheet, 33, 3, styleNormal).setCellValue(equipeGaz.difference)
		excelUtils.createOrGetCell(sheet, 34, 2, styleNormal).setCellValue("Economies Equipe (%)")
		excelUtils.createOrGetCell(sheet, 34, 3, styleNormal).setCellValue(equipeGaz.economie)
		
		excelUtils.mergeCell(sheet, 35, 38, 0, 1, "Consommations d'eau", styleResultatsEquipe)
		excelUtils.createOrGetCell(sheet, 35, 2, styleNormal).setCellValue("Référence (L)")
		excelUtils.createOrGetCell(sheet, 35, 3, styleNormal).setCellValue(equipeEau.reference)
		excelUtils.createOrGetCell(sheet, 36, 2, styleNormal).setCellValue("Action (L)")
		excelUtils.createOrGetCell(sheet, 36, 3, styleNormal).setCellValue(equipeEau.action)
		excelUtils.createOrGetCell(sheet, 37, 2, styleNormal).setCellValue("Différence (L)")
		excelUtils.createOrGetCell(sheet, 37, 3, styleNormal).setCellValue(equipeEau.difference)
		excelUtils.createOrGetCell(sheet, 38, 2, styleNormal).setCellValue("Economies Equipe (%)")
		excelUtils.createOrGetCell(sheet, 38, 3, styleNormal).setCellValue(equipeEau.economie)
		
		// BLOC RESULTATS DEFI
		// ---------------------------------------------------------------------
		
		Map styleResultatsDefi = [name: "styleResultatsDefi", color: new Color(217, 225, 242), hAlign: HorizontalAlignment.CENTER,
			vAlign: VerticalAlignment.CENTER, border: BorderStyle.MEDIUM, wrapped: true]
		
		List classementGlobal = modeles.modelResultatsDefi.global.classement
		List classementBatiment = modeles.modelResultatsDefi["profil${BATIMENT_PROFIL_ID}"].classement
		List classementCommerce = modeles.modelResultatsDefi["profil${COMMERCE_PROFIL_ID}"].classement
		List classementParticulier = modeles.modelResultatsDefi["profil${PARTICULIER_PROFIL_ID}"].classement
		Map defiEnergie = modeles.modelResultatsDefi.energie.consos
		Map defiEau = modeles.modelResultatsDefi.eau.consos
		resultatBatiment = findResultatByProfil(defiEnergie.values, BATIMENT_PROFIL_ID)
		resultatCommerce = findResultatByProfil(defiEnergie.values, COMMERCE_PROFIL_ID)
		resultatParticulier = findResultatByProfil(defiEnergie.values, PARTICULIER_PROFIL_ID)
		
		excelUtils.mergeCell(sheet, 39, 39, 0, 3, "RESULTATS DEFI", styleResultatsDefi)
		
		excelUtils.mergeCell(sheet, 40, 51, 0, 0, "Consommations totales d'énergie", styleResultatsDefi)
		excelUtils.mergeCell(sheet, 40, 42, 1, 1, "Bâtiment public", styleResultatsDefi)
		excelUtils.createOrGetCell(sheet, 40, 2, styleNormal).setCellValue("Référence (kWh)")
		excelUtils.createOrGetCell(sheet, 40, 3, styleNormal).setCellValue(resultatBatiment?.reference_energie())
		excelUtils.createOrGetCell(sheet, 41, 2, styleNormal).setCellValue("Action (kWh)")
		excelUtils.createOrGetCell(sheet, 41, 3, styleNormal).setCellValue(resultatBatiment?.action_energie())
		excelUtils.createOrGetCell(sheet, 42, 2, styleNormal).setCellValue("Différence (kWh)")
		excelUtils.createOrGetCell(sheet, 42, 3, styleNormal).setCellValue(resultatBatiment?.difference_energie())
		excelUtils.mergeCell(sheet, 43, 45, 1, 1, "Commerce", styleResultatsDefi)
		excelUtils.createOrGetCell(sheet, 43, 2, styleNormal).setCellValue("Référence (kWh)")
		excelUtils.createOrGetCell(sheet, 43, 3, styleNormal).setCellValue(resultatCommerce?.reference_energie())
		excelUtils.createOrGetCell(sheet, 44, 2, styleNormal).setCellValue("Action (kWh)")
		excelUtils.createOrGetCell(sheet, 44, 3, styleNormal).setCellValue(resultatCommerce?.action_energie())
		excelUtils.createOrGetCell(sheet, 45, 2, styleNormal).setCellValue("Différence (kWh)")
		excelUtils.createOrGetCell(sheet, 45, 3, styleNormal).setCellValue(resultatCommerce?.difference_energie())
		excelUtils.mergeCell(sheet, 46, 48, 1, 1, "Particulier", styleResultatsDefi)
		excelUtils.createOrGetCell(sheet, 46, 2, styleNormal).setCellValue("Référence (kWh)")
		excelUtils.createOrGetCell(sheet, 46, 3, styleNormal).setCellValue(resultatParticulier?.reference_energie())
		excelUtils.createOrGetCell(sheet, 47, 2, styleNormal).setCellValue("Action (kWh)")
		excelUtils.createOrGetCell(sheet, 47, 3, styleNormal).setCellValue(resultatParticulier?.action_energie())
		excelUtils.createOrGetCell(sheet, 48, 2, styleNormal).setCellValue("Différence (kWh)")
		excelUtils.createOrGetCell(sheet, 48, 3, styleNormal).setCellValue(resultatParticulier?.difference_energie())
		excelUtils.mergeCell(sheet, 49, 51, 1, 1, "Défi", styleResultatsDefi)
		excelUtils.createOrGetCell(sheet, 49, 2, styleNormal).setCellValue("Référence (kWh)")
		excelUtils.createOrGetCell(sheet, 49, 3, styleNormal).setCellValue(defiEnergie.reference)
		excelUtils.createOrGetCell(sheet, 50, 2, styleNormal).setCellValue("Action (kWh)")
		excelUtils.createOrGetCell(sheet, 50, 3, styleNormal).setCellValue(defiEnergie.action)
		excelUtils.createOrGetCell(sheet, 51, 2, styleNormal).setCellValue("Différence (kWh)")
		excelUtils.createOrGetCell(sheet, 51, 3, styleNormal).setCellValue(defiEnergie.difference)
		
		excelUtils.mergeCell(sheet, 52, 63, 0, 0, "Consommations totales d'eau", styleResultatsDefi)
		excelUtils.mergeCell(sheet, 52, 54, 1, 1, "Bâtiment public", styleResultatsDefi)
		excelUtils.createOrGetCell(sheet, 52, 2, styleNormal).setCellValue("Référence (L)")
		excelUtils.createOrGetCell(sheet, 52, 3, styleNormal).setCellValue(resultatBatiment?.reference_eau())
		excelUtils.createOrGetCell(sheet, 53, 2, styleNormal).setCellValue("Action (L)")
		excelUtils.createOrGetCell(sheet, 53, 3, styleNormal).setCellValue(resultatBatiment?.action_eau())
		excelUtils.createOrGetCell(sheet, 54, 2, styleNormal).setCellValue("Différence (L)")
		excelUtils.createOrGetCell(sheet, 54, 3, styleNormal).setCellValue(resultatBatiment?.difference_eau())
		excelUtils.mergeCell(sheet, 55, 57, 1, 1, "Commerce", styleResultatsDefi)
		excelUtils.createOrGetCell(sheet, 55, 2, styleNormal).setCellValue("Référence (L)")
		excelUtils.createOrGetCell(sheet, 55, 3, styleNormal).setCellValue(resultatCommerce?.reference_eau())
		excelUtils.createOrGetCell(sheet, 56, 2, styleNormal).setCellValue("Action (L)")
		excelUtils.createOrGetCell(sheet, 56, 3, styleNormal).setCellValue(resultatCommerce?.action_eau())
		excelUtils.createOrGetCell(sheet, 57, 2, styleNormal).setCellValue("Différence (L)")
		excelUtils.createOrGetCell(sheet, 57, 3, styleNormal).setCellValue(resultatCommerce?.difference_eau())
		excelUtils.mergeCell(sheet, 58, 60, 1, 1, "Particulier", styleResultatsDefi)
		excelUtils.createOrGetCell(sheet, 58, 2, styleNormal).setCellValue("Référence (L)")
		excelUtils.createOrGetCell(sheet, 58, 3, styleNormal).setCellValue(resultatParticulier?.reference_eau())
		excelUtils.createOrGetCell(sheet, 59, 2, styleNormal).setCellValue("Action (L)")
		excelUtils.createOrGetCell(sheet, 59, 3, styleNormal).setCellValue(resultatParticulier?.action_eau())
		excelUtils.createOrGetCell(sheet, 60, 2, styleNormal).setCellValue("Différence (L)")
		excelUtils.createOrGetCell(sheet, 60, 3, styleNormal).setCellValue(resultatParticulier?.difference_eau())
		excelUtils.mergeCell(sheet, 61, 63, 1, 1, "Défi", styleResultatsDefi)
		excelUtils.createOrGetCell(sheet, 61, 2, styleNormal).setCellValue("Référence (L)")
		excelUtils.createOrGetCell(sheet, 61, 3, styleNormal).setCellValue(defiEau.reference)
		excelUtils.createOrGetCell(sheet, 62, 2, styleNormal).setCellValue("Action (L)")
		excelUtils.createOrGetCell(sheet, 62, 3, styleNormal).setCellValue(defiEau.action)
		excelUtils.createOrGetCell(sheet, 63, 2, styleNormal).setCellValue("Différence (L)")
		excelUtils.createOrGetCell(sheet, 63, 3, styleNormal).setCellValue(defiEau.difference)
		
		excelUtils.mergeCell(sheet, 64, 75, 0, 0, "Classement", styleResultatsDefi)
		excelUtils.mergeCell(sheet, 64, 66, 1, 1, "Général", styleResultatsDefi)
		excelUtils.createOrGetCell(sheet, 64, 2, styleNormal).setCellValue("1er : ${libelleClassement(classementGlobal, 0) }")
		excelUtils.createOrGetCell(sheet, 64, 3, styleNormal).setCellValue(economieClassement(classementGlobal, 0))
		excelUtils.createOrGetCell(sheet, 65, 2, styleNormal).setCellValue("2e : ${libelleClassement(classementGlobal, 1) }")
		excelUtils.createOrGetCell(sheet, 65, 3, styleNormal).setCellValue(economieClassement(classementGlobal, 1))
		excelUtils.createOrGetCell(sheet, 66, 2, styleNormal).setCellValue("3e : ${libelleClassement(classementGlobal, 2) }")
		excelUtils.createOrGetCell(sheet, 66, 3, styleNormal).setCellValue(economieClassement(classementGlobal, 2))
		excelUtils.mergeCell(sheet, 67, 69, 1, 1, "Bâtiment public", styleResultatsDefi)
		excelUtils.createOrGetCell(sheet, 67, 2, styleNormal).setCellValue("1er : ${libelleClassement(classementBatiment, 0) }")
		excelUtils.createOrGetCell(sheet, 67, 3, styleNormal).setCellValue(economieClassement(classementBatiment, 0))
		excelUtils.createOrGetCell(sheet, 68, 2, styleNormal).setCellValue("2e : ${libelleClassement(classementBatiment, 1) }")
		excelUtils.createOrGetCell(sheet, 68, 3, styleNormal).setCellValue(economieClassement(classementBatiment, 1))
		excelUtils.createOrGetCell(sheet, 69, 2, styleNormal).setCellValue("3e : ${libelleClassement(classementBatiment, 2) }")
		excelUtils.createOrGetCell(sheet, 69, 3, styleNormal).setCellValue(economieClassement(classementBatiment, 2))
		excelUtils.mergeCell(sheet, 70, 72, 1, 1, "Commerce", styleResultatsDefi)
		excelUtils.createOrGetCell(sheet, 70, 2, styleNormal).setCellValue("1er : ${libelleClassement(classementCommerce, 0) }")
		excelUtils.createOrGetCell(sheet, 70, 3, styleNormal).setCellValue(economieClassement(classementCommerce, 0))
		excelUtils.createOrGetCell(sheet, 71, 2, styleNormal).setCellValue("2e : ${libelleClassement(classementCommerce, 1) }")
		excelUtils.createOrGetCell(sheet, 71, 3, styleNormal).setCellValue(economieClassement(classementCommerce, 1))
		excelUtils.createOrGetCell(sheet, 72, 2, styleNormal).setCellValue("3e : ${libelleClassement(classementCommerce, 2) }")
		excelUtils.createOrGetCell(sheet, 72, 3, styleNormal).setCellValue(economieClassement(classementCommerce, 2))
		excelUtils.mergeCell(sheet, 73, 75, 1, 1, "Particulier", styleResultatsDefi)
		excelUtils.createOrGetCell(sheet, 73, 2, styleNormal).setCellValue("1er : ${libelleClassement(classementParticulier, 0) }")
		excelUtils.createOrGetCell(sheet, 73, 3, styleNormal).setCellValue(economieClassement(classementParticulier, 0))
		excelUtils.createOrGetCell(sheet, 74, 2, styleNormal).setCellValue("2e : ${libelleClassement(classementParticulier, 1) }")
		excelUtils.createOrGetCell(sheet, 74, 3, styleNormal).setCellValue(economieClassement(classementParticulier, 1))
		excelUtils.createOrGetCell(sheet, 75, 2, styleNormal).setCellValue("3e : ${libelleClassement(classementParticulier, 2) }")
		excelUtils.createOrGetCell(sheet, 75, 3, styleNormal).setCellValue(economieClassement(classementParticulier, 2))
		
		
		// Mise en forme générale do document
		// ---------------------------------------------------------------------
		
		sheet.setColumnWidth(0, 20*256) // 20 caractères d'après la formule POI
		sheet.setColumnWidth(1, 20*256) // 20 caractères d'après la formule POI
		sheet.autoSizeColumn(2)
		
		excelUtils.writeTo(response, "export-defi-${command.defi.id}-.${mimeType.extension}")
	}
	
	
	AbstractDefiResultat findResultatByProfil(Collection values, long profilId) {
		values.find { it.profil.id == profilId }
	}
	
	Map findClassement(Collection values, int idx) {
		idx < values.size() ? values[idx] : [:]
	}
	
	String libelleClassement(Collection values, int idx) {
		findClassement(values, idx)?.libelle ?: ""
	}
	
	Double economieClassement(Collection values, int idx) {
		findClassement(values, idx)?.economie
	}
}
