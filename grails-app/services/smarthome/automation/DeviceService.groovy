package smarthome.automation

import java.io.Serializable;

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;
import groovy.time.TimeCategory;
import groovy.time.TimeDuration;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import smarthome.automation.deviceType.AbstractDeviceType;
import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.Chronometre;
import smarthome.core.DateUtils;
import smarthome.core.ExchangeType;
import smarthome.core.QueryUtils;
import smarthome.core.ScriptUtils;
import smarthome.core.SmartHomeException;
import smarthome.rule.DeviceTypeDetectRuleService;
import smarthome.security.User;


class DeviceService extends AbstractService {

	AgentService agentService
	DeviceTypeDetectRuleService deviceTypeDetectRuleService
	
	
	/**
	 * Enregistrement d"un device
	 * 
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@PreAuthorize("hasPermission(#device, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def save(Device device) throws SmartHomeException {
		if (!device.save()) {
			throw new SmartHomeException("Erreur enregistrement device !", device)
		}
		
		return device
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
		
		throw new SmartHomeException("Accès refusé !")
	}
	
	
	/**
	 * Changement d'une métadata et envoit à l'agent
	 * 
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def changeMetadata(Device device, String metadataName) throws SmartHomeException {
		this.save(device)
		
		if (device.agent) {
			def data = [header: 'config', deviceMac: device.mac, metadataName: metadataName,
				metadataValue: device.metadata(metadataName)?.value]
			
			try {
				agentService.sendMessage(device.agent, data)
			} catch (Exception e) {
				log.error("Can't send configuration change to agent : ${e.message}")
			}
		}
		
		return device
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
		DeviceValue.where({ device == device }).deleteAll()
		DeviceMetadata.where({ device == device }).deleteAll()
		DeviceMetavalue.where({ device == device }).deleteAll()
		DeviceShare.where({ device == device }).deleteAll()
		
		device.delete();
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
	@AsynchronousMessage(exchange = "smarthome.automation.deviceService.changeValue", exchangeType = ExchangeType.FANOUT)
	Device changeValueFromAgent(Agent agent, def datas) throws SmartHomeException {
		log.info "change value ${datas.mac} : ${datas.value}"
		
		if (!datas.mac) {
			throw new SmartHomeException("Mac is empty !")
		}
		
		if (datas.value == null) {
			throw new SmartHomeException("Value is empty !")
		}
		
		String implClass = deviceTypeDetectRuleService.execute(datas, true)
		
		def virtualMetas = []
		def fetchAgent = Agent.get(agent.id)
		def device = findOrCreateDevice(fetchAgent, datas.mac, datas.label, implClass)
		def resultDevice = null  
		
		// bien metre à jour la date avant toutes les autres instructions
		device.dateValue = DateUtils.parseJson(datas.dateValue, datas.timezoneOffset)
		
		
		// ajout des métavalues
		datas.metavalues?.each { key, values ->
			def meta = device.addMetavalue(key, values)
			
			// on tri les metas pour savoir si on doit créer des devices virtuels
			if (meta.virtualDevice) {
				virtualMetas << meta
			}
		}
		
		// gestion des metadatas
		datas.metadatas?.each { key, values ->
			device.addMetadata(key, values)
		}
		
		// gestion des devices virtuels associés aux metas virtuels
		processVirtualMetas(device, virtualMetas)
		
		// si toutes les valeurs envoyées dans metavalue sont des  virtuelMeta
		// alors on ne touche pas au device principal mais on met à jour seulement les devices virtuels
		if (! (datas.metavalues?.size() == virtualMetas.size() && virtualMetas)) {
			device.value = datas.value
			device.processValue()
			resultDevice = device
		}
		
		// dans tous les cas faut enregistrer car il faut quand même enregistrer les valeurs des meta
		// même si elles sont virtuelles
		this.save(device)
		
		// retourner null désactive le déclenchement des historisations sur le device
		return resultDevice
	}
	
	
	/**
	 * Recherche ou création d'un device
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
	private void processVirtualMetas(Device device, List metas) throws SmartHomeException {
		metas?.each {
			def virtualDevice = findOrCreateDevice(device.agent, "${device.mac}-${it.name}",
				"${it.label}  -> ${device.label}",
				device.deviceType.implClass) 
			
			virtualDevice.value = it.value
			virtualDevice.dateValue = device.dateValue
			virtualDevice.processValue()
			
			this.save(virtualDevice)
			
			// envoi d'un message AMQP manuellement pour relancer toutes les actions sur les
			// devices virtuels
			this.sendAsynchronousMessage("smarthome.automation.deviceService.changeValue", "", 
				[result: virtualDevice], ExchangeType.FANOUT)
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
	@AsynchronousMessage(exchange = "smarthome.automation.deviceService.changeValue", exchangeType = ExchangeType.FANOUT)
	Device invokeAction(Device device, String actionName) throws SmartHomeException {
		log.info "Invoke action ${actionName} on device ${device.mac}"
		
		// instancie le type device pour exécuter l'action
		def deviceImpl = device.newDeviceImpl()
		device.fetchParams()
		def context = new WorkflowContext()
		
		try {
			deviceImpl."${actionName}"(context)
		} catch (Exception e) {
			log.error("Runtime execution ${actionName} on device : ${e.message}")
			throw new SmartHomeException("Can't invoke ${actionName} on device ${device.label}")
		}
	
		// traca de l'action
		device.dateValue = new Date()
		device.actionName = actionName
		
		return this.save(device)
	}
	
	
	/**
	 * Trace le changement de valeur pour garder un historique
	 * 
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void traceValue(Device device) throws SmartHomeException {
		log.info "Trace value for device ${device.mac}"
		def value
		def doubleValue
		
		if (!device.attached) {
			device.attach()
		}
		
		def deviceType = device.newDeviceImpl()
		doubleValue = DeviceValue.parseDoubleValue(device.value)
		
		// trace la valeur principale du device
		if (doubleValue != null) {
			value = new DeviceValue(device: device, value: doubleValue, dateValue: device.dateValue)
			
			if (!value.save()) {
				throw new SmartHomeException("Erreur trace valeur !", value)
			}
		}
		
		// trace les metavalues 
		device.metavalues?.each {
			if (it.value) {
				// si la meta est principale, pas besoin de tracer car déjà fait au niveau device
				// si meta virtuelle, pas besoin non car ca sera fait au niveau du device virtuel
				// sinon on regarde si activée au niveau meta
				if (it.trace && !it.main && !it.virtualDevice) {
					doubleValue = DeviceValue.parseDoubleValue(it.value)
					
					if (doubleValue != null) {
						value = new DeviceValue(device: device, name: it.name, value: doubleValue,
							dateValue: device.dateValue)
						
						if (!value.save()) {
							throw new SmartHomeException("Erreur trace meta valeur !", value)
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Charge les valeurs du device depuis sinceHour
	 * 
	 * @param device
	 * @param sinceHour
	 * @param name
	 * @return
	 * @throws SmartHomeException
	 */
	def values(Device device, Date start, Date end, String name, DataModifierEnum projection = null) throws SmartHomeException {
		def dayDuration = 0
		def values
		AbstractDeviceType deviceImpl = device.newDeviceImpl()
		
		use(groovy.time.TimeCategory) {
			dayDuration = end - start
		}
		
		// projections automatiques pour les longues périodes (> 1 jour)
		// pour éviter trop de volumes de données
		if (projection || dayDuration.days > AbstractDeviceType.MAX_DAY_WITHOUT_PROJECTION) {
			values = deviceImpl.projectionValues(start, end, name, dayDuration.days, projection)
		} else {
			values = deviceImpl.values(start, end, name, dayDuration.days)
		}
		
		log.info "Load trace values for ${device.mac} from ${start} to ${end} : ${values?.size()} values"
		
		return values
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
		
		// calcul des devices partagés à l'utilisateur
		def sharedDevices = command.sharedDevice ? this.listSharedDeviceId(command.userId) : []
		
		def commonCriteria = {
			if (command.sharedDevice) {
				'in' 'id', sharedDevices ?: [0L]
			} else {
				user {
					idEq(command.userId)
				}
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
		
		log.info "List devices : ${chrono.stop()}ms"
		
		return devices
	}
	
	
	/**
	 * Utile pour les environnements sans session hibernate automatique
	 * Ex : Camel ESB
	 *
	 * @param id
	 * @return
	 */
	def findById(Serializable id) {
		Device.get(id)
	}
	
	
	/**
	 * Construit le message à envoyer à un agent pour exécuter une action
	 * 
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	Map invokeActionMessage(Device device, String invokeAction) throws SmartHomeException {
		if (!device.attached) {
			device.attach()
		}
		
		// chargement des associations pour eviter erreur à conversion json
		// @see static Device JSON.registerObjectMarshaller
		def deviceType = device.deviceType
		device.metadatas = DeviceMetadata.findAllByDevice(device)
		
		return [header: 'invokeAction', action: invokeAction, 
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
}
