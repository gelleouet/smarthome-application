package smarthome.automation

import java.io.Serializable
import java.util.List

import grails.async.PromiseList
import grails.converters.JSON
import grails.plugin.cache.CachePut
import grails.plugin.cache.Cacheable
import groovy.time.TimeCategory
import groovy.time.TimeDuration

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional

import smarthome.automation.deviceType.AbstractDeviceType
import smarthome.automation.scheduler.SmarthomeScheduler
import smarthome.automation.scheduler.WorkflowContextJob
import smarthome.core.AbstractService
import smarthome.core.AsynchronousWorkflow
import smarthome.core.Chronometre
import smarthome.core.DateUtils
import smarthome.core.ExchangeType
import smarthome.core.QueryUtils
import smarthome.core.ScriptUtils
import smarthome.core.SmartHomeException
import smarthome.core.TransactionUtils
import smarthome.core.WorkflowService
import smarthome.core.chart.GoogleChart
import smarthome.core.query.HQL
import smarthome.rule.DeviceTypeDetectRuleService
import smarthome.security.User
import smarthome.security.UserAdmin


class DeviceService extends AbstractService {

	static final String CHANGE_VALUE_WORKFLOW = "deviceService.changeValue"

	AgentService agentService
	DeviceTypeDetectRuleService deviceTypeDetectRuleService
	WorkflowService workflowService
	SmarthomeScheduler smarthomeScheduler
	DevicePlanningService devicePlanningService
	PlanningService planningService


	/**
	 * Enregistrement d'un device sans les contrôles d'autorisation
	 * A utiliser dans les contextes hors request (ie jobs)
	 * 
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Device saveWithoutAuthorize(Device device) throws SmartHomeException {
		// on peut passer par le save classique car les annotations ne sont
		// visibles depuis les appels internes (aop)
		return this.save(device)
	}


	/**
	 * Enregistrement d"un device
	 * 
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@PreAuthorize("hasPermission(#device, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Device save(Device device) throws SmartHomeException {
		if (!device.save()) {
			throw new SmartHomeException("Erreur enregistrement device !", device)
		}

		return device
	}


	/**
	 * Enregistrement du device et déclenchement process pour changement de valeur
	 * 
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@AsynchronousWorkflow(DeviceService.CHANGE_VALUE_WORKFLOW)
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Device saveAndTriggerChange(Device device) throws SmartHomeException {
		return this.save(device)
	}


	/**
	 * Enregistrement d"un device avec toutes les associations bindées
	 * 
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@PreAuthorize("hasPermission(#device, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Device saveWithAssociations(Device device) throws SmartHomeException {
		device.clearNotBindingLevelAlert()
		device.clearNotBindingPlanning()

		device.devicePlannings.each { devicePlanning ->
			// association forcée sur le user du device
			// et on revalide l'objet pour supprimer l'erreur sur la propriété user qui était nulle
			devicePlanning.planning.user = device.user
			devicePlanning.planning.validate()
			planningService.save(devicePlanning.planning)

			devicePlanning.save()
		}

		return this.save(device)
	}


	/**
	 * Edition d'un device
	 * 
	 * @param device
	 * @return
	 */
	@PreAuthorize("hasPermission(#device, 'OWNER')")
	Device edit(Device device) {
		return device
	}


	/**
	 * Vérifie si l'utilisateur a un accès public sur le device
	 * 
	 * @param device
	 * @return
	 */
	Device assertSharedAccess(Device device, User user) throws SmartHomeException {
		// si l'utilisateur est le proprio => ok
		if (device.user.id == user.id) {
			return device
		}

		// si l'utilisateur a un accès partagé
		def share = DeviceShare.createCriteria().get {
			eq 'device', device
			eq 'sharedUser', user
		}

		if (share) {
			return device
		}

		// si le user est admin du device
		def admin = UserAdmin.findByAdminAndUser(user, device.user)

		if (admin) {
			return device
		}

		throw new SmartHomeException("Accès refusé !")
	}


	/**
	 * Vérifie si l'utilisateur est le proprio du device
	 * 
	 * @param device
	 * @param userId
	 * @return
	 * @throws SmartHomeException
	 */
	Device assertOwnerAccess(long deviceId, long userId) throws SmartHomeException {
		Device device = Device.read(deviceId)

		if (device?.user?.id != userId) {
			throw new SmartHomeException("Accès refusé !")
		}

		return device
	}


