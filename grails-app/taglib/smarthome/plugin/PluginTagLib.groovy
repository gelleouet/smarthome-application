package smarthome.plugin

import grails.converters.JSON;

/**
 * Librairie tags GSP pour l'utilisation des plugins
 * 
 * @author gregory
 *
 */
class PluginTagLib {
	static namespace = 'app'

	//static defaultEncodeAs = [taglib:'html']

	// les tags renvoyant directement une valeur pouvant être utilisé comme fonction
	static returnObjectForTags = [
		
	]


	/**
	 * Affiche tous les plugins VIEW associés à une clé. Les plugins VIEW sont des controllers qui renvoient du code HTML.
	 * Ils doivent s'enregistrer avec l'annotation NavigableAction dans la catégorie "viewPlugin" avec 
	 * le breadcrumb = controllerName.actionName.section
	 *  
	 * Chaque plugin sera affiché dans un tab à part
	 *
	 * @attr section REQUIRED la section où affiché les plugins
	 * @attr command l'object command à passer en paramètre dans le cas d'un formulaire
	 */
	def renderViewPlugins = { attrs, body ->
		def key = controllerName + "." + actionName + "." + attrs.section
		def bodyContent = body()
		
		// Recherche des plugins liés à cette clé
		def plugins = grailsApplication.navigationItems['viewPlugin']?.subitems?.find { item ->
			item.label == key
		}
		
		// si un contenu est présent, il doit être affiché même si pas de plugins
		if ((plugins && plugins.subitems) || bodyContent) {
			def newParams = params
			// on enregistre l'action et le controller source de l'appel
			newParams.srcController = controllerName
			newParams.srcAction = actionName
			newParams.command = attrs.command
			newParams.commandJson = attrs.command as JSON
			
			out << render(template: '/plugin/tabsViewPlugin', model: [plugins: plugins?.subitems, bodyContent: bodyContent], params: newParams)
		}		
	}
	
}
