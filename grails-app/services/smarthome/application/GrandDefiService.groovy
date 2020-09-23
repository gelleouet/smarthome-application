/**
 * 
 */
package smarthome.application

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.annotation.Transactional

import grails.transaction.NotTransactional

import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import smarthome.application.granddefi.AccountCommand
import smarthome.automation.House
import smarthome.automation.HouseService
import smarthome.core.AbstractService
import smarthome.core.AsynchronousWorkflow
import smarthome.core.Config
import smarthome.core.ConfigService
import smarthome.core.SmartHomeException
import smarthome.security.Profil
import smarthome.security.RegistrationCode
import smarthome.security.Role
import smarthome.security.User
import smarthome.security.UserAdmin
import smarthome.security.UserService

/**
 * @author gregory.elleouet@gmail.com<Grégory Elléoouet>
 *
 */
class GrandDefiService extends AbstractService {

	private static final int MAX_CLASSEMENT = 3

	HouseService houseService
	GrailsApplication grailsApplication
	UserService userService
	LinkGenerator grailsLinkGenerator
	ConfigService configService
	DefiService defiService


	/**
	 * Création du modèle pour l'affichage des consommations
	 * 
	 * @param user
	 * @param typeCompteur
	 * 
	 * @return
	 * @throws SmartHomeException
	 */
	Map modelConsommation(User user, DefiCompteurEnum typeCompteur) throws SmartHomeException {
		Map model = [user: user]

		model.house = houseService.findDefaultByUser(user)

		return model
	}


	private Map defaultModelResultat(DefiCommand command) throws SmartHomeException {
		Map model = [user: command.user, command: command]

		model.defis = defiService.listByUser(command, [max: 1])
		model.currentDefi = command.defi ?: (model.defis ? model.defis[0] : null)

		if (model.currentDefi) {
			def myResultat = defiService.findUserResultat(model.currentDefi, command.user)

			if (!myResultat) {
				throw new SmartHomeException("Accès refusé au défi !")
			}

			// on se branche soit sur ses propres résultats, soit sur les résultats
			// d'un autre participant
			// la recherche de la config de la house doit s'adapter (elle sert pour
			// le chargement des consos individuelles à partir des compteurs)
			if (command.defiEquipeParticipant) {
				model.participant = command.defiEquipeParticipant
				model.house = houseService.findDefaultByUser(model.participant.user)
			} else {
				model.participant = myResultat
				model.house = houseService.findDefaultByUser(command.user)
			}

			if (!model.house) {
				throw new SmartHomeException("Profil incomplet !")
			}

			model.participant.house = model.house
			// IMPORTANT !! bien associé le house avant de calculer le groupKey
			// car il se base sur cette info
			model.participant.groupKey = defiService.groupKeyParticipant(model.participant)

			// on se branche soit sur une équipe indiquée soit sur l'équipe
			// du user connecté
			if (command.defiEquipe) {
				model.equipe = command.defiEquipe
			} else {
				// ATTENTION !! on recherche l'équipe du participant et non pas du
				// user connecté, car si participant sélectionné, on doit afficher
				// son équipe
				model.equipe = defiService.findEquipeResultat(model.currentDefi, model.participant.user)
			}
		}

		// prépare le modèle par compteur
		// on duplique le total classement pour q'il soit accessible depuis la map
		// principale de chaque compteur qui sera délèguée à des templates
		model.electricite = [type: DefiCompteurEnum.electricite, consos: [:]]
		model.gaz = [type: DefiCompteurEnum.gaz, consos: [:]]
		model.global = [type: DefiCompteurEnum.global, consos: [:]]

		return model
	}


	/**
	 * Affichage participants / équipe
	 * 
	 * @param command
	 * @return
	 * @throws SmartHomeException
	 */
	Map modelParticipants(DefiCommand command) throws SmartHomeException {
		Map model = defaultModelResultat(command)

		// liste tous les participants avec profil public
		if (model.currentDefi) {
			model.participants = defiService.listParticipantResultat(
					new DefiCommand(defi: model.currentDefi, profilPublic: true), [:])
		}

		return model
	}


	/**
	 * Défi Résultats Défi
	 *
	 * @param command
	 * @return
	 * @throws SmartHomeException
	 */
	Map modelResultatsDefi(DefiCommand command) throws SmartHomeException {
		Map model = defaultModelResultat(command)

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
		model.global.chartTotal = defiService.chartTotal(model.currentDefi, model.global.consos)
		model.global.chartConso = defiService.chartProfil(model.currentDefi, model.global.consos,
				DefiCompteurEnum.global)

		model.global.chartClassement = defiService.chartClassement(model.currentDefi,
				model.global.classement, model.equipe)

		for (Profil profil : model.profils) {
			model["profil${ profil.id }"].chartClassement = defiService.chartClassement(model.currentDefi,
					model["profil${ profil.id }"].classement, model.equipe)
		}

		return model
	}


	/**
	 * Défi Résultats Equipe
	 *
	 * @param command
	 * @return
	 * @throws SmartHomeException
	 */
	Map modelResultatsEquipe(DefiCommand command) throws SmartHomeException {
		Map model = defaultModelResultat(command)

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
		model.global.chartTotal = defiService.chartTotal(model.currentDefi,
				model.global.consos)
		model.global.chartConso = defiService.chartProfil(model.currentDefi,
				model.global.consos, DefiCompteurEnum.global)

		model.electricite.chartTotal = defiService.chartTotal(model.currentDefi,
				model.electricite.consos)
		model.electricite.chartConso = defiService.chartProfil(model.currentDefi,
				model.electricite.consos, DefiCompteurEnum.electricite)

		model.gaz.chartTotal = defiService.chartTotal(model.currentDefi,
				model.gaz.consos)
		model.gaz.chartConso = defiService.chartProfil(model.currentDefi,
				model.gaz.consos, DefiCompteurEnum.gaz)

		return model
	}


