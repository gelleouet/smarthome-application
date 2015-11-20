package smarthome.automation

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;
import groovy.time.TimeCategory;
import groovy.time.TimeDuration;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.ExchangeType;
import smarthome.core.QueryUtils;
import smarthome.core.ScriptUtils;
import smarthome.core.SmartHomeException;
import smarthome.security.User;


class DeviceEventService extends AbstractService {

	DeviceService deviceService
	WorkflowService workflowService
	
	
	/**
	 * Enregistrement d'un domain
	 *
	 * @param domain
	 *
	 * @return domain
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def save(deviceEvent) throws SmartHomeException {
		if (!deviceEvent.save()) {
			throw new SmartHomeException("Erreur enregistrement device event", deviceEvent);
		}
		
		return deviceEvent
	}
	
	
	/**
	 * Liste les devices d'un user
	 *
	 * @param pagination
	 * @return
	 * @throws SmartHomeException
	 */
	def listByUser(String search, Long userId, Map pagination) throws SmartHomeException {
		if (!userId) {
			throw new SmartHomeException("userId required !")
		}
		
		return DeviceEvent.createCriteria().list(pagination) {
			user {
				idEq(userId)
			}
			
			if (search) {
				ilike 'libelle', QueryUtils.decorateMatchAll(search)
			}
			
			join "device"
			order "libelle"
		}
	}
	
	
	/**
	 * Déclenche les événéments associés à un device en exécutant chaque condition
	 *
	 * @param device
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def triggerEvents(Device device) throws SmartHomeException {
		if (!device.attached) {
			device.attach()
		}
		
		def context = null
		
		device.events?.each { event ->
			// n'actionne que les events actifs
			if (!event.actif) {
				return	
			}
			
			def hasTrigger
			
			// charge une seule fois le contexte
			if (context == null) {
				context = this.contextForTrigger(device)
			}
			
			// exécute la condition si présente
			// IMPORTANT : la condition est exécutée dans une transaction à part et surtout en lecgure seule
			// pour éviter toute erreur de manip ou mauvaise intention
			if (event.condition) {
				DeviceEvent.withTransaction([propagationBehavior: TransactionDefinition.PROPAGATION_REQUIRES_NEW, readOnly: true]) {
					hasTrigger = ScriptUtils.runScript(event.condition, context)
				}
			} else {
				// déclenchement systématique si aucune condition
				hasTrigger = true
			}
			
			// on s'assure que le result de la condition est bien un boolean
			if (hasTrigger instanceof Boolean && hasTrigger) {
				// Toutes les conditions sont réunies pour déclencher tous les triggers
				event.triggers?.each { trigger ->
					// déclenchement d'un workflow
					if (trigger.workflow) {
						log.info "Trigger workflow ${workflow.label} from device ${device.label}"
					}
					
					// déclenchement d'un autre device via une action
					if (trigger.device && trigger.actionName) {
						log.info "Trigger device ${trigger.device.label}->${trigger.actionName} from device ${device.label}"
						
						// exécute le pre-script dans une transaction en lecture seule
						if (trigger.preScript) {
							// modifie le contexte pour y rajouter la variable "deviceTrigger"
							DeviceEvent.withTransaction([propagationBehavior: TransactionDefinition.PROPAGATION_REQUIRES_NEW, readOnly: true]) {
								ScriptUtils.runScript(event.condition, trigger.preScript)
							}
						}
						
						deviceService.invokeAction(trigger.device, trigger.actionName)
					}
				}
				
				// trace l'exécution de l'event
				event.lastEvent = new Date()
				event.save()
			}
		}
	}
	
	
	/**
	 * Construit un contexte pour l'exécution des conditions event
	 * 
	 * @param device
	 * @return
	 */
	Map contextForTrigger(Device device) {
		def context = [:]
		
		context.device = device
		context.devices = [:]
		
		// charge tous les devices et les met dans la map indexés par leurs MAC
		deviceService.listByUser(new DeviceSearchCommand(userId: device.user.id))?.each { 
			context.devices[(it.mac)] = it
		}
		
		return context
	}
}
