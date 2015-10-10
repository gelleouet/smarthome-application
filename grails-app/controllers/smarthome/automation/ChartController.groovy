package smarthome.automation



import smarthome.core.AbstractController;
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
	 * Affichage paginé avec fonction recherche
	 *
	 * @return
	 */
	@NavigableAction(label = "Graphiques", navigation = NavigationEnum.configuration, header = "Graphiques", breadcrumb = [
		NavigableAction.CONFIG_APPLICATION,
		"Domotique"
	])
	def charts(String chartSearch) {
		int recordsTotal
		def search = QueryUtils.decorateMatchAll(chartSearch)
		def userId = principal.id

		def charts = Chart.createCriteria().list(this.getPagination([:])) {
			user {
				idEq(userId)
			}
			
			if (chartSearch) {
				ilike 'label', search
			}
		}
		recordsTotal = charts.totalCount

		// charts est accessible depuis le model avec la variable chart[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond charts, model: [recordsTotal: recordsTotal, chartSearch: chartSearch]
	}
	
	
	/**
	 * Affichage en grille
	 *
	 * @return
	 */
	@NavigableAction(label = "Graphiques", navigation = NavigationEnum.navbarPrimary)
	def chartsGrid(String chartSearch) {
		int recordsTotal
		def search = QueryUtils.decorateMatchAll(chartSearch)
		def userId = principal.id

		def charts = Chart.createCriteria().list(this.getPagination([:])) {
			user {
				idEq(userId)
			}
			
			if (chartSearch) {
				ilike 'label', search
			}
		}
		recordsTotal = charts.totalCount

		// charts est accessible depuis le model avec la variable chart[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond charts, model: [recordsTotal: recordsTotal, chartSearch: chartSearch]
	}
	
	
	/**
	 * Edition
	 *
	 * @param chart
	 * @return
	 */
	def edit(Chart chart) {
		def editChart = parseFlashCommand(COMMAND_NAME, chart)
		this.preAuthorize(chart)
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editChart]))
	}
	
	
	/**
	 * Construction d'un graphique
	 *
	 * @param chart
	 * @return
	 */
	
	def chartView(Chart chart, Long sinceHour) {
		this.preAuthorize(chart)
		def timesAgo = [1: '1 heure', 6: '6 heures', 12: '12 heures', 24: '24 heures']
		sinceHour = sinceHour ?: 1
		def datas = chartService.values(chart, sinceHour = sinceHour ?: 1)
		render(view: 'chartView', model: [chart: chart, timesAgo: timesAgo, sinceHour: sinceHour, datas: datas,
			chartType: ChartTypeEnum.valueOf(chart.chartType)])
	}
	
	
	/**
	 * Rendu du graphique sans les menus autour
	 * 
	 * @param chart
	 * @param sinceHour
	 * @return
	 */
	def chartPreview(Chart chart, Long sinceHour) {
		this.preAuthorize(chart)
		def datas = chartService.values(chart, sinceHour = sinceHour ?: 1) 
		render(template: '/chart/chartDatasMap', model: [label: chart.label, datas: datas])
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
		this.preAuthorize(chart)
		checkErrors(this, chart)
		chartService.save(chart)
		redirect(action: COMMAND_NAME + 's')
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
		redirect(action: COMMAND_NAME + 's')
	}
	
	
	/**
	 * Suppression d'un chart
	 *
	 * @return
	 */
	@ExceptionNavigationHandler(actionName = "charts", modelName = "")
	def delete(Chart chart) {
		this.preAuthorize(chart)
		chartService.delete(chart)
		redirect(action: 'charts')
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
}