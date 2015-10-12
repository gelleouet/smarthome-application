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
	 * Enregistrement d"un device
	 * 
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def saveWithEvents(Device device) throws SmartHomeException {
		device.clearNotPersistEvents()
		return this.save(device)
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
			
			device = new Device(agent: fetchAgent, user: fetchAgent.user, mac: datas.mac, label: datas.mac, deviceType: deviceType)
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
		datas.metavalues?.each { key, value ->
			device.addMetavalue(key, value)
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
		def deviceImpl = device.deviceType.newDeviceType()
		deviceImpl.device = device
		device.fetchParams()
		def context = new WorkflowContext()
		
		try {
			deviceImpl."${actionName}"(context)
		} catch (Exception e) {
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
		
		if (!device.attached) {
			device.attach()
		}
		
		def value = new DeviceValue(device: device, value: device.value, dateValue: device.dateValue)
		
		if (!value.save()) {
			throw new SmartHomeException("Erreur trace valeur !", value)
		}
		
		// enregistrement des valeurs spécifiques
		device.metavalues?.each {
			if (it.value) {
				value = new DeviceValue(device: device, name: it.name, value: it.value, dateValue: device.dateValue)
				
				if (!value.save()) {
					throw new SmartHomeException("Erreur trace meta valeur !", value)
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
	List<DeviceValue> values(Device device, Long sinceHour, String name) throws SmartHomeException {
		log.info "Load trace values for ${device.mac} since ${sinceHour} hours"
		
		Date now = new Date()
		TimeDuration duration = new TimeDuration(sinceHour.toInteger(), 0, 0, 0)
		
		use(TimeCategory) {
			now = now - duration	
		}
		
		DeviceValue.createCriteria().list {
			eq 'device', device
			ge 'dateValue', now
			
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
