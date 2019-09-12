/**
 * 
 */
package smarthome.application

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.springframework.transaction.annotation.Transactional;

import smarthome.api.DataConnectService;
import smarthome.application.granddefi.RegisterCompteurCommand;
import smarthome.automation.Device;
import smarthome.automation.DeviceService;
import smarthome.automation.DeviceType;
import smarthome.automation.HouseService;
import smarthome.automation.NotificationAccountService;
import smarthome.automation.deviceType.TeleInformation;
import smarthome.core.AbstractService;
import smarthome.core.SmartHomeException;
import smarthome.security.User;

/**
 * @author gregory.elleouet@gmail.com<Grégory Elléoouet>
 *
 */
class GrandDefiService extends AbstractService {

	private static final String LABEL_COMPTEUR_ELEC_MANUEL = "Compteur électrique (non connecté)"
	
	HouseService houseService
	DeviceService deviceService
	NotificationAccountService notificationAccountService
	DataConnectService dataConnectService
	GrailsApplication grailsApplication
	
	
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
		
		// recherche du compteur connecté
		if (model.dataConnect) {
			model.dataConnectDevice = dataConnectService.getDeviceFromConfig(model.dataConnect)
		}
		
		// si pas de compteur connecté, recherche du compteur non connecté
		if (!model.dataConnectDevice) {
			model.compteurElecDevice = deviceService.findByLabel(user, LABEL_COMPTEUR_ELEC_MANUEL)
		}
		
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
		Device compteur = new Device(
			user: command.user,
			unite: 'W',
			mac: 'compteur_elec',
			label: LABEL_COMPTEUR_ELEC_MANUEL,
			deviceType: DeviceType.findByImplClass(TeleInformation.name))
		
		// ajout ou update config device
		compteur.addMetadata('modele', [value: command.compteurModel, label: 'Modèle'])
		
		deviceService.save(compteur)
	}
}


