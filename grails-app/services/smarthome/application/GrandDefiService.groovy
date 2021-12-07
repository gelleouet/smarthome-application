/**
 * 
 */
package smarthome.application

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.transaction.annotation.Transactional
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import smarthome.application.granddefi.AccountCommand
import smarthome.application.granddefi.model.DefiModel
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
			model.defiModel = model.currentDefi.newModeleImpl()
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
		// le type est dupliqué dans la key consos car c'est elle qui est passé aux fonctions de graphe
		// et le type est nécessaire
		model.electricite = [type: DefiCompteurEnum.electricite, consos: [:]]
		model.gaz = [type: DefiCompteurEnum.gaz, consos: [:]]
		model.eau = [type: DefiCompteurEnum.eau, consos: [:]]
		model.energie = [type: DefiCompteurEnum.energie, consos: [:]]
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
		model.defiModel?.modelParticipants(command, model)
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
		model.defiModel?.modelResultatsDefi(command, model)
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
		model.defiModel?.modelResultatsEquipe(command, model)
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
		model.defiModel?.modelMesResultats(command, model)
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
		RegistrationCode registrationCode

		if (!user ) {
			// création d'un code d'enregistrement si le compte est bloqué par défaut
			if (account.defaultAccountLocked) {
				registrationCode = new RegistrationCode(username: account.username)
				registrationCode.serverUrl = grailsLinkGenerator.link(controller: 'register', action: 'confirmAccount',
					params: [username: account.username, token: registrationCode.token], absolute: true)
		
				if (!registrationCode.save()) {
					throw new SmartHomeException("Erreur création d'un token d'activation !", account)
				}
			}
	
			// création d'un user avec compte bloqué en attente déblocage
			user = new User(username: account.username, password: account.newPassword, prenom: account.prenom,
				nom: account.nom, lastActivation: new Date(), accountLocked: account.defaultAccountLocked,
				applicationKey: UUID.randomUUID(), profilPublic: account.profilPublic,
				profil: account.profil, telephoneMobile: account.telephone,
				autorise_user_data: account.autorise_user_data, autorise_conso_data: account.autorise_conso_data,
				autorise_share_data: account.autorise_share_data, engage_enedis_account: account.engage_enedis_account)
			
			try {
				userService.save(user, false)
			} catch (SmartHomeException ex) {
				// rethrow l'erreur en spécifiant le bon command et les bonnes erreurs
				throw new SmartHomeException(ex.message, account, user)
			}
		} else {
			if (account.checkUserExist) {
				throw new SmartHomeException("Un compte existe déjà avec cette adresse !", account)
			}
		}
		
		// inscription au défi en cours
		String grandDefiId = configService.value(Config.GRAND_DEFI_ID)

		if (!grandDefiId) {
			throw new SmartHomeException("Les inscriptions pour le Grand Défi ne sont plus autorisées", account)
		}
		
		try {
			inscriptionDefi(user, grandDefiId as Long, [chauffage: account.chauffage,
				ecs: account.ecs, surface: account.surface, location: account.commune.libelle,
				adresse: account.adresse, codePostal: account.codePostal, chauffageSecondaire: account.chauffageSecondaire,
				nbPersonne: account.nbPersonne])
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
