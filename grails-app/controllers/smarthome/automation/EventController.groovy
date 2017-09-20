package smarthome.automation


import org.springframework.security.access.annotation.Secured;
import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.QueryUtils;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;


@Secured("isAuthenticated()")
class EventController extends AbstractController {

    private static final String COMMAND_NAME = 'event'
	
	EventService eventService
	ScenarioService scenarioService
	DeviceService deviceService
	ModeService modeService
	
	
	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Evénements", navigation = NavigationEnum.configuration, header = "Smarthome")
	def events(String eventSearch) {
		def search = QueryUtils.decorateMatchAll(eventSearch)
		
		def events = eventService.listByUser(eventSearch, principal.id, this.getPagination([:])) 
		def recordsTotal = events.totalCount

		// eventInstances est accessible depuis le model avec la variable eventInstance[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond events, model: [recordsTotal: recordsTotal, eventSearch: eventSearch]
	}
	
	
	/**
	 * Edition
	 *
	 * @param event
	 * @return
	 */
	def edit(Event event) {
		def editevent = parseFlashCommand(COMMAND_NAME, event)
		editevent = eventService.edit(editevent)
		editevent.triggerParameterToJson()
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editevent]))
	}


	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]
		def user = authenticatedUser
		
		model.devices = deviceService.listByUser(new DeviceSearchCommand(userId: user.id))
		model.modes = modeService.listModesByUser(user)
		model.triggerActions = eventService.eventTriggerActions()
		
		// on remplit avec les infos du user
		model << userModel
		
		// init les triggers
		eventService.prepareEventTriggers(userModel.event, user)
		
		return model
	}
	
	
	/**
	 * Enregistrement d'un nouveau
	 *
	 * @param event
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = "event")
	def save(Event event) {
		event.triggerParameterFromJson()
		event.user = authenticatedUser
		event.validate() // important car les erreurs sont traitées lors du binding donc le event.user sort en erreur
		checkErrors(this, event)
		event.clearNotPersistTriggers()
		eventService.save(event)
		eventService.saveModes(event)
		eventService.saveDevices(event)
		edit(event)
	}

		
	/**
	 * Suppression
	 *
	 * @param event
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "events", modelName = "")
	def delete(Event event) {
		eventService.delete(event)
		redirect(action: COMMAND_NAME + 's')
	}
	
	
	/**
	 * Exécute une action sur un device
	 *
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "events", modelName = "")
	def execute(Event event) {
		eventService.edit(event)
		eventService.execute(event)
		redirect(action: 'events')
	}
	
	
	/**
	 * Supprime un event en fonction de sa position
	 *
	 * @param device
	 * @param status
	 * @return
	 */
	def deleteTrigger(Event event, Integer status) {
		eventService.deleteTrigger(event, status)
		render(template: 'triggers', model: fetchModelEdit([event: event]))
	}

	def addTrigger(Event event) {
		eventService.addTrigger(event)
		render(template: 'triggers', model: fetchModelEdit([event: event]))
	}
	
	
	def templateTriggers(Event event) {
		event.clearNotPersistTriggers()
		render(template: 'triggers', model: fetchModelEdit([event: event]))
	}
	
	
	/**
	 * Synthèse activité
	 * 
	 * @return
	 */
	def synthese() {
		def lastEvents = eventService.listLastByUser(principal.id, 10, 7)
		render (template: 'eventActivite', model: [lastEvents: lastEvents])
	}
}
