package smarthome.automation

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.transaction.annotation.Transactional

import smarthome.api.DataConnectService
import smarthome.application.granddefi.RegisterCompteurCommand
import smarthome.automation.deviceType.TeleInformation
import smarthome.core.AbstractService
import smarthome.core.SmartHomeException
import smarthome.security.User

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class CompteurService extends AbstractService {

	private static final String LABEL_COMPTEUR_ELEC_MANUEL = "Compteur électrique (non connecté)"

	HouseService houseService
	DeviceService deviceService
	NotificationAccountService notificationAccountService
	DataConnectService dataConnectService
	GrailsApplication grailsApplication


	/**
	 * Création du modèle pour la gestion des compteurs
	 *
	 * @param model
	 * @return
	 */
	Map modelCompteur(User user) throws SmartHomeException {
		Map model = [user: user]

		model.house = houseService.findDefaultByUser(user)

		// recherche des services associés
		model.dataConnect = notificationAccountService.findByUserAndLibelleSender(user,
				grailsApplication.config.enedis.appName)

		// recherche du compteur dataconnect
		if (model.dataConnect) {
			model.dataConnectDevice = dataConnectService.getDeviceFromConfig(model.dataConnect)
		}

		// recherche des compteurs existants
		model.compteurElecs = deviceService.listByUser(new DeviceSearchCommand(userId: user.id,
		deviceTypeClass: TeleInformation.name))

		return model
	}


	/**
	 * Enregistrement d'un nouveau compteur non connecté
	 *
	 * @param command
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void registerCompteurElec(RegisterCompteurCommand command) throws SmartHomeException {
		Device compteur

		if (command.compteurElec) {
			compteur = command.compteurElec
		} else {
			compteur = new Device(
					user: command.user,
					unite: 'W',
					mac: 'compteur_elec',
					label: LABEL_COMPTEUR_ELEC_MANUEL,
					deviceType: DeviceType.findByImplClass(TeleInformation.name))
		}

		// ajout ou update config device
		compteur.addMetadata('modele', [value: command.compteurModel, label: 'Modèle'])

		deviceService.save(compteur)

		// si le compteur est bien créé, on l'associe à la maison principale
		houseService.bindDefault(command.user, [compteur: compteur])
	}


	/**
	 * Reset du compteur associé à la maison
	 * @param user
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void resetCompteurElec(User user)  throws SmartHomeException {
		houseService.bindDefault(user, [compteur: null])
	}
}
