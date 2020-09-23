package smarthome.plugin

import smarthome.plugin.NavigationItem;

/**
 * Un item représentant un lien vers une ressource Web (associé à un controller) 
 * ou un menu imbriquant d'autres items
 * 
 * @author gregory
 *
 */
class NavigationItem implements Serializable {

	// le label à afficher
	String label

	// nom du controller (vide si groupe de menu)
	String controller

	// nom de l'action (vide si groupe de menu)
	String action

	// style css
	String style

	// description du lien
	String tooltip

	// utilisation d'un formulaire pour déclencher le controller
	// au lieu d'un lien simple
	boolean useForm

	// la liste des sous items si item groupe
	def subitems = []

	// item par défaut dans le groupe
	boolean defaultGroup

	// un header de groupe
	String header

	// Expression Spring Security pour l'accès à l'action
	boolean security
	
	// Référence item parent (pour faciliter la navigation montante dans l'arbre)
	NavigationItem parent
	
	boolean customMenuView
	
	// icon html
	String icon
	String iconLib
	
	String iconHeader
	String iconLibHeader

}