	/**
	 * Changement d'une métadata et envoit à l'agent
	 * 
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@PreAuthorize("hasPermission(#device, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def syncMetadata(Device device, String metadataName) throws SmartHomeException {
		this.save(device)

		if (device.agent) {
			if (device.agent.online) {
				def data = [header: 'config', deviceMac: device.mac, metadataName: metadataName,
					metadataValue: device.metadata(metadataName)?.value]
				agentService.sendMessage(device.agent, data)
			} else {
				throw new SmartHomeException("Agent not connected !", device)
			}
		}

		return device
	}


	/**
	 * Synchronise les plannings vers l'agent
	 *
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@PreAuthorize("hasPermission(#device, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void syncPlannings(Device device) throws SmartHomeException {
		if (device.agent) {
			if (device.agent.online) {
				def data = []

				List<DevicePlanning> devicePlannings = devicePlanningService.listByDevice(device)

				devicePlannings.each { devicePlanning ->
					devicePlanning.planning.loadJsonData()
					data << [title: devicePlanning.planning.title, rule: devicePlanning.planning.rule,
						data: devicePlanning.planning.jsonData]
				}

				def message = [header: 'config', deviceMac: device.mac, metadataName: '_plannings',
					metadataValue: data]
				agentService.sendMessage(device.agent, message)
			} else {
				throw new SmartHomeException("Agent not connected !", device)
			}
		}
	}


	/**
	 * Suppression d'un device avec ses associations
	 * 
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@PreAuthorize("hasPermission(#device, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def delete(Device device) throws SmartHomeException {
		// IMPORTANT : suppression des associations en batch sinon une requete delete par value (trop long)
		DeviceValue.executeUpdate("DELETE FROM DeviceValue deviceValue WHERE deviceValue.device = :device", [device: device])
		DeviceValueDay.executeUpdate("DELETE FROM DeviceValueDay deviceValue WHERE deviceValue.device = :device", [device: device])
		DeviceValueMonth.executeUpdate("DELETE FROM DeviceValueMonth deviceValue WHERE deviceValue.device = :device", [device: device])
		DeviceMetadata.executeUpdate("DELETE FROM DeviceMetadata deviceMeta WHERE deviceMeta.device = :device", [device: device])
		DeviceMetavalue.executeUpdate("DELETE FROM DeviceMetavalue deviceMetaValue WHERE deviceMetaValue.device = :device", [device: device])
		DeviceShare.executeUpdate("DELETE FROM DeviceShare deviceShare WHERE deviceShare.device = :device", [device: device])
		DeviceLevelAlert.executeUpdate("DELETE FROM DeviceLevelAlert deviceAlert WHERE deviceAlert.device = :device", [device: device])
		EventDevice.executeUpdate("DELETE FROM EventDevice deviceEvent WHERE deviceEvent.device = :device", [device: device])
		EventDevice.executeUpdate("DELETE FROM ChartDevice deviceChart WHERE deviceChart.device = :device", [device: device])
		Device.executeUpdate("DELETE FROM Device device WHERE device = :device", [device: device])
		return device
	}


	/**
	 * 
	 * @param agent
	 * @param datas
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousWorkflow(DeviceService.CHANGE_VALUE_WORKFLOW)
	Device changeValueFromAgent(Agent agent, def datas) throws SmartHomeException {
		if (!datas.mac) {
			throw new SmartHomeException("Mac is empty !")
		}

		if (datas.value == null) {
			throw new SmartHomeException("Value is empty !")
		}

		boolean offline = datas.offline ? true : false

		if (offline) {
			log.info "change [offline] value ${datas.mac} : ${datas.value}"
		} else {
			log.info "change value ${datas.mac} : ${datas.value}"
		}

		String implClass = deviceTypeDetectRuleService.execute(datas, true)

		def virtualMetas = []
		def fetchAgent = Agent.read(agent.id)
		Device device = findOrCreateDevice(fetchAgent, datas.mac, datas.label, implClass)

		// la création de nouveaux device n'est possible qu'en mode association
		// donc ce mode n'est pas activé, on refuse les nouveaux
		if (!device.id) {
			// compatiblité avec l'ancienne version Raspberry qui n'envoit pas l'info association
			if (datas.association != null && !datas.association) {
				log.warn("Refuse device ${datas.mac} : no association mode")
				return null
			}
		}

		def resultDevice = null

		// bien metre à jour la date avant toutes les autres instructions
		Date dateValue = DateUtils.parseJson(datas.dateValue, datas.timezoneOffset)

		// bind des métavalues
		datas.metavalues?.each { key, values ->
			def meta = device.addMetavalue(key, values)

			// on tri les metas pour savoir si on doit créer des devices virtuels
			if (meta.virtualDevice) {
				virtualMetas << meta
			}
		}

		// bind des metadatas
		datas.metadatas?.each { key, values ->
			device.addMetadata(key, values)
		}

		// gestion des devices virtuels associés aux metas virtuels
		processVirtualMetas(device, virtualMetas, dateValue, offline)

		// si toutes les valeurs envoyées dans metavalue sont des  virtuelMeta
		// alors on ne touche pas au device principal mais on met à jour seulement les devices virtuels
		if (! (datas.metavalues?.size() == virtualMetas.size() && virtualMetas)) {
			device.value = datas.value
			device.dateValue = dateValue
			device.processValue(datas)
			resultDevice = device
		}

		// dans tous les cas faut enregistrer car il faut quand même enregistrer les valeurs des meta
		// même si elles sont virtuelles
		this.save(device)

		// retourner null désactive le déclenchement du workflow avec historisation et trigger
		return resultDevice
	}


	/**
	 * Recherche ou création d'un device
	 * Si le device est trouvé, un verrou (en base) est actionné sur l'objet pour éviter des accès
	 * concurrents
	 */
	private Device findOrCreateDevice(Agent agent, String mac, String label, String implClass) throws SmartHomeException {
		// ajout d'un verrou pessimiste car erreur quand remontée infos depuis agent
		def device = Device.findByMacAndAgent(mac, agent, [lock: true])

		// on tente de le créer auto si on a toutes les infos
		if (!device) {
			def deviceType = DeviceType.findByImplClass(implClass)

			if (!deviceType) {
				throw new SmartHomeException("Type device (implClass) is empty or not found : ${implClass}")
			}

			device = new Device(agent: agent, user: agent.user, mac: mac,
			label: label ?: mac, deviceType: deviceType)
		}

		return device
	}


