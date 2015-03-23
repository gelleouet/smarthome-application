package smarthome.plugin

import java.lang.reflect.Method

import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.GrailsClass

import smarthome.plugin.NavigableAction;
import smarthome.plugin.NavigableActions;
import smarthome.plugin.NavigationItem;

/**
 * Classe utilitaire pour 
 * @author gregory
 *
 */
class NavigationItemUtils {

	private static final log = LogFactory.getLog(this)

	/**
	 * Scanne tous les controlleurs à la recherche de controller 'Plugable'
	 * et les classe par section
	 * 
	 * @return
	 */
	static Map<String, NavigationItem> findNavigationItem(GrailsApplication grailsApplication) {
		log.info "Searching navigable controller..."
		def sections = [:]

		// Scanne tous les controllers
		for (controller in grailsApplication.controllerClasses) {
			// Scanne toutes les méthodes avec annotation NavigableAction ou NavigableActions
			// on ne regarde que dans les méthodes de la classe
			controller.clazz.getDeclaredMethods().findAll { method ->
				method.getAnnotation(NavigableAction) || method.getAnnotation(NavigableActions)
			}.each { method ->
				addControllerAction(controller, method, sections)
			}
		}

		// on ajoute le buffer des controller dans le contexte application
		grailsApplication.metaClass.navigationItems = sections

		log.info "Finding ${sections.size()} section of navigable controller."
		return sections
	}


	/**
	 * Recherche un item enfant avec le nom du controller à partir d'un item parent. PArcourt toute la grappe parent -> enfants
	 * 
	 * @param itemParent
	 * @param controller
	 * @param action
	 * @return
	 */
	static NavigationItem findChildByControllerAction(NavigationItem itemParent, String controllerName) {
		if (itemParent.controller == controllerName) {
			return itemParent
		} else {
			for (item in itemParent.subitems) {
				if (findChildByControllerAction(item, controllerName)) {
					return item
				}
			}

			return null
		}
	}


	/**
	 * Ajout d'une action dans le buffer des sections et renvoit le PluginItem associé
	 *
	 * @param controller
	 * @param method
	 * @param sections
	 * @param navigation
	 * @return
	 */
	static addControllerAction(GrailsClass controller, Method method, Map sections, NavigableAction navigation) {
		// on ajoute l'action dans la map indexée sur le nom de la section
		if (! sections[navigation.navigation().toString()]) {
			sections[navigation.navigation().toString()] = new NavigationItem(label: navigation.navigation().toString())
		}

		// en fonction du breadcrumb, on créé un simple item ou alors on l'imbrique dans item existant
		NavigationItem item = buildBreadcrumb(controller, method, navigation, sections[navigation.navigation().toString()], 0)

		// on complète l'item avec les infos de navigation
		if (item) {
			item.action = method.name
			item.controller = controller.logicalPropertyName
			item.style = navigation.style()
			item.tooltip = navigation.tooltip() ?: navigation.label()
			item.defaultGroup = navigation.defaultGroup()
			item.useForm = navigation.useForm()
			item.header = navigation.header()
			item.security = '' // TODO from @Secured
		}
	}


	/**
	 * Ajout d'une action dans le buffer des sections et renvoit le PluginItem associé
	 * 
	 * @param controller
	 * @param method
	 * @param sections
	 * @return
	 */
	static addControllerAction(GrailsClass controller, Method method, Map sections) {
		NavigableActions navigations = method.getAnnotation(NavigableActions)

		if (navigations) {
			for (navigation in navigations.value()) {
				addControllerAction (controller, method, sections, navigation)
			}
		} else {
			addControllerAction (controller, method, sections,  method.getAnnotation(NavigableAction))
		}
	}



	/**
	 * Construit l'arborescence du breadcrumb dans le buffer des navigations items
	 * Retourne l'élément inséré déjà rattaché à son parent si élément enfant
	 * 
	 * @param navigation
	 * @param items
	 * @param levelBreadcrumb
	 * @return
	 */
	private static NavigationItem buildBreadcrumb(GrailsClass controller, Method method, NavigableAction navigation, NavigationItem rootItem, 
		int levelBreadcrumb) {
		if (navigation.breadcrumb() && levelBreadcrumb < navigation.breadcrumb().length && navigation.breadcrumb()[levelBreadcrumb] != "") {
			def nameItem = navigation.breadcrumb()[levelBreadcrumb]

			// recherche d'un élément existant
			def itemParent = rootItem.subitems.find { item ->
				item.label == nameItem
			}

			// l'élément n'existe pas, on le créé ajoute dans la liste et on continue au niveau inférieur
			if (!itemParent) {
				itemParent = new NavigationItem(label: nameItem, parent: rootItem)
				rootItem.subitems << itemParent
			}

			return buildBreadcrumb(controller, method, navigation, itemParent, levelBreadcrumb + 1)
		} else {
			// on vérifie qu'un controlleur identique (nom et action n'existe pas déjà)
			def existItem = rootItem.subitems.find { item ->
				item.controller == controller.logicalPropertyName && item.action == method.name
			}
			
			if (!existItem) {
				// dernier niveau ou pas de niveau du tout : on ajoute un nouveau item dans la liste
				NavigationItem item = new NavigationItem(label: navigation.label(), parent: rootItem)
				rootItem.subitems << item
				return item
			} else {
				return null
			}
		}
	}

}
