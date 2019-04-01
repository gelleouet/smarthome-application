package smarthome.automation

import smarthome.core.AbstractController;
import smarthome.core.ChartUtils;
import smarthome.core.DateUtils;
import smarthome.core.ExceptionNavigationHandler;
import smarthome.core.QueryUtils;
import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigationEnum;

import org.springframework.security.access.annotation.Secured;


@Secured("isAuthenticated()")
class ChartController extends AbstractController {

    private static final String COMMAND_NAME = 'chart'
	
	ChartService chartService
	
	
	/**
	 * Affichage en grille
	 *
	 * @return
	 */
	def chartsGrid(ChartCommand command) {
		command.navigation()
		def groupes = chartService.listGroupes(principal.id)
		// sélection du 1er groupe si rien de préciser
		if (!command.groupe && groupes) {
			command.groupe = groupes[0]
		}
		def charts = chartService.listByUser(command, principal.id, [:])
		def recordsTotal = charts.totalCount

		// charts est accessible depuis le model avec la variable chart[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond charts, model: [recordsTotal: recordsTotal, command: command, groupes: groupes]
	}
	
	
	/**
	 * Edition
	 *
	 * @param chart
	 * @return
	 */
	def edit(Chart chart) {
		def editChart = parseFlashCommand(COMMAND_NAME, chart)
		editChart = chartService.edit(editChart)
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editChart]))
	}
	
	
	/**
	 * Rendu des datas du graphique
	 * 
	 * @param chart
	 * @param sinceHour
	 * @return
	 */
	def chartDatas(ChartCommand command) {
		chartService.edit(command.chart)
		def datas = chartService.values(command)
		render(template: '/chart/datas/chartMultipleDatas', model: [command: command, datas: datas])
	}

	/**
	 * Création
	 *
	 * @return
	 */
	def create() {
		def editChart = parseFlashCommand(COMMAND_NAME, new Chart())
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editChart]))
	}
	
	
	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	def fetchModelEdit(userModel) {
		def model = [:]
		
		// Compléter le model
		model.chartTypes = ChartTypeEnum.values()
		model.functions = DataModifierEnum.values()
		model.devices = Device.findAllByUser(authenticatedUser)
		
		// on remplit avec les infos du user
		model << userModel
		
		return model
	}
	
	
	/**
	 * Enregistrement modification
	 *
	 * @param chart
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "edit", modelName = ChartController.COMMAND_NAME)
	def saveEdit(Chart chart) {
		checkErrors(this, chart)
		chartService.save(chart)
		redirect(action: 'chartsGrid', params: [groupe: chart.groupe])
	}


	/**
	 * Enregistrement d'un nouveau
	 *
	 * @param user
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "create", modelName = ChartController.COMMAND_NAME)
	def saveCreate(Chart chart) {
		chart.user = authenticatedUser
		chart.validate() // important car les erreurs sont traitées lors du binding donc le chart.user sort en erreur
		checkErrors(this, chart)
		chartService.save(chart)
		redirect(action: 'chartsGrid', params: [groupe: chart.groupe])
	}
	
	
	/**
	 * Suppression d'un chart
	 *
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "charts", modelName = "")
	def delete(Chart chart) {
		chartService.delete(chart)
		redirect(action: 'chartsGrid', params: [groupe: chart.groupe])
	}
	
	
	/**
	 * Ajout d'un device sur le graph
	 * 
	 * @param chart
	 * @return
	 */
	def addDevice(Chart chart) {
		// on refait le bind manuellement pour l'association devices
		chart.devices = []
		bindData(chart, params)
		
		def model = fetchModelEdit([chart: chart])
		
		// ajout du device (vide)
		def device = new ChartDevice()
		def lastDevice = chart.devices.max { it.position }
		device.position = lastDevice ? lastDevice.position + 1 : 0
		
		if (model['devices'].size()) {
			device.device = model['devices'][0]
		}
		
		chart.devices << device
		
		render(template: 'chartDevice', model: model)
	}
	
	
	/**
	 * Suppression d'un device
	 * 
	 * @return
	 */
	def deleteDevice(Chart chart, int position) {
		// on refait le bind manuellement pour l'association devices
		chart.devices = []
		bindData(chart, params)
		chart.devices.removeAll {
			it.position == position
		}
		
		def model = fetchModelEdit([chart: chart])
		
		render(template: 'chartDevice', model: model)
	}
	
	
	/**
	 * Rfresh device list
	 * 
	 * @param
	 * @return
	 */
	def refreshDevice(Chart chart) {
		// on refait le bind manuellement pour l'association devices
		chart.devices = []
		bindData(chart, params)
		def model = fetchModelEdit([chart: chart])
		render(template: 'chartDevice', model: model)
	}
	
	
	/**
	 * Widget chart
	 * 
	 * @param Ch
	 * @return
	 */
	def widgetChart(Chart chart) {
		ChartCommand command = new ChartCommand(chart: chart)
		def datas = chartService.values(command)
		render(template: 'widgetChart', model: [chart: chart, command: command, datas: datas])
	}
}
