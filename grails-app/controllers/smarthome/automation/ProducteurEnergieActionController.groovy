package smarthome.automation

import org.springframework.security.access.annotation.Secured

import smarthome.automation.deviceType.PanneauSolaire
import smarthome.core.AbstractController
import smarthome.core.ExceptionNavigationHandler
import smarthome.core.SmartHomeException
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum


@Secured("isAuthenticated()")
class ProducteurEnergieActionController extends AbstractController {

	private static final String COMMAND_NAME = 'producteurEnergieAction'

	ProducteurEnergieActionService producteurEnergieActionService
	ProducteurEnergieService producteurEnergieService
	DeviceService deviceService


	/**
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Mes actions", navigation = NavigationEnum.configuration, header = "Compte")
	def producteurEnergieActions(ProducteurEnergieActionCommand command) {
		def user = authenticatedUser // inject plugin spring security
		def actions = producteurEnergieActionService.listByUser(user, command, this.getPagination([:]))
		def recordsTotal = actions.totalCount

		// producteurEnergieInstances est accessible depuis le model avec la variable producteurEnergieInstance[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond actions, model: [recordsTotal: recordsTotal, command: command]
	}


	/**
	 * Edition
	 *
	 * @param producteurEnergie
	 * @return
	 */
	def edit(ProducteurEnergieAction producteurEnergieAction) {
		def editProducteurEnergieAction = parseFlashCommand(COMMAND_NAME, producteurEnergieAction)
		producteurEnergieActionService.edit(editProducteurEnergieAction)
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editProducteurEnergieAction]))
	}


	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]

		model.panneaux = deviceService.listByUser(new DeviceSearchCommand([userId: principal.id,
			deviceTypeClass: PanneauSolaire.name]))
		model.productions = producteurEnergieService.search(new ProducteurEnergieCommand(), [:])

		// on remplit avec les infos du user
		model << userModel

		return model
	}


	/**
	 * Enregistrement modification
	 *
	 * @param producteurEnergie
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = "producteurEnergieAction")
	def save(ProducteurEnergieAction producteurEnergieAction) {
		// reinject le user et revalide le domain pour reset les erreurs
		producteurEnergieAction.user = authenticatedUser // inject spring security plugin
		producteurEnergieAction.validate()

		checkErrors(this, producteurEnergieAction)
		producteurEnergieActionService.save(producteurEnergieAction)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Suppression
	 *
	 * @param producteurEnergie
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "producteurEnergieActions")
	def delete(ProducteurEnergieAction producteurEnergieAction) {
		producteurEnergieActionService.delete(producteurEnergieAction)
		redirect(action: COMMAND_NAME + 's')
	}


	/**
	 * Widget investissement collaboratif
	 * 
	 * @return
	 */
	def widgetInvestissement() {
		def user = authenticatedUser // inject plugin spring security
		def totalAction = producteurEnergieActionService.totalAction(user)
		def totalSurface = producteurEnergieActionService.totalSurface(user)
		def totalInvestissement = producteurEnergieActionService.totalInvestissement(user)
		def actions = producteurEnergieActionService.listByUser(user,
				new ProducteurEnergieActionCommand(sort: 'nbaction', order: 'desc'),
				[max: 3])

		render(template: 'widget/investissement', model: [totalAction: totalAction,
			totalSurface: totalSurface, totalInvestissement: totalInvestissement,
			actions: actions])
	}


	/**
	 * Graphique production solaire
	 * 
	 * @param command
	 * @return
	 */
	def productionChart(ProductionChartCommand command) {
		def user = authenticatedUser // inject plugin spring security
		def chart = producteurEnergieActionService.chartProduction(user, command)
		render(view: 'productionChart', model: [command: command, chart: chart])
	}


	/**
	 * Graphique répartition investissement
	 * 
	 * @param command
	 * @return
	 */
	def investissementChart() {
		def user = authenticatedUser // inject plugin spring security
		def actions = producteurEnergieActionService.listByUser(user,
				new ProducteurEnergieActionCommand(),
				[:])
		render(view: 'investissementChart', model: [actions: actions])
	}
}
