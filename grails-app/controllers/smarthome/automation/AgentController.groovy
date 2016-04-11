
package smarthome.automation

import grails.converters.JSON;

import org.springframework.security.access.annotation.Secured;

import smarthome.core.AbstractController;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.QueryUtils;
import smarthome.core.SmartHomeException;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;



@Secured("isAuthenticated()")
class AgentController extends AbstractController {

    private static final String COMMAND_NAME = 'agent'
	
	AgentService agentService
	
	
	/**
	 * Demande d'un agent pour obtenir un token de connexion
	 *
	 * @param agent
	 * @return
	 */
	@Secured("permitAll()")
	def subscribe(String mac, String username, String applicationKey, String privateIp, String agentModel) {
		def agent = new Agent(mac: mac, privateIp: privateIp, publicIp: request.remoteAddr, agentModel: agentModel)
		
		try {
			def agentToken = agentService.subscribe(agent, username, applicationKey)
			
			if (agentToken) {
				render agentToken as JSON
			} else {
			render(status: 400, text: 'Error no token')
			}
		} catch (SmartHomeException ex) {
			render(status: 400, text: ex.message)
		}
	}
	
	
	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Agents", navigation = NavigationEnum.configuration, header = "Installation", breadcrumb = [
		NavigableAction.CONFIG_APPLICATION,
		"Domotique"
	])
	def agents(String agentSearch) {
		def agents = agentService.listByUser(agentSearch, principal.id, this.getPagination([:]))
		def recordsTotal = agents.totalCount

		// agents est accessible depuis le model avec la variable agent[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond agents, model: [recordsTotal: recordsTotal, agentSearch: agentSearch]
	}
	
	
	/**
	 * Activation / désactivation d'un agent
	 * 
	 * @param agent
	 * @param actif
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "agents", modelName = AgentController.COMMAND_NAME)
	def activer(Agent agent, boolean actif) {
		agentService.activer(agent, actif)
		redirect(action: 'agents')
	}
	
	
	/**
	 * Ajout d'un device sur agent
	 * 
	 * @param agent
	 * @return
	 */
	def addDevice(Agent agent) {
		flash.device = new Device(agent: agent)
		redirect(action: 'create', controller: 'device')
	}
	
	
	/**
	 * Edition d'un agent
	 * 
	 * @param agent
	 * @return
	 */
	def edit(Agent agent) {
		agent = agentService.edit(agent)
		render(view: 'agent', model: [agent: agent])
	}
	
	
	/**
	 * Enregistrement d'un agent
	 * 
	 * @param agent
	 * @return
	 */
	def save(Agent agent) {
		agentService.save(agent)
		redirect(action: 'agents')
	}
	
	
	/**
	 * Démarre l'inclusion sur un agent
	 * 
	 * @param agent
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "agents", modelName = AgentController.COMMAND_NAME)
	def startInclusion(Agent agent) {
		agentService.startAssociation(agent, true)
		redirect(action: 'agents')
	}
	
	/**
	 * Démarre l'exclusion sur un agent
	 *
	 * @param agent
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "agents", modelName = AgentController.COMMAND_NAME)
	def startExclusion(Agent agent) {
		agentService.startAssociation(agent, false)
		redirect(action: 'agents')
	}
}
