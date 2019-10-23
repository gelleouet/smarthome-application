package smarthome.automation

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.transaction.annotation.Transactional

import smarthome.api.AdictService
import smarthome.api.DataConnectService
import smarthome.application.granddefi.RegisterCompteurCommand
import smarthome.automation.deviceType.Compteur
import smarthome.automation.deviceType.CompteurGaz
import smarthome.automation.deviceType.TeleInformation
import smarthome.core.AbstractService
import smarthome.core.AsynchronousWorkflow
import smarthome.core.SmartHomeException
import smarthome.core.query.HQL
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
	DeviceTypeProviderService deviceTypeProviderService


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

		model.contratElecs = TeleInformation.contrats()
		model.fournisseurElecs = deviceTypeProviderService.listByDeviceTypeImpl(TeleInformation.name)

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

		model.contratGaz = CompteurGaz.contrats()
		model.fournisseurGaz = deviceTypeProviderService.listByDeviceTypeImpl(CompteurGaz.name)

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

		if (command.compteurModel == "_exist") {
			if (!command.compteurElec) {
				throw new SmartHomeException("Veuillez sélectionner un compteur !")
			}

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

				compteur.addMetadata('modele', [value: command.compteurModel, label: 'Modèle'])
				compteur.addMetadata('aggregate', [value: 'sum-conso', label: 'Calcul des données aggrégées'])

				compteur.addMetavalue('opttarif', [label: 'Option tarifaire', value: command.contrat ?: Compteur.DEFAULT_CONTRAT])
				compteur.addMetavalue('baseinst', [unite: 'Wh', label: 'Période consommation', trace: true])
			}
		}

		// ne pas écraser l'ancienne valeur si pas renseigné
		if (command.fournisseur) {
			compteur.addMetadata('fournisseur', [value: command.fournisseur, label: 'Fournisseur'])
		}

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

		if (command.compteurModel == "_exist") {
			if (!command.compteurGaz) {
				throw new SmartHomeException("Veuillez sélectionner un compteur !")
			}

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

				compteur.addMetadata('modele', [value: command.compteurModel, label: 'Modèle'])
				compteur.addMetadata('coefConversion', [label: 'Coefficient conversion'])

				compteur.addMetavalue('conso', [unite: 'Wh', label: 'Période consommation', trace: true])
			}
		}

		// ne pas écraser l'ancienne valeur si pas renseigné
		if (command.fournisseur) {
			compteur.addMetadata('fournisseur', [value: command.fournisseur, label: 'Fournisseur'])
		}

		// ajout ou update config device
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


	/**
	 * Enregistrement d'un index pour validation par admin
	 * L'index n'est pas enregistré sur le device mais dans une table TMP
	 * pour qu'un admin le valide
	 * Un seul index temporaire par user à la fois
	 * 
	 * @param command
	 * @throws SmartHomeException
	 */
	@AsynchronousWorkflow("compteurService.saveIndexForValidation")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	CompteurIndex saveIndexForValidation(SaisieIndexCommand command)  throws SmartHomeException {
		command.asserts()

		Device device = Device.read(command.deviceId)

		// le nouvel index doit aussi être postérieur au dernier relevé
		if (device.dateValue && command.dateIndex <= device.dateValue) {
			throw new SmartHomeException("Le nouvel index est antérieur au dernier relevé du compteur !", command)
		}

		// quit si au moins un index est trouvé pour le user
		//		if (countCompteurIndexForDevice(device)) {
		//			throw new SmartHomeException("Un index en attente de validation existe déjà pour ce compteur !")
		//		}

		// construction d'un objet Index
		CompteurIndex index = new CompteurIndex(device: device,
		dateIndex: command.dateIndex,
		index1: command.index1, index2: command.index2,
		param1: command.param1, photo: command.photo)

		return super.save(index)
	}


	/**
	 * Validation d'un index
	 * Transfert vers les consos du device
	 * 
	 * @param compteurIndex
	 * @throws SmartHomeException
	 */
	@AsynchronousWorkflow("compteurService.validIndex")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void validIndex(CompteurIndex compteurIndex) throws SmartHomeException {
		Device device = compteurIndex.device

		// contrôles intégrité donnée
		if (device.dateValue && compteurIndex.dateIndex <= device.dateValue) {
			throw new SmartHomeException("Le nouvel index est antérieur au dernier relevé du compteur !", compteurIndex)
		}

		// mise à jour des champs communs
		device.dateValue = compteurIndex.dateIndex

		// on passe les données du compteur dans l'impl associée pour mettre à
		// jour les bonnes données
		try {
			Compteur compteurImpl = device.newDeviceImpl()
			compteurImpl.parseIndex(compteurIndex)
		} catch (SmartHomeException ex) {
			// on recatche l'erreur pour passer l'objet command
			throw new SmartHomeException(ex.message, compteurIndex)
		}

		deviceService.saveAndTriggerChange(device)

		// si tout s'est bien passé, on peut supprimer cette saisie
		compteurIndex.delete()
	}


	/**
	 * Nombre d'index à valider pour un device
	 * 
	 * @param device
	 * @return
	 */
	long countCompteurIndexForDevice(Device device) {
		return CompteurIndex.createCriteria().get {
			eq 'device.id', device.id
			projections {
				count('id')
			}
		}
	}

	/**
	 * Nombre d'index à valider pour un user
	 *
	 * @param user
	 * @return
	 */
	long countCompteurIndexForUser(User user) {
		return CompteurIndex.createCriteria().get {
			device {
				eq 'user.id', user.id
			}
			projections {
				count('id')
			}
		}
	}


	/**
	 * Liste les index en cours de validation par un admin
	 * 
	 * @param command
	 * @param pagination
	 * @return
	 */
	List<CompteurIndex> listCompteurIndex(CompteurIndexCommand command, Map pagination) {
		HQL hql = new HQL("compteurIndex",	""" 
			FROM CompteurIndex compteurIndex
			JOIN FETCH compteurIndex.device device
			JOIN FETCH device.deviceType deviceType
			JOIN FETCH device.user user
			LEFT JOIN FETCH user.profil profil""")

		hql.addCriterion("""user.id in (select userAdmin.user.id from UserAdmin userAdmin
			where userAdmin.admin.id = :adminId)""", [adminId: command.admin.id])

		if (command.deviceType) {
			hql.addCriterion("deviceType.id = :deviceTypeId", [deviceTypeId: command.deviceType.id])
		}

		if (command.profil) {
			hql.addCriterion("user.profil.id = :profilId", [profilId: command.profil.id])
		}

		hql.addOrder("compteurIndex.dateIndex")
		hql.addOrder("compteurIndex.id")

		return CompteurIndex.withSession { session ->
			hql.list(session, pagination)
		}
	}
}
