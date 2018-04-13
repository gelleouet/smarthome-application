package smarthome.automation

import java.io.Serializable;

import org.quartz.CronExpression;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import smarthome.automation.scheduler.EventSimpleJob;
import smarthome.automation.scheduler.SmarthomeScheduler;
import smarthome.core.AbstractService;
import smarthome.core.AsynchronousMessage;
import smarthome.core.ClassUtils;
import smarthome.core.ExchangeType;
import smarthome.core.QueryUtils;
import smarthome.core.ScriptUtils;
import smarthome.core.SmartHomeCoreConstantes;
import smarthome.core.SmartHomeException;
import smarthome.core.chart.GoogleChart;
import smarthome.core.chart.GoogleDataTableCol;
import smarthome.rule.EventDecalageRuleService;
import smarthome.security.User;


class EventService extends AbstractService {

	DeviceService deviceService
	ScenarioService scenarioService
	SmarthomeScheduler smarthomeScheduler
	EventDecalageRuleService eventDecalageRuleService
	ModeService modeService
	HouseService houseService
	NotificationService notificationService
	
	
	/**
	 * Edition ACL
	 * 
	 * @param event
	 * @return
	 */
	@PreAuthorize("hasPermission(#event, 'OWNER')")
	Event edit(Event event) {
		return event
	}
	
	
	/**
	 * Suppression d'une instance
	 * 
	 * @param domain
	 * @return
	 */
	@PreAuthorize("hasPermission(#event, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	void delete(Event event) {
		try {
			// flush direct pour catcher une erreur SQL (ex : clé étrangère) et la renvoyer en SmartHomeException
			// sinon l'erreur est déclenchée hors méthode
			event.delete(flush: true)
		} catch (Exception ex) {
			throw new SmartHomeException(ex, event)
		}
	}
	
