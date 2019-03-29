package smarthome.core

import smarthome.core.AbstractController;

import org.springframework.security.access.annotation.Secured;


@Secured("isAuthenticated()")
class WidgetController extends AbstractController {

	WidgetService widgetService
	
	
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
	 * @return
	 */
	def addWidgetUser(Widget widget) {
		widgetService.addWidgetUser(widget, principal.id)
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
