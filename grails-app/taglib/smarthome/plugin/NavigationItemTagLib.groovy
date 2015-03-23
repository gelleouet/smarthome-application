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

	//static defaultEncodeAs = [taglib:'html']

	// les tags renvoyant directement une valeur pouvant être utilisé comme fonction
	static returnObjectForTags = [
		'isCurrentItem',
		'isChildCurrentItem',
		'currentItem'
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
		attrs.item.controller == controllerName
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
	
}
