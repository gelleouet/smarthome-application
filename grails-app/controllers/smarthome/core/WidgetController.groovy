package smarthome.core

import smarthome.automation.Device
import smarthome.core.AbstractController;
import smarthome.plugin.NavigableAction
import smarthome.plugin.NavigationEnum

import org.springframework.security.access.annotation.Secured;


@Secured("isAuthenticated()")
class WidgetController extends AbstractController {

	private static final String COMMAND_NAME = 'widget'
	
	WidgetService widgetService
	
	
	/**
	 * Retourne la liste des utilisateurs
	 *
	 * @return
	 */
	@Secured("hasRole('ROLE_ADMIN')")
	@NavigableAction(label = "Widgets", navigation = NavigationEnum.configuration, header = "Administrateur")
	def widgets(WidgetCommand command) {
		def widgets = widgetService.list(command, this.getPagination([:]))
		def recordsTotal = widgets.totalCount

		// users est accessible depuis le model avec la variable user[Instance]List
		// @see grails.scaffolding.templates.domainSuffix
		respond widgets, model: [recordsTotal: recordsTotal, command: command]
	}
	
	
	/**
	 * Edition
	 *
	 * @param widget
	 * @return
	 */
	@Secured("hasRole('ROLE_ADMIN')")
	def edit(Widget widget) {
		def editWidget = parseFlashCommand(COMMAND_NAME, widget)
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editWidget]))
	}


	/**
	 * Création
	 *
	 * @return
	 */
	@Secured("hasRole('ROLE_ADMIN')")
	def create() {
		def editWidget = parseFlashCommand(COMMAND_NAME, new Widget())
		render(view: COMMAND_NAME, model: fetchModelEdit([(COMMAND_NAME): editWidget]))
	}


	/**
	 * Prépare le model pour les ecrans de création et modification
	 *
	 * @return
	 */
	protected def fetchModelEdit(userModel) {
		def model = [:]

		// on remplit avec les infos du user
		model << userModel

		return model
	}


	/**
	 * Enregistrement modification
	 *
	 * @param device
	 * @return
	 */
	@Secured("hasRole('ROLE_ADMIN')")
	def saveEdit(Widget widget) {
		checkErrors(this, widget)
		widgetService.save(widget)
		redirect(action: 'widgets')
	}
	
	/**
	 * Déplace un widget sur l'écran
	 *
	 * @param widgetUser
	 * @param row
	 * @param col
	 * @return
	 */
	def moveWidgetUser(WidgetUser widgetUser, int row, int col) {
		widgetService.moveWidgetUser(widgetUser, row, col)
		nop()
	}
	
	
	/**
	 * Affichage dialogue ajout widget
	 *
	 */
	def dialogAddWidget() {
		def widgets = widgetService.list(new WidgetCommand(), this.getPagination([:]))
		render (template: 'dialogAddWidget', model: [widgets: widgets])
	}
	
	
	/**
	 * Template content dialog add widget
	 * 
	 * @param command
	 * @return
	 */
	def contentDialogAddWidget(WidgetCommand command) {
		def widgets = widgetService.list(command, this.getPagination([:]))
		render (template: 'contentDialogAddWidget', model: [widgets: widgets])
	}
	
	
	/**
	 * Ajout d'un widget à un utilisateur
	 *
	 * @param widget
	 * @param paramId
	 * @return
	 */
	def addWidgetUser(Widget widget, Long paramId) {
		widgetService.addWidgetUser(widget, paramId, principal.id)
		redirect(uri: '/')
	}
	
	
	/**
	 * Supprime un widget d'un utilisateur
	 *
	 * @param widget
	 * @return
	 */
	def removeWidgetUser(WidgetUser widgetUser) {
		widgetService.delete(widgetUser)
		redirect(uri: '/')
	}
}
