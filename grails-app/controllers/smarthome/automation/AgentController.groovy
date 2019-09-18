
package smarthome.automation

import grails.converters.JSON

import org.springframework.security.access.annotation.Secured

import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.QueryUtils
import smarthome.core.SmartHomeException
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum



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
	def subscribe(MessageAgentCommand command) {
		command.publicIp = request.remoteAddr

		try {
			def agentToken = agentService.subscribe(command)

			if (agentToken) {
				render agentToken as JSON
			} else {
				render(status: 400, text: 'Error no token')
			}
		} catch (SmartHomeException ex) {
			log.error "Cannot subscribe : $ex.message"
			render(status: 400, text: ex.message)
		}
	}


	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Agents", navigation = NavigationEnum.configuration, header = "Smarthome")
	def agents(String agentSearch) {
		def user = authenticatedUser
		def agents = agentService.listByUser(agentSearch, user.id, this.getPagination([:]))
		def recordsTotal = agents.totalCount

		// agents est accessible depuis le model avec la variable agent[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond agents, model: [recordsTotal: recordsTotal, agentSearch: agentSearch, user: user]
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
		edit(agent)
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


	/**
	 * Reset config
	 *
	 * @param agent
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "agents", modelName = AgentController.COMMAND_NAME)
	def resetConfig(Agent agent) {
		agentService.resetConfig(agent)
		redirect(action: 'agents')
	}
}
