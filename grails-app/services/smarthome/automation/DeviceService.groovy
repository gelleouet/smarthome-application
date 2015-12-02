package smarthome.automation

import java.io.Serializable;

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;
import groovy.time.TimeCategory;
import groovy.time.TimeDuration;

import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.ExchangeType;
import smarthome.core.QueryUtils;
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
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def save(Device device) throws SmartHomeException {
		if (!device.save()) {
			throw new SmartHomeException("Erreur enregistrement device !", device)
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
		def device = Device.findByMacAndAgent(datas.mac, fetchAgent)
		
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
		def device = Device.findByMacAndAgent(datas.mac, fetchAgent)
		
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
		
		if (!device.attached) {
			device.attach()
		}
		
		def deviceType = device.newDeviceImpl()
		
		// ne trace que si activé sur le device
		if (deviceType.isTraceValue()) {
			value = new DeviceValue(device: device, value: device.value, dateValue: device.dateValue)
			
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
					value = new DeviceValue(device: device, name: it.name, value: it.value, dateValue: device.dateValue)
					
					if (!value.save()) {
						throw new SmartHomeException("Erreur trace meta valeur !", value)
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
		log.info "Load trace values for ${device.mac} from ${start} to ${end}"
		def dayDuration = 0
		
		use(groovy.time.TimeCategory) {
			dayDuration = end - start
		}
		
		// projections automatiques pour les longues périodes (> 1 jour)
		// pour éviter trop de volumes de données
		if (!projection && dayDuration.days > 1) {
			return DeviceValue.createCriteria().list {
				eq 'device', device
				between 'dateValue', start, end
				
				if (name) {
					if (name == '') {
						isNull 'name'
					} else {
						eq 'name', name
					}
				}
				
				projections {
					max(value)
					min(value)
					avg(value)
					count(value)
				}
				
				order 'dateValue', 'name'
			}
		} else {
			return DeviceValue.createCriteria().list {
				eq 'device', device
				between 'dateValue', start, end
				
				if (name) {
					if (name == '') {
						isNull 'name'
					} else {
						eq 'name', name
					}
				}
				
				order 'dateValue', 'name'
			}
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
		def search = QueryUtils.decorateMatchAll(command.search)
		
		if (!command.userId) {
			throw new SmartHomeException("userId must be fill !", command)
		}
		
		return Device.createCriteria().list(command.pagination) {
			user {
				idEq(command.userId)
			}
			
			if (command.search) {
				or {
					ilike 'label', search
					ilike 'groupe', search
				}
			}
			
			if (command.filterShow) {
				eq "show", true
			}
			
			join "deviceType"
		}
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
}
