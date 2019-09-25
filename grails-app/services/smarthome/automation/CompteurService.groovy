package smarthome.automation

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.transaction.annotation.Transactional

import smarthome.api.AdictService
import smarthome.api.DataConnectService
import smarthome.application.granddefi.RegisterCompteurCommand
import smarthome.automation.deviceType.CompteurGaz
import smarthome.automation.deviceType.TeleInformation
import smarthome.core.AbstractService
import smarthome.core.SmartHomeException
import smarthome.security.User

/**
 * @author gelleouet <gregory.elleouet@gmail.com>
 *
 */
class CompteurService extends AbstractService {

	private static final String MAC_COMPTEUR_ELEC_MANUEL = "compteur_elec"
	private static final String LABEL_COMPTEUR_ELEC_MANUEL = "Compteur électrique (non connecté)"
	private static final String MAC_COMPTEUR_GAZ_MANUEL = "compteur_gaz"
	private static final String LABEL_COMPTEUR_GAZ_MANUEL = "Compteur gaz (non connecté)"

	HouseService houseService
	DeviceService deviceService
	NotificationAccountService notificationAccountService
	DataConnectService dataConnectService
	GrailsApplication grailsApplication
	AdictService adictService


	/**
	 * Création du modèle pour la gestion des compteurs
	 *
	 * @param model
	 * @return
	 */
	Map modelCompteur(User user) throws SmartHomeException {
		Map model = [user: user]

		model.house = houseService.findDefaultByUser(user)

		// --------------------------------------------------------------------
		// Compteur élec

		// recherche des services associés
		model.dataConnect = notificationAccountService.findByUserAndLibelleSender(user,
				grailsApplication.config.enedis.appName)

		// recherche du compteur connecté associé
		if (model.dataConnect) {
			model.dataConnectDevice = dataConnectService.getDeviceFromConfig(model.dataConnect)
		}

		// recherche des compteurs existants
		model.compteurElecs = deviceService.listByUser(new DeviceSearchCommand(userId: user.id,
		deviceTypeClass: TeleInformation.name))

		// --------------------------------------------------------------------
		// Compteur gaz

		// recherche des services associés
		model.adict = notificationAccountService.findByUserAndLibelleSender(user,
				grailsApplication.config.grdf.appName)

		// recherche du compteur connecté associé
		if (model.adict) {
			model.adictDevice = adictService.getDeviceFromConfig(model.adict)
		}

		// recherche des compteurs existants
		model.compteurGazs = deviceService.listByUser(new DeviceSearchCommand(userId: user.id,
		deviceTypeClass: CompteurGaz.name))

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
			// recherche d'un existant
			compteur = deviceService.findByMac(command.user, MAC_COMPTEUR_ELEC_MANUEL)

			if (!compteur) {
				compteur = new Device(
						user: command.user,
						unite: 'W',
						mac: MAC_COMPTEUR_ELEC_MANUEL,
						label: LABEL_COMPTEUR_ELEC_MANUEL,
						deviceType: DeviceType.findByImplClass(TeleInformation.name))
			}
		}

		// ajout ou update config device
		compteur.addMetadata('modele', [value: command.compteurModel, label: 'Modèle'])

		deviceService.save(compteur)

		// si le compteur est bien créé, on l'associe à la maison principale
		houseService.bindDefault(command.user, [compteur: compteur])
	}


	/**
	 * Enregistrement d'un nouveau compteur non connecté
	 *
	 * @param command
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void registerCompteurGaz(RegisterCompteurCommand command) throws SmartHomeException {
		Device compteur

		if (command.compteurGaz) {
			compteur = command.compteurGaz
		} else {
			// recherche d'un existant
			compteur = deviceService.findByMac(command.user, MAC_COMPTEUR_GAZ_MANUEL)

			if (!compteur) {
				compteur = new Device(
						user: command.user,
						mac: MAC_COMPTEUR_GAZ_MANUEL,
						label: LABEL_COMPTEUR_GAZ_MANUEL,
						deviceType: DeviceType.findByImplClass(CompteurGaz.name))
			}
		}

		// ajout ou update config device
		compteur.addMetadata('modele', [value: command.compteurModel, label: 'Modèle'])

		deviceService.save(compteur)

		// si le compteur est bien créé, on l'associe à la maison principale
		houseService.bindDefault(command.user, [compteurGaz: compteur])
	}


	/**
	 * Reset du compteur élec associé à la maison
	 * 
	 * @param user
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void resetCompteurElec(User user)  throws SmartHomeException {
		houseService.bindDefault(user, [compteur: null])
	}


	/**
	 * Reset du compteur gaz associé à la maison
	 *
	 * @param user
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void resetCompteurGaz(User user)  throws SmartHomeException {
		houseService.bindDefault(user, [compteurGaz: null])
	}
}