	/**
	 * Enregistrement des modes
	 *
	 * @param domain
	 *
	 * @return domain
	 */
	@PreAuthorize("hasPermission(#event, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Event saveModes(Event event) throws SmartHomeException {
		def existModes = []
		
		// supprime les modes non présents
		event.modes?.removeAll { eventMode ->
			Mode mode = event.modeList.find {
				it.id == eventMode.mode.id
			}
			
			// si on l'a trouvé, on le flag car on ne devra pas le réinséré
			if (mode) {
				existModes << mode
			}
			
			return !mode
		}
		
		// supprime les modes déjà présents
		event.modeList.removeAll(existModes)
		
		// ajoute les modes sélectionnés
		event.modeList.each {
			event.addToModes(new EventMode(event: event, mode: it))
		}
		
		super.save(event)
		
		return event
	}
	
	
	/**
	 * Enregistrement des modes
	 *
	 * @param domain
	 *
	 * @return domain
	 */
	@PreAuthorize("hasPermission(#event, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Event saveDevices(Event event) throws SmartHomeException {
		def existDevices = []
		
		// supprime les devices non présents
		event.devices?.removeAll { eventDevice ->
			Device device = event.deviceList.find {
				it.id == eventDevice.device.id
			}
			
			// si on l'a trouvé, on le flag car on ne devra pas le réinséré
			if (device) {
				existDevices << device
			}
			
			return !device
		}
		
		// supprime les modes déjà présents
		event.deviceList.removeAll(existDevices)
		
		// ajoute les modes sélectionnés
		event.deviceList.each {
			event.addToDevices(new EventDevice(event: event, device: it))
		}
		
		super.save(event)
		
		return event
	}
	
	
	/**
	 * Enregistrement d'un domain
	 *
	 * @param domain
	 *
	 * @return domain
	 */
	@PreAuthorize("hasPermission(#event, 'OWNER')")
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	def save(event) throws SmartHomeException {
		if (event.cron) {
			if (!CronExpression.isValidExpression(event.cron)) {
				throw new SmartHomeException("Expression de planification incorrecte !", event);
			}
		}
		
		if (!event.save()) {
			throw new SmartHomeException("Erreur enregistrement device event", event);
		}
		
		return event
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
		
		return Event.createCriteria().list(pagination) {
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
		
		return Event.createCriteria().list() {
			user {
				idEq(userId)
			}
			
			eq 'actif', true
			gt 'lastEvent', (new Date() - maxDay)
			join "device"
			maxResults(maxEvent)
			order "lastEvent", "desc"
		}
	}
	
	
	/**
	 * Retourne les événements actifs  associés à un device
	 * 
	 * @param device
	 * @return
	 */
	List<Event> listActifByDevice(Device device) {
		return Event.createCriteria().list {
			eq 'actif', true
			devices {
				eq 'device', device
			}	
		}	
	}
	
	
	/**
	 * Les actions possibles au niveau des triggers
	 * 
	 * @return
	 */
	List<Map> eventTriggerActions() {
		return [
			//[name: Event.class.name, label: 'Evénement'],
			[name: Notification.class.name, label: 'Notification'],
			[name: Device.class.name, label: 'Objet'],
			[name: Scenario.class.name, label: 'Scénario']
			
		]
	}
	
	
	/**
	 * Prépare les triggers d'un event
	 * 
	 * @param event
	 * @param user
	 * @return
	 */
	Event prepareEventTriggers(Event event, User user) {
		event.triggers?.each { trigger ->
			prepareEventTrigger(trigger, user)
		}
		return event	
	}
	
	
	/**
	 * En fonction des données du trigger, initialise les données associées (les listes de choix)
	 *  
	 * @param eventTrigger
	 * @return
	 */
	EventTrigger prepareEventTrigger(EventTrigger eventTrigger, User user) {
		if (!eventTrigger.event.user) {
			eventTrigger.event.user = user
		}
		
		if (eventTrigger.domainClassName) {
			EventTriggerPreparable triggerInit = ClassUtils.forNameInstance(eventTrigger.domainClassName)
			eventTrigger.domainList = triggerInit.domainList(eventTrigger)
			eventTrigger.domainValue = triggerInit.domainValue()
			
			if (eventTrigger.domainId) {
				eventTrigger.actionList = triggerInit.actionList(eventTrigger)
				
				if (eventTrigger.actionName) {
					eventTrigger.parameterList = triggerInit.parameterList(eventTrigger)
				}
			}
		}
		
		return eventTrigger	
	} 
	
	
	/**
	 * Exécution synchrone d'un événement. L'excution peut être associée à un objet (device, event,
	 * alerte, etc). Dans ce cas, les infos sont passées dans la map de config
	 * 
	 * @param event
	 * @param heritedContext
	 * 
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Event execute(Event event, Map heritedContext = [:]) throws SmartHomeException {
		Date dateEvent = new Date()
		
		if (!event.attached) {
			event.attach()
		}
		
		// Vérifie si l'event doit être déclenché en fonction mode activé
		List<Mode> houseModes = houseService.defaultHouseModes(event.user)
		
		// annule l'exécution si les modes sélectionnés sur event ne sont pas activés
		if (!modeService.matchModes(event.modes*.mode, houseModes, event.inverseMode)) {
			log.info "Cancel event ${event.libelle} : mode not match !"
			return event
		}
		
		// construction du contexte pour l'exécution de l'événement et des triggers
		// on inject les variables prédéfinies (même null) pour éviter des plantages dans les scripts user
		// elles seront écrasées par le contexte hérité si présent
		Map context = [event: event, device: null, alert: null, alertLevel: null]
		context.putAll(heritedContext)
		
		// exécute la condition si présente
		// IMPORTANT : la condition est exécutée dans une transaction à part et surtout en lecture seule
		// pour éviter toute erreur de manip ou mauvaise intention
		def hasTrigger = true
		
		if (event.condition) {
			Event.withTransaction([propagationBehavior: TransactionDefinition.PROPAGATION_REQUIRES_NEW, readOnly: true]) {
				hasTrigger = ScriptUtils.runScript(event.condition, context)
			}
		}
		
		// le retour de la condition est un CronExpression. On vérifie qu'il matche avec l'heure d'exécution de l'event
		// et on le transforme en boolean
		if (hasTrigger instanceof CronExpression) {
			hasTrigger = hasTrigger.isSatisfiedBy(dateEvent)
		}
		
		// on s'assure que le result de la condition est bien un boolean
		// Toutes les conditions sont réunies pour déclencher tous les triggers
		if (hasTrigger instanceof Boolean && hasTrigger) {
			// charge les paramètres json des triggers
			event.triggerParameterToJson()
			
			event.triggers?.each { EventTrigger trigger ->
				// on complète le contexte pour chaque trigger avec les paramètres de l'action
				context.actionParameters = trigger.jsonParameters
				
				switch(trigger.domainClassName) {
					case (Device.class.name):
						Device triggerDevice = Device.read(trigger.domainId)
						String actionName = trigger.actionName
						
						// cas particulier pour l'exécution de la méthode de l'objet parent
						if (actionName == EventTrigger.HERITED_ACTION_NAME && heritedContext.device?.actionName) {
							actionName = heritedContext.device.actionName
							triggerDevice.value = heritedContext.device.value
							triggerDevice.command = heritedContext.device.command
						}
						
						deviceService.execute(triggerDevice, actionName, trigger.jsonParameters)
						break
						
					case (Notification.class.name):
						Notification notification = Notification.read(trigger.domainId)
						notificationService.execute(notification, context)
						break
						
					case (Scenario.class.name):
						Scenario scenario = Scenario.read(trigger.domainId)
						scenarioService.execute(scenario, context)
						break
				}
			}
			
			// trace l'exécution de l'event
			event.lastEvent = dateEvent
			event.save()
		}
		
		return event
	}
	
	
	/**
	 * Exécution d'un évenement programmé à une certaine date
	 * Gestion de la synchro soleil
	 * 
	 * @param event
	 * @param scheduledDate
	 * 
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Event executeSchedule(Event event, Date scheduledDate) throws SmartHomeException {
		Date newDate = eventDecalageRuleService.execute(event, true, [scheduledDate: scheduledDate])
		
		// un décalage est calculé, dans ce cas on créé un job "one-shot" qui sera exécuté 
		// ultérieurement à la date voulue + décalage
		if (newDate) {
			smarthomeScheduler.scheduleOneShotJob(EventSimpleJob, newDate, [eventId: event.id])
			
			// enregistrement du dernier décalage
			event.lastHeureDecalage = newDate.format(Event.FORMAT_HEURE_DECALAGE)
			event.save()
			
			log.info "Re-schedule event ${event.libelle} at ${newDate}"
			
			// IMPORTANT : pour détecter l'annulation de l'exécution
			return null
		} else {
			this.execute(event)
		}
		
		return event
	}
	
	
	/**
	 * Créé ou met à jour un event
	 * 
	 * @param eventName
	 * @param user
	 * @param cron
	 * @return
	 * @throws SmartHomeException
	 */
	@Transactional(readOnly = false, rollbackFor = [SmartHomeException])
	Event createOrUpdateEvent(String libelle, User user, String cron) throws SmartHomeException {
		Event event = Event.findByLibelleAndUser(libelle, user)
		
		if (!event) {
			event = new Event(user: user, libelle: libelle, actif: true)
		}	
		
		// met à jour le cron
		event.cron = cron
		
		return this.save(event)
	}
	
	
	/**
	 * Calcule les dates sur une année complète d'une planif solsticiale
	 * 
	 * @param event
	 * @return
	 * @throws SmartHomeException
	 */
	List<Map> getYearScheduledDates(Event event) throws SmartHomeException {
		List<Map> scheduledDates = []
		
		if (event.cron) {
			CronExpression cron = new CronExpression(event.cron)
			Date scheduledDate = cron.getNextValidTimeAfter(new Date().clearTime().copyWith([date: 1, month: Calendar.JANUARY])) 
			Date maxDate = scheduledDate.copyWith([date: 1, month: Calendar.JANUARY, year: scheduledDate[Calendar.YEAR] + 1])
			
			while (scheduledDate < maxDate) {
				scheduledDates << [object: event, parameters: [scheduledDate: scheduledDate]]
				scheduledDate += 1
			}
			
			eventDecalageRuleService.executeBatch(scheduledDates, true)
		}
		
		return scheduledDates
	}
	
	
	/**
	 * Création d'un graphique avec les dates de la planif
	 * 
	 * @param event
	 * @return
	 * @throws SmartHomeException
	 */
	GoogleChart createScheduledChart(Event event) throws SmartHomeException {
		GoogleChart chart = new GoogleChart(chartType: ChartTypeEnum.Line.factory)
		chart.values = this.getYearScheduledDates(event)
		
		chart.colonnes << new GoogleDataTableCol(label: "Date", type: "date", value: { deviceValue, index, currentChart ->
			deviceValue.parameters.scheduledDate
		})
		chart.colonnes << new GoogleDataTableCol(label: "Heure", type: "timeofday", value: { deviceValue, index, currentChart ->
			deviceValue.result ?: deviceValue.parameters.scheduledDate
		})
		
		// série par défaut en bleu
		chart.series << [color: '#3572b0', type: SeriesTypeEnum.area.toString()]
		
		return chart
	}
	
	
	/**
	 * Liste tous les événements planifiés
	 * 
	 * @return
	 */
	List<Map> listScheduledEventIds(Map pagination) {
		return Event.createCriteria().list(pagination) {
			isNotNull "cron"
			eq "actif", true
			projections {
				property "id", "id"
				property "cron", "cron"
			}
			order "id"
			// transformer pour récupérer une map au lieu d'un tableau
			resultTransformer org.hibernate.Criteria.ALIAS_TO_ENTITY_MAP
		}
	}
	
	
	/**
	 * Compte tous les événements planifiés
	 * 
	 * @return
	 */
	long countScheduledEvents() {
		return Event.createCriteria().get {
			isNotNull "cron"
			eq "actif", true
			projections {
				count("id")
			}
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
		Event.get(id)
	}
	
	
	/**
	 * Ajout d'un trigger sans persistance
	 * 
	 * @param event
	 * @return
	 */
	Event addTrigger(Event event) {
		event.clearNotPersistTriggers()
		event.triggers << new EventTrigger(event: event)
		return event
	}
	
	
	/**
	 * Suppression d'un trigger sans persistance
	 * 
	 * @param event
	 * @return
	 */
	Event deleteTrigger(Event event, Integer status) {
		event.clearNotPersistTriggers()
		event.triggers?.removeAll {
			it.status == status
		}
		return event
	}
}