	/**
	 * Défi Mes Résultats
	 * 
	 * @param command
	 * @return
	 * @throws SmartHomeException
	 */
	Map modelMesResultats(DefiCommand command) throws SmartHomeException {
		Map model = defaultModelResultat(command)

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

				model.electricite.chartTotal = defiService.chartTotal(model.currentDefi,
						model.electricite.consos)
				model.electricite.chartConso = defiService.chartUserDay(model.currentDefi,
						model.electricite.consos)
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

				model.gaz.chartTotal = defiService.chartTotal(model.currentDefi,
						model.gaz.consos)
				model.gaz.chartConso = defiService.chartUserDay(model.currentDefi,
						model.gaz.consos)
			}
		} catch (SmartHomeException ex) {
			model.gaz.error = ex.message
		}

		// passer toutes les consos de chaque compteur dans cette méthode
		// pour calculer les consos globales et créer les chart correspondants
		if (model.currentDefi) {
			model.global.consos = defiService.groupConsos(model.currentDefi,
					model.electricite.consos, model.gaz.consos)
			model.participant?.injectResultat(model.global.consos, DefiCompteurEnum.global)
		}

		model.global.chartTotal = defiService.chartTotal(model.currentDefi,
				model.global.consos)
		// le graphe détaillé est différent selon la granularité des consos
		// entre les différents compteurs. si un compteur elec est connecté
		// et le connecteur gaz non, alors beaucoup plus de valeurs sur l'élec et
		// les consos gaz seront toutes enregistrées sur une seule date. donc
		// le graphe à la journée ne sera pas représentatif. On passe dans ce
		// cas à un graphe à la semaine
		if (model.global.consos.bestView == "day") {
			model.global.chartConso = defiService.chartUserDay(model.currentDefi,
					model.global.consos)
		} else {
			model.global.chartConso = defiService.chartUserWeek(model.currentDefi,
					model.global.consos)
		}

		return model
	}


	/**
	 * Création d'un compte
	 *
	 * @param username
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousWorkflow("registerService.createAccount")
	RegistrationCode createAccount(AccountCommand account) throws SmartHomeException {
		log.info "Demande création compte ${account.username}"

		// on vérifie que l'adresse n'est pas déjà prise
		User user = User.findByUsername(account.username)

		if (user) {
			throw new SmartHomeException("Un compte existe déjà avec cette adresse !", account)
		}
		
		// construction des équipes "à la volée" et association avec une équipe
		// et le grand défi en cours
		String grandDefiId = configService.value(Config.GRAND_DEFI_ID)

		if (!grandDefiId) {
			throw new SmartHomeException("Les inscriptions pour le Grand Défi ne sont plus autorisées", account)
		}

		// création d'un code d'enregistrement
		RegistrationCode registrationCode = new RegistrationCode(username: account.username)
		registrationCode.serverUrl = grailsLinkGenerator.link(controller: 'register', action: 'confirmAccount',
		params: [username: account.username, token: registrationCode.token], absolute: true)

		if (!registrationCode.save()) {
			throw new SmartHomeException("Erreur création d'un token d'activation !", account)
		}

		// création d'un user avec compte bloqué en attente déblocage
		// et attribution du role GRAND_DEFI
		user = new User(username: account.username, password: account.newPassword, prenom: account.prenom,
		nom: account.nom, lastActivation: new Date(), accountLocked: true,
		applicationKey: UUID.randomUUID(), profilPublic: account.profilPublic,
		profil: account.profil)
		
		try {
			userService.save(user, false)
		} catch (SmartHomeException ex) {
			// rethrow l'erreur en spécifiant le bon command et les bonnes erreurs
			throw new SmartHomeException(ex.message, account, user)
		}

		// inscription
		try {
			inscriptionDefi(user, grandDefiId as Long, [chauffage: account.chauffage,
				ecs: account.ecs, surface: account.surface,
				location: account.commune.libelle + ", France"])
		} catch (SmartHomeException ex) {
			// rethrow l'erreur en spécifiant le bon command et les bonnes erreurs
			throw new SmartHomeException(ex.message, account)
		}

		return registrationCode
	}
	

	/**
	 * Inscription à un défi 
	 * 	
	 * @param user
	 * @param defiId
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	DefiEquipeParticipant inscriptionDefi(User user, Long defiId, Map houseinfo) throws SmartHomeException {
		// Association à un admin (pour la validation des index)
		String adminIds = configService.value(Config.GRAND_DEFI_ADMIN_IDS)

		// suppression des anciennes asso si déjà inscrit
		UserAdmin.where{ user == user}.deleteAll()
		
		if (adminIds) {
			for (String adminId : adminIds.split(",")) {
				userService.save(new UserAdmin(user: user, admin: User.read(adminId.trim() as Long)))
			}
		}
		
		// création ou récupération d'une maison par défaut
		House defaultHouse = houseService.bindDefault(user, houseinfo)
		
		return defiService.inscription(user, defiId, defaultHouse.location ?: "Non renseigné")
	}
	
	
	/**
	 * 
	 * @param user
	 * @param defiId
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void desinscriptionDefi(User user, Long defiId) throws SmartHomeException {
		defiService.desinscription(user, defiId)
	}

}
