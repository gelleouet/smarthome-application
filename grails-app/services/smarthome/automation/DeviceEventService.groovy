package smarthome.automation

import static java.util.Calendar.DAY_OF_MONTH
import static java.util.Calendar.MONTH
import static java.util.Calendar.YEAR

import java.io.Serializable;

import grails.converters.JSON;
import grails.plugin.cache.CachePut;
import grails.plugin.cache.Cacheable;
import groovy.time.TimeCategory;
import groovy.time.TimeDuration;

import org.quartz.CronExpression;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

import smarthome.automation.scheduler.DeviceEventTimerJob;
import smarthome.automation.scheduler.SmarthomeScheduler;
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
	SmarthomeScheduler smarthomeScheduler
	
	
	/**
	 * Enregistrement d'un domain
	 *
	 * @param domain
	 *
	 * @return domain
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def save(deviceEvent) throws SmartHomeException {
		if (deviceEvent.cron) {
			if (!CronExpression.isValidExpression(deviceEvent.cron)) {
				throw new SmartHomeException("Expression de planification incorrecte !", deviceEvent);
			}
		}
		
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
	def triggerEvents(Device device, String syncActionName) throws SmartHomeException {
		if (!device.attached) {
			device.attach()
		}
		
		// ne prend que les events actifs, non planifiés et avec trigger
		def events = device.events?.findAll {
			it.actif && !it.cron && it.triggers
		}
		
		if (events) {
			def context = this.buildContext(device)
			
			events.each { event ->
				triggerDeviceEvent(event, syncActionName, context)
			}
		}
	}
	
	
	/**
	 * Exécution d'un événement associé à un device
	 * 
	 * @param event
	 * @param syncActionName
	 * @param context
	 * 
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def triggerDeviceEvent(DeviceEvent event, String syncActionName, Map context) throws SmartHomeException {
		def hasTrigger
		
		if (!event.attached) {
			event.attach()
		}
		
		def device = event.device
		
		if (context == null) {
			context = this.buildContext(device)
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
					workflowService.execute(trigger.workflow, context)
				}
				
				// déclenchement d'un autre device via une action choisie
				// ou l'action du device source
				if (trigger.device && (trigger.actionName || syncActionName)) {
					log.info "Trigger device ${trigger.device.label} from device ${device.label}"
					def runScript = true
					
					// exécute le pre-script dans une transaction en lecture seule
					if (trigger.preScript) {
						// modifie le contexte pour y rajouter la variable "deviceTrigger"
						context.triggerDevice = trigger.device
						
						DeviceEvent.withTransaction([propagationBehavior: TransactionDefinition.PROPAGATION_REQUIRES_NEW, readOnly: true]) {
							runScript = ScriptUtils.runScript(trigger.preScript, context)
						}
					}
					
					// si le script ne renvoit pas boolean, on l'exécute
					// sinon on tient compte de la valeur du boolean
					if (!(runScript instanceof Boolean) || runScript) {
						if (trigger.actionName) {
							deviceService.invokeAction(trigger.device, trigger.actionName)
						} else {
							// si on doit exzcuter l'actyion du device source
							// on doit également recoipié son état
							trigger.device.value = device.value
							trigger.device.command = device.command
							deviceService.invokeAction(trigger.device, syncActionName)
						}
					}
				}
			}
			
			// trace l'exécution de l'event
			event.lastEvent = new Date()
			event.save()
		}
	}
	
	
	/**
	 * Exécution en mode asycnhrone d'un évenement programmé à une certaine date
	 * Gestion de la synchro soleil
	 * 
	 * @param event
	 * @param scheduledDate
	 * 
	 * @return null pour ne pas exécuter l'event par le système asynchrone
	 * @throws SmartHomeException
	 */
	@AsynchronousMessage()
	DeviceEvent executeScheduleDeviceEvent(DeviceEvent event, Date scheduledDate) throws SmartHomeException {
		int decalageMinute = 0
		
		if (event.synchroSoleil) {
			decalageMinute = this.getDecalageMinute(event)
			
			// un décalage est calculé, dans ce cas on créé un job "one-shot" qui sera exécuté 
			// ultérieurement à la date voulue + décalage
			if (decalageMinute) {
				def newDate
				
				use(TimeCategory) {
					newDate = scheduledDate + decalageMinute.minutes
				}
				
				log.info "Re-schedule event ${event.libelle} at ${newDate} (${decalageMinute}min)"
				smarthomeScheduler.scheduleOneShotJob(DeviceEventTimerJob, newDate, [deviceEventId: event.id])
				
				return null
			}
		}
		
		return event
	}
	
	
	/**
	 * Calcule le décalage en minute du jour si la synchro horaire est activée
	 *  
	 * @param event
	 * @return
	 * @throws SmartHomeException
	 */
	int getDecalageMinute(DeviceEvent event) throws SmartHomeException {
		if (!event.decalageMinute) {
			return 0
		}
		
		Date now = new Date().clearTime()
		Date solsticeEte = now.copyWith([date: 21, month: 5])
		Date solsticeHiver = now.copyWith([date: 21, month: 11])
		Date solsticeHiverPrec = now.copyWith([date: 21, month: 11, year: now[YEAR]-1])
		
		def decalage
		def totalJour
		
		// calcul de la durée entre le jour J et le solstice de référence
		use(TimeCategory) {
			if (now <= solsticeEte) {
				decalage = now - solsticeHiverPrec
			} else if (now > solsticeEte && now <= solsticeHiver) {
				decalage = solsticeHiver - now
			} else {
				decalage = now - solsticeHiver
			}
			
			totalJour = (now <= solsticeEte ? solsticeEte - solsticeHiverPrec : solsticeHiver - solsticeEte)
		}
		
		// produit en X pour calculer le décalage journalier en fonction du total
		if (totalJour.days) {
			return (decalage.days * event.decalageMinute) / totalJour.days
		} else {
			return 0
		}
	}
	
	
	/**
	 * Construit un contexte pour l'exécution des conditions event
	 * 
	 * @param device
	 * @return
	 */
	Map buildContext(Device device) {
		def context = [:]
		
		context.device = device
		context.devices = [:]
		
		// charge tous les devices et les met dans la map indexés par leurs MAC
		deviceService.listByUser(new DeviceSearchCommand(userId: device.user.id))?.each { 
			context.devices[(it.mac)] = it
		}
		
		return context
	}
	
	
	/**
	 * Liste tous les événements planifiés
	 * 
	 * @return
	 */
	List<DeviceEvent> listScheduledEvent() {
		return DeviceEvent.createCriteria().list {
			isNotNull "cron"
			eq "actif", true
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
		DeviceEvent.get(id)
	}
}
