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
import smarthome.core.ConfigService
import smarthome.core.SmartHomeException
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
	 * @return
	 * @throws SmartHomeException
	 */
	Map modelConsommation(User user) throws SmartHomeException {
		Map model = [user: user]

		model.house = houseService.findDefaultByUser(user)

		return model
	}


	/**
	 * Modèle affichage Mon Défi
	 * 
	 * @param command
	 * @return
	 * @throws SmartHomeException
	 */
	Map modelMonDefi(DefiCommand command) throws SmartHomeException {
		Map model = [user: command.user, command: command]

		model.defis = defiService.listByUser(command, [max: 5])
		model.currentDefi = command.defi ?: (model.defis ? model.defis[0] : null)

		model.electricite = [:]
		model.gaz = [:]
		model.global = [:]

		if (model.currentDefi) {
			// charge les données elec et construit les graphes
			// on passe dans des transactions séparées car le service appelé peut
			// déclencher une SmartHomeException qui va péter un rollabck alors que
			// lecture seule sur la transaction principale. Même avec un @NotTransaction
			// ca ne marche pas
			try {
				Defi.withTransaction([readOnly: true, propagationBehavior: TransactionDefinition.PROPAGATION_REQUIRES_NEW]) {
					// un seul chargement de données qui est passé ensuite aux graphes
					// pour ne pas devoir charger à chaque graphe
					model.electricite.consos = defiService.loadUserConso(model.currentDefi, model.user,
							DefiCompteurEnum.electricite)

					model.electricite.chartTotal = defiService.chartUserTotal(model.currentDefi,
							model.user, model.electricite.consos)
					model.electricite.chartConso = defiService.chartUserDay(model.currentDefi,
							model.user, model.electricite.consos)
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
					model.gaz.consos = defiService.loadUserConso(model.currentDefi, model.user,
							DefiCompteurEnum.gaz)

					model.gaz.chartTotal = defiService.chartUserTotal(model.currentDefi,
							model.user, model.gaz.consos)
					model.gaz.chartConso = defiService.chartUserDay(model.currentDefi,
							model.user, model.gaz.consos)
				}
			} catch (SmartHomeException ex) {
				model.gaz.error = ex.message
			}

			// passer toutes les consos de chaque compteur dans cette méthode
			// pour calculer les consos globales et créer les chart correspondants
			model.global.consos = defiService.groupConsos(model.currentDefi,
					model.electricite.consos, model.gaz.consos)
			model.global.chartTotal = defiService.chartUserTotal(model.currentDefi,
					model.user, model.global.consos)
			// le graphe détaillé est différent selon la granularité des consos
			// entre les différents compteurs. si un compteur elec est connecté
			// et le connecteur gaz non, alors beaucoup plus de valeurs sur l'élec et
			// les consos gaz seront toutes enregistrées sur une seule date. donc
			// le graphe à la journée ne sera pas représentatif. On passe dans ce
			// cas à un graphe à la semaine
			if (model.global.consos.bestView == "day") {
				model.global.chartConso = defiService.chartUserDay(model.currentDefi,
						model.user, model.global.consos)
			} else {
				model.global.chartConso = defiService.chartUserWeek(model.currentDefi,
						model.user, model.global.consos)
			}
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
		user.roles << Role.findByAuthority('ROLE_GRAND_DEFI').id
		try {
			userService.save(user, true)
		} catch (SmartHomeException ex) {
			// rethrow l'erreur en spécifiant le bon command et les bonnes erreurs
			throw new SmartHomeException(ex.message, account, user)
		}

		// Association à un admin (pour la validation des index)
		String adminIds = configService.value("GRAND_DEFI_ADMIN_IDS")

		if (adminIds) {
			for (String adminId : adminIds.split(",")) {
				userService.save(new UserAdmin(user: user, admin: User.read(adminId.trim() as Long)))
			}
		}

		// création d'une maison par défaut
		try {
			houseService.bindDefault(user, [chauffage: account.chauffage,
				ecs: account.ecs, surface: account.surface,
				location: account.commune.libelle + ", France"])
		} catch (SmartHomeException ex) {
			// rethrow l'erreur en spécifiant le bon command et les bonnes erreurs
			throw new SmartHomeException(ex.message, account)
		}

		// construction des équipes "à la volée" et association avec une équipe
		// et le grand défi en cours
		String grandDefiId = configService.value("GRAND_DEFI_ID")

		if (!grandDefiId) {
			throw new SmartHomeException("Les inscriptions pour le Grand Défi ne sont plus autorisées", account)
		}

		try {
			defiService.inscription(user, grandDefiId as Long, account.commune.libelle)
		} catch (SmartHomeException ex) {
			// rethrow l'erreur en spécifiant le bon command et les bonnes erreurs
			throw new SmartHomeException(ex.message, account)
		}

		return registrationCode
	}

}
