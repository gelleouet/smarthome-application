/**
 * 
 */
package smarthome.application

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.transaction.annotation.Transactional

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