	/**
	 * Gestion des metas virtuelles. On doit créer un device associé à la méta
	 * avec sa propre valeur
	 * 
	 */
	private void processVirtualMetas(Device device, List metas, Date dateValue, boolean offline) throws SmartHomeException {
		metas?.each {
			def virtualDevice = findOrCreateDevice(device.agent, "${device.mac}-${it.name}",
					"${it.label}  -> ${device.label}",
					device.deviceType.implClass)

			virtualDevice.value = it.value
			virtualDevice.dateValue = dateValue
			virtualDevice.processValue()

			this.save(virtualDevice)

			// Exécute le workflow dédié au changement de valeur
			// dans les params du workflow, reprennent les mêmes paramètres que la fonction principale
			// ie changeValueFromAgent(agent, datas)
			workflowService.asyncExecute(CHANGE_VALUE_WORKFLOW, [result: virtualDevice,
				arg0: device.agent, arg1: [offline: offline]])
		}
	}


	/**
	 * Exécute une action sur le device. Les actions dépendent de l'implémentation du type device.
	 * 
	 * @param device
	 * @param actionName
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	@AsynchronousWorkflow(DeviceService.CHANGE_VALUE_WORKFLOW)
	Device execute(Device device, String actionName, Map actionParameters) throws SmartHomeException {
		if (!device.attached) {
			device.attach()
		}

		WorkflowContext context = new WorkflowContext(parameters: actionParameters,
		device: device, actionName: actionName, dateAction: new Date())

		WorkflowContext delayContext = context.withDelay()

		// le contexte doit être différé. On replanifie l'exécution à la bonne date
		// et on interromp le process
		if (delayContext) {
			smarthomeScheduler.scheduleOneShotJob(WorkflowContextJob, delayContext.dateAction,
					WorkflowContextJob.convertJobParams(delayContext))
			return null
		}

		// instancie le type device pour exécuter l'action
		def deviceImpl = device.newDeviceImpl()
		device.fetchParams()
		Object result

		try {
			result = deviceImpl."${actionName}"(context)
			log.info "Invoke ${deviceImpl.class.name}.${actionName} [${device.label}]"
		} catch (Exception e) {
			throw new SmartHomeException("Can't invoke ${deviceImpl.class.name}.${actionName} [${device.label}]")
		}

		// le device a déclenché un timer pour une autre exécution
		if (result instanceof WorkflowContext) {
			smarthomeScheduler.scheduleOneShotJob(WorkflowContextJob, result.dateAction,
					WorkflowContextJob.convertJobParams(result))
		}

		device.dateValue = new Date()
		device.actionName = actionName

		return this.save(device)
	}


	/**
	 * Liste les devices d'un user pour une application tierce (Ex : google home, alexa, etc.)
	 * Pour apparaitre dans la liste, le device doit être dispo sur un tableau de bord ou favori (visible déjà 
	 * dans l'application actuelle) et avoir une config spéciale au niveau de l'implémentation du device
	 * 
	 * @param user
	 * @param applicationName
	 * @return une liste de Map contenant un élément device et config
	 * 
	 * @throws SmartHomeException
	 */
	List<Map> listForApplication(User user, String applicationName) throws SmartHomeException {
		// 1ere recherche des devices avec soit tableau bord, soit favori
		List<Device> devices = Device.createCriteria().list {
			eq 'user', user
			or {
				isNotNull 'tableauBord'
				eq 'favori', true
			}
			join 'deviceType'
			order 'label'
		}

		// Filtre : ne prend que les devices qui ont une config liée à l'application
		Map<Long, DeviceTypeConfig> configs = [:]
		List<Map> results = []

		devices.each { Device device ->
			DeviceTypeConfig config = configs[device.deviceType.id]

			if (!config) {
				config = device.deviceType.config() ?: new DeviceTypeConfig()
				config.loadJsonData()
				configs[device.deviceType.id] = config
			}

			if (config.jsonData[applicationName]) {
				results << [device: device, config: config]
			}
		}

		return results
	}


