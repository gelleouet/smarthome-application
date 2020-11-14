package smarthome.core

class IconTagLib {
	static namespace = "app"

	// les tags renvoyant directement une valeur pouvant être utilisé comme fonction
	static returnObjectForTags = []


	/**
	 * Insère les attributs HTML pour la sauvegarde de l'état
	 * 
	 * @attr name REQUIRED nom de l'icone à insérer
	 * @attr lib nom de la librairie (par défaut feather)
	 * @attr class style css
	 * @attr style style css
	 * @attr width
	 * @attr height
	 * 
	 */
	def icon = { attrs, body ->
		if (attrs.lib in ['awesome', 'fa']) {
			out << """<i class="${ attrs.class } fas fa-${ attrs.name }" style="${ attrs.style ?: ''}"
				width="${ attrs.width }" height="${ attrs.height }"></i>"""
		} else {
			out << """<i class="${ attrs.class ?: ''}" data-feather="${ attrs.name }" style="${ attrs.style ?: ''}"
				width="${ attrs.width }" height="${ attrs.height }"></i>"""
		}
	}
}
