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
		
		device.events?.each { event ->
			def trigger
			
			// exécute la condition si présente
			// IMPORTANT : la condition est exécutée dans une transaction à part et surtout en lecgure seule
			// pour éviter toute erreur de manip ou mauvaise intention
			if (event.condition) {
				DeviceEvent.withTransaction([propagationBehavior: TransactionDefinition.PROPAGATION_REQUIRES_NEW, readOnly: true]) {
					trigger = ScriptUtils.runScript(event.condition, this.contextForCondition(device))
				}
			} else {
				// déclenchement systématique si aucune condition
				trigger = true
			}
			
			// on s'assure que le result de la condition est bien un boolean
			if (trigger instanceof Boolean && trigger) {
				// TODO : déclenchement d'un workflow
				if (event.triggeredWorkflow) {
					log.info "Trigger workflow ${workflow.label} from device ${device.label}"
				}
				
				// déclenchement d'un autre device via une action
				if (event.triggeredDevice && event.triggeredAction) {
					log.info "Trigger device ${event.triggeredDevice.label}->${event.triggeredAction} from device ${device.label}"
					deviceService.invokeAction(event.triggeredDevice, event.triggeredAction)
				}
			}
		}
	}
	
	
	/**
	 * Construit un contexte pour l'exécution des conditions event
	 * 
	 * @param device
	 * @return
	 */
	Map contextForCondition(Device device) {
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