	/**
	 * Liste les 
	 * @param command
	 * @return
	 */
	List listSupervision (SupervisionCommand command) throws SmartHomeException {
		if (!command.adminId) {
			throw new SmartHomeException("adminId must be fill !", command)
		}

		HQL hql = new HQL("device",	""" 
			FROM Device device 
			JOIN FETCH device.deviceType deviceType
			JOIN FETCH device.user user
			LEFT JOIN FETCH user.profil profil""")

		//hql.addCriterion("""user.id in (select userAdmin.user.id from UserAdmin userAdmin
		//	where userAdmin.admin.id = :adminId)""", [adminId: command.adminId])
			
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

		hql.addOrder("profil.libelle")
		hql.addOrder("user.nom")
		hql.addOrder("user.prenom")
		hql.addOrder("device.label")

		return Device.withSession { session ->
			hql.list(session, command.pagination())
		}
	}


	/**
	 * Liste les devices d'un user
	 * 
	 * @param pagination
	 * @return
	 * @throws SmartHomeException
	 */
	def listByUser(DeviceSearchCommand command) throws SmartHomeException {
		Chronometre chrono = new Chronometre()

		def search = QueryUtils.decorateMatchAll(command.search)

		if (!command.userId) {
			throw new SmartHomeException("userId must be fill !", command)
		}

		def commonCriteria = {
			user {
				idEq(command.userId)
			}

			if (command.search) {
				ilike 'label', search
			}

			if (command.tableauBord) {
				eq 'tableauBord', command.tableauBord
			}

			if (command.favori) {
				eq 'favori', command.favori
			}

			if (command.deviceTypeClass) {
				deviceType {
					eq 'implClass', command.deviceTypeClass
				}
			}
			if (command.userSharedId) {
				shares {
					eq 'sharedUser.id', command.userSharedId
				}
			}

			join "deviceType"
			join "user"

			order "label"
		}

		def devices

		// si on doit tout charger, on en profite pour charger les meta datas et values
		if (!command.pagination) {
			devices = Device.createCriteria().listDistinct() {
				commonCriteria.delegate = delegate
				commonCriteria()
				join 'shares'
			}
		} else {
			devices = Device.createCriteria().list(command.pagination) {
				commonCriteria.delegate = delegate
				commonCriteria()
			}
		}

		log.info "List ${devices?.size()} devices : ${chrono.stop()}ms"

		return devices
	}


