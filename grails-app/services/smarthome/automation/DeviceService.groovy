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
import smarthome.core.ExchangeType;
import smarthome.core.QueryUtils;
import smarthome.core.ScriptUtils;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class DeviceService extends AbstractService {

	AgentService agentService
	
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
		
		def fetchAgent = Agent.get(agent.id)
		
		// ajout d'un verrou pessimiste car erreur quand remontée infos depuis agent
		// entre changeMetadataFromAgent et changeValueFromAgent
		def device = Device.findByMacAndAgent(datas.mac, fetchAgent, [lock: true])
		
		// on tente de le créer auto si on a toutes les infos
		if (!device) {
			def deviceType = DeviceType.findByImplClass(datas.implClass)
			
			if (!deviceType) {
				throw new SmartHomeException("Type device (implClass) is empty !")
			}
			
			device = new Device(agent: fetchAgent, user: fetchAgent.user, mac: datas.mac, 
				label: datas.label ?: datas.mac, deviceType: deviceType)
		}
		
		// insère nouvelle valeur
		// transforme les datas si formule présente sur le device
		if (device.formula) {
			ScriptUtils.runScript(device.formula, [device: datas])
		}
		device.value = datas.value
		device.dateValue = datas.dateValue ? Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", datas.dateValue) : new Date()
		
		// on cale la date sur le bon timezone de l'agent
		if (datas.timezoneOffset) {
			// le offset est en minute
			def offset = new TimeDuration(0, datas.timezoneOffset.toInteger(), 0, 0)
			use(TimeCategory) {
				device.dateValue = device.dateValue - offset
			}
		}
		
		// gestion des métavalues
		datas.metavalues?.each { key, values ->
			device.addMetavalue(key, values)
		}
		
		// on passe les méta données dans l'implémentation pour transformation ou calcul
		try {
			device.newDeviceImpl().prepareMetaValuesForSave()
		} catch (Exception e) {
			log.error("Prepare metavalues for device ${device.label}", e)
		}
		
		
		return this.save(device)
	}
	
	
	/**
	 *
	 * @param agent
	 * @param datas
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Device changeMetadataFromAgent(Agent agent, def datas) throws SmartHomeException {
		log.info "change metadata ${datas.mac}"
		
		if (!datas.mac) {
			throw new SmartHomeException("Mac is empty !")
		}
		
		def fetchAgent = Agent.get(agent.id)
		
		// ajout d'un verrou pessimiste car erreur quand remontée infos depuis agent
		// entre changeMetadataFromAgent et changeValueFromAgent
		def device = Device.findByMacAndAgent(datas.mac, fetchAgent, [lock: true])
		
		// on tente de le créer auto si on a toutes les infos
		if (!device) {
			def deviceType = DeviceType.findByImplClass(datas.implClass)
			
			if (!deviceType) {
				throw new SmartHomeException("Type device (implClass) is empty !")
			}
			
			device = new Device(agent: fetchAgent, user: fetchAgent.user, mac: datas.mac,
				label: datas.label ?: datas.mac, deviceType: deviceType)
		}
		
		// gestion des metadatas
		datas.metadatas?.each { key, values ->
			device.addMetadata(key, values)
		}
		
		return this.save(device)
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
		
		// ne trace que si activé sur le device
		if (doubleValue != null && deviceType.isTraceValue() ) {
			value = new DeviceValue(device: device, value: doubleValue, dateValue: device.dateValue)
			
			if (!value.save()) {
				throw new SmartHomeException("Erreur trace valeur !", value)
			}
		}
		
		// enregistrement des valeurs spécifiques
		device.metavalues?.each {
			if (it.value) {
				def metaValuesInfo = deviceType.metaValuesInfo()
				
				// verifie si le trace est activé pour la metavalue
				if (metaValuesInfo && metaValuesInfo[it.name] && metaValuesInfo[it.name].trace) {
					doubleValue = DeviceValue.parseDoubleValue(it.value)
					
					if (doubleValue != null) {
						value = new DeviceValue(device: device, name: it.name, value: doubleValue, dateValue: device.dateValue)
						
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
}
