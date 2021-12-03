package smarthome.automation

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.transaction.annotation.Transactional

import smarthome.api.AdictService
import smarthome.api.DataConnectService
import smarthome.application.granddefi.RegisterCompteurCommand
import smarthome.automation.deviceType.Compteur
import smarthome.automation.deviceType.CompteurEau
import smarthome.automation.deviceType.CompteurGaz
import smarthome.automation.deviceType.TeleInformation
import smarthome.core.AbstractService
import smarthome.core.AsynchronousWorkflow
import smarthome.core.DateUtils
import smarthome.core.QueryUtils
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
	private static final String MAC_COMPTEUR_EAU_MANUEL = "compteur_eau"
	private static final String LABEL_COMPTEUR_EAU_MANUEL = "Compteur eau (non connecté)"

	HouseService houseService
	DeviceService deviceService
	DeviceUtilService deviceUtilService
	DeviceValueService deviceValueService
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
		
		// --------------------------------------------------------------------
		// Compteur eau

		model.fournisseurEau = deviceTypeProviderService.listByDeviceTypeImpl(CompteurEau.name)

		// recherche des compteurs existants
		model.compteurEaux = deviceService.listByUser(new DeviceSearchCommand(userId: user.id,
			deviceTypeClass: CompteurEau.name))

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
				compteur.addMetadata('fournisseur', [value: Compteur.DEFAULT_FOURNISSEUR, label: 'Fournisseur'])

				compteur.addMetavalue('opttarif', [value: Compteur.DEFAULT_CONTRAT, label: 'Option tarifaire'])
				compteur.addMetavalue('baseinst', [unite: 'Wh', label: 'Période consommation', trace: true])
			}
		}

		// ne pas écraser l'ancienne valeur si pas renseigné
		if (command.contrat) {
			compteur.addMetavalue('opttarif', [value: command.contrat, label: 'Option tarifaire'])
		}
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
	
	
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void registerCompteurEau(RegisterCompteurCommand command) throws SmartHomeException {
		Device compteur

		if (command.compteurModel == "_exist") {
			if (!command.compteurEau) {
				throw new SmartHomeException("Veuillez sélectionner un compteur !")
			}

			compteur = command.compteurEau
		} else {
			// recherche d'un existant
			compteur = deviceService.findByMac(command.user, MAC_COMPTEUR_EAU_MANUEL)

			if (!compteur) {
				compteur = new Device(
						user: command.user,
						mac: MAC_COMPTEUR_EAU_MANUEL,
						label: LABEL_COMPTEUR_EAU_MANUEL,
						deviceType: DeviceType.findByImplClass(CompteurEau.name))

				compteur.addMetavalue('conso', [unite: 'L', label: 'Période consommation', trace: true])
			}
		}

		// ne pas écraser l'ancienne valeur si pas renseigné
		if (command.fournisseur) {
			compteur.addMetadata('fournisseur', [value: command.fournisseur, label: 'Fournisseur'])
		}

		// ajout ou update config device
		deviceService.save(compteur)

		// si le compteur est bien créé, on l'associe à la maison principale
		houseService.bindDefault(command.user, [compteurEau: compteur])
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
	 * Reset du compteur eau associé à la maison
	 *
	 * @param user
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void resetCompteurEau(User user)  throws SmartHomeException {
		houseService.bindDefault(user, [compteurEau: null])
	}


	/**
	 * Enregistrement d'un index pour validation par admin
	 * L'index n'est pas enregistré sur le device mais dans une table TMP
	 * pour qu'un admin le valide
	 * 
	 * @param command
	 * @throws SmartHomeException
	 */
	@AsynchronousWorkflow("compteurService.saveIndexForValidation")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	CompteurIndex saveIndexForValidation(CompteurIndex command)  throws SmartHomeException {
		// transformation et controle par le compteur lui-meme avant les controles généraux
		// car l'impl peut calculer de nouveaux index à partir des valeurs saisies
		(command.device.newDeviceImpl() as Compteur).bindCompteurIndex(command)
		command.asserts()
		
		return super.save(command)
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
		Compteur compteurImpl = device.newDeviceImpl()

		// on délègue le boulot à l'impl car elle seule connait l'organisation
		// et le calcul des consos
		try {
			compteurImpl.bindCompteurIndex(compteurIndex)
			compteurIndex.asserts()
			compteurImpl.parseIndex(compteurIndex)
		} catch (SmartHomeException ex) {
			// on recatche l'erreur pour passer l'objet command
			throw new SmartHomeException(ex.message, compteurIndex)
		}

		// mise à jour des champs communs
		// IMPORTANT : faire après le parseIndex qui cherche les derniers index
		// et qui peut se base sur la date value précédente du device.
		// surtout quand elle est nulle (pas de valeur), on dirait que Postgtesql
		// est obligé de scanner une bonne partie de la base pour essayer de trouver
		// une valeur qui n'existe pas
		device.dateValue = compteurIndex.dateIndex
		deviceService.saveAndTriggerChange(device)

		// mise à jour des index voisins car il n'y pas d'ordre d'ajout d'index
		// donc si insertion d'un index entre 2 autres, il faut recalculer les consos
		// des index antérieurs et postérieurs
		// A faire APRES la mise à jour du device pour persister les nouvelles metavalues
		try {
			Collection siblingValues = compteurImpl.refactoringNextIndex(compteurIndex)
			deviceValueService.saveAll(siblingValues)
			deviceUtilService.aggregateValues(siblingValues)
		} catch (SmartHomeException ex) {
			// on recatche l'erreur pour passer l'objet command
			throw new SmartHomeException(ex.message, compteurIndex)
		}
		
		// si tout s'est bien passé, on peut supprimer cette saisie
		compteurIndex.delete()
	}
	
	
	/**
	 * Modification d'un index existant.
	 * Les consos associées doivent être modifiées ainsi que celle de l'index juste après
	 * 
	 * @param compteurIndex
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void updateIndex(DeviceValue deviceValue) throws SmartHomeException {
		Device device = deviceValue.device
		Compteur compteurImpl = device.newDeviceImpl()

		// on délègue le boulot à l'impl car elle seule connait l'organisation
		// et le calcul des consos
		try {
			// recalcul des consos aggrégées sur toutes les dates qui ont changé
			Collection siblingValues = compteurImpl.updateIndex(deviceValue)
			deviceValueService.saveAll(siblingValues)
			deviceUtilService.aggregateValues(siblingValues)
		} catch (SmartHomeException ex) {
			// on recatche l'erreur pour passer l'objet command
			throw new SmartHomeException(ex.message, deviceValue)
		}
	}
	
	
	/**
	 * Suppression d'un index
	 * Les consos associées doivent êre répercutées sur l'index suivant s'il existe
	 * On fait un recalcul avec index suivant et index antérieur et non pas une simple
	 * addition de la conso actuelle pour être sur d'avoir des valeurs cohérentes
	 * 
	 * @param deviceValue
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void deleteIndex(DeviceValue deviceValue) throws SmartHomeException {
		Device device = deviceValue.device
		Compteur compteurImpl = device.newDeviceImpl()

		// on délègue le boulot à l'impl car elle seule connait l'organisation
		// et le calcul des consos
		try {
			// recalcul des consos aggrégées sur toutes les dates qui ont changé
			Map siblingValues = compteurImpl.deleteIndex(deviceValue)
			deviceValueService.saveAll(siblingValues.persist)
			deviceUtilService.aggregateValues(siblingValues.aggregate)
		} catch (SmartHomeException ex) {
			// on recatche l'erreur pour passer l'objet command
			throw new SmartHomeException(ex.message, deviceValue)
		}
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
	 * Liste paginée des index en cours de validation par un admin
	 * 
	 * @param command
	 * @return
	 */
	List<CompteurIndex> listCompteurIndex(CompteurIndexCommand command) {
		HQL hql = new HQL("compteurIndex",	""" 
			FROM CompteurIndex compteurIndex
			JOIN FETCH compteurIndex.device device
			JOIN FETCH device.deviceType deviceType
			JOIN FETCH device.user user
			LEFT JOIN FETCH user.profil profil""")

		hql.addCriterion("""user.id in (select userAdmin.user.id from UserAdmin userAdmin
			where userAdmin.admin.id = :adminId)""", [adminId: command.adminId])
		
		if (command.userSearch) {
			hql.addCriterion("lower(user.username) like :userSearch or lower(user.prenom) like :userSearch or lower(user.nom) like :userSearch",
				[userSearch: QueryUtils.decorateMatchAll(command.userSearch.toLowerCase())])
		}

		if (command.deviceTypeId) {
			hql.addCriterion("deviceType.id = :deviceTypeId", [deviceTypeId: command.deviceTypeId])
		}

		if (command.profilId) {
			hql.addCriterion("user.profil.id = :profilId", [profilId: command.profilId])
		}

		command.addOrder(hql)

		return CompteurIndex.withSession { session ->
			hql.list(session, command.pagination())
		}
	}
}
