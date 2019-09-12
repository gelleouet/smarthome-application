package smarthome.plugin

import smarthome.plugin.NavigationItemUtils;

/**
 * Librairie tags GSP pour l'utilisation des plugins
 * 
 * @author gregory
 *
 */
class NavigationItemTagLib {
	static namespace = 'app'

	/** Dependency injection for webInvocationPrivilegeEvaluator. */
	def webInvocationPrivilegeEvaluator
	
	/** Dependency injection for springSecurityService. */
	def springSecurityService
	
	//static defaultEncodeAs = [taglib:'html']

	// les tags renvoyant directement une valeur pouvant être utilisé comme fonction
	static returnObjectForTags = [
		'isCurrentItem',
		'isChildCurrentItem',
		'currentItem',
		'navigationItems'
	]


	/**
	 * Renvoit vrai si l'action en cours correspond à l'item passé en paramètre.
	 * Permet de rajouter par exemple un style pour sélectionner un menu correspond à l'action en cours
	 * 
	 * @attr item  REQUIRED un objet @see NavigationItem
	 * 
	 * @return true si item == action courante
	 */
	def isCurrentItem = { attrs, body ->
		attrs.item.controller == controllerName && attrs.item.action == actionName
	}


	/**
	 * Renvoit vrai si l'action en cours est un item enfant (ou le même) de l'item passé en paramètre
	 * Permet de rajouter par exemple un style pour sélectionner un menu correspond à l'action en cours
	 * 
	 * @attr item  REQUIRED un objet @see NavigationItem
	 * 
	 * @return true si item == parent action courante
	 */
	def isChildCurrentItem = { attrs, body ->
		NavigationItemUtils.findChildByControllerAction(attrs.item, controllerName) != null
	}
	
	
	/**
	 * Renvoie l'élément courant à partir de la liste globale
	 * 
	 * @attr item REQUIRED l'élément racine
	 * 
	 * @return @see NavigationItem ou null
	 */
	def currentItem = { attrs, body ->
		def item = NavigationItemUtils.findChildByControllerAction(attrs.item, controllerName)
		return item
	}
	
	
	/**
	 * Renvoit les menus de navigation en fonction des permissions de l'utilisateur
	 * 
	 * @attr category REQUIRED
	 */
	def navigationItems = { attrs, body ->
		def sections
		
		if (request.navigationItems != null) {
			sections = request.navigationItems
		} else {
			sections = [:]
	
			// Scanne tous les controllers
			for (controller in grailsApplication.controllerClasses) {
				// Scanne toutes les méthodes avec annotation NavigableAction ou NavigableActions
				// on ne regarde que dans les méthodes de la classe
				controller.clazz.getDeclaredMethods().findAll { method ->
					method.getAnnotation(NavigableAction) || method.getAnnotation(NavigableActions)
				}.each { method ->
					// on vérifie les permissions avant d'ajouter le menu
					if (hasAccess(controller.logicalPropertyName, method.name)) {
						NavigationItemUtils.addControllerAction(controller, method, sections)
					}
				}
			}
			
			// met le résultat dans la request pour d'autres appels
			request.navigationItems = sections
		}
		
		// renvoit les items de la catégory demandée
		return sections[(attrs.category)] ?: new NavigationItem()
	}
	
	
	/**
	 * Vérifie les droits d'accès à l'action de l'utilisateur connecté
	 * 
	 * @param controller
	 * @param action
	 * @return
	 */
	protected boolean hasAccess(String controller, String action) {
		def auth = springSecurityService.authentication
		def url = g.createLink(controller: controller, action: action, base: '/').toString()
		return webInvocationPrivilegeEvaluator.isAllowed(request.contextPath, url, 'GET', auth)
	}
	
}