	/**
	 * Utile pour les environnements sans session hibernate automatique
	 * Ex : Camel ESB
	 *
	 * @param id
	 * @return
	 */
	Device findById(Serializable id) {
		return Device.createCriteria().get {
			idEq id as Long
			join 'deviceType'
		}
	}


	/**
	 * Recherche d'un objet par son nom si celui-ci a été par convention (nommage automatique
	 * par le système). C'est le seul cas car ce champ n'est pas unique par user
	 * 
	 * Déclenche une erreur si plusieurs devices sont trouvés
	 * 
	 * @param user
	 * @param label
	 * @return
	 */
	Device findByLabel(User user, String label) throws SmartHomeException {
		List devices = Device.createCriteria().list {
			eq 'label', label
			eq 'user', user
			join 'deviceType'
		}

		if (devices.size() > 1) {
			throw new SmartHomeException("Objet ${label} en doublon !")
		}

		return devices ? devices[0] : null
	}


	/**
	 * Recherche d'un objet par son identifiant mac
	 *
	 * @param user
	 * @param label
	 * @return
	 */
	Device findByMac(User user, String mac) throws SmartHomeException {
		return Device.createCriteria().get {
			eq 'mac', mac
			eq 'user', user
			join 'deviceType'
		}
	}


	/**
	 * Construit le message à envoyer à un agent pour exécuter une action
	 * 
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	Map createMessage(Device device, String actionName) throws SmartHomeException {
		if (!device.attached) {
			device.attach()
		}

		// chargement des associations pour eviter erreur à conversion json
		// @see static Device JSON.registerObjectMarshaller
		def deviceType = device.deviceType
		//device.metadatas = DeviceMetadata.findAllByDevice(device)
		device.metadatas.size()

		return [header: 'invokeAction', action: actionName,
			device: device]
	}


	/**
	 * Les objets partagés d'un utilisateur
	 * 
	 * @param user
	 * @return
	 */
	List<Long> listSharedDeviceId(Long userId) {
		return DeviceShare.createCriteria().list {
			sharedUser {
				idEq userId
			}
			projections {
				property 'device.id'
			}
		}
	}


	/**
	 * Nombre de devices d'un user
	 * 
	 * @param user
	 * @return
	 */
	long countDevice(User user) {
		return Device.where({ user == user}).count()
	}


	/**
	 * Enregistrement d"un device
	 *
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@PreAuthorize("hasPermission(#device, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def favori(Device device, boolean favori) throws SmartHomeException {
		device.favori = favori
		return this.save(device)
	}


	/**
	 * Déplacement dans un autre tableau de bord
	 *
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@PreAuthorize("hasPermission(#device, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def moveToTableauBord(Device device, String tableauBord) throws SmartHomeException {
		device.tableauBord = tableauBord
		device.groupe = null
		return this.save(device)
	}


	/**
	 * Déplacement dans un autre groupe
	 *
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@PreAuthorize("hasPermission(#device, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def moveToGroupe(Device device, String groupe) throws SmartHomeException {
		device.groupe = groupe
		return this.save(device)
	}


	/**
	 * Calcul des tableaux de bord
	 * 
	 * @return
	 */
	List<String> groupByTableauBord(long userId) {
		return Device.createCriteria().list {
			isNotNull 'tableauBord'
			eq 'user.id', userId
			projections {
				groupProperty 'tableauBord'
			}
			order 'tableauBord'
		}
	}


	/**
	 * Dernière activité du user
	 *
	 * @param userId
	 * @param maxEvent
	 * @param maxDay
	 * @return
	 * @throws SmartHomeException
	 */
	def listLastByUser(Long userId, int maxEvent, int maxDay) throws SmartHomeException {
		if (!userId) {
			throw new SmartHomeException("userId required !")
		}

		return Device.createCriteria().list() {
			user {
				idEq(userId)
			}

			gt 'dateValue', (new Date() - maxDay)
			maxResults(maxEvent)
			order "dateValue", "desc"
		}
	}


	/**
	 * Prépare les objets pour la vue
	 * Optimise le chargement des métadonnées de chaque objet en
	 * parallélisant le traitement de chaque objet
	 * 
	 * @param devices
	 */
	void prepareForView(List<Device> devices) throws SmartHomeException {
		if (devices) {
			PromiseList promiseList = new PromiseList()

			devices.each { device ->
				promiseList << {
					device.newDeviceImpl()
					device.deviceImpl.prepareForView()
				}
			}

			promiseList.get()
		}
	}
}
