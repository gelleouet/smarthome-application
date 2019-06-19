package smarthome.core

class ComposantVueTagLib {
	static namespace = "app"

	// les tags renvoyant directement une valeur pouvant être utilisé comme fonction
	static returnObjectForTags = ['stateDataUser']

	ComposantVueService composantVueService


	/**
	 * Renvoit la valeur d'une data d'un composant pour l'utilisateur connecté
	 * 
	 * @attr name REQUIRED le nom du composant
	 * @attr page REQUIRED le nom du composant
	 * 
	 */
	def stateDataUser = { attrs, body ->
		ComposantVue state = composantVueService.findPrincipal(attrs.name, attrs.page)
		return state?.data
	}


	/**
	 * Insère les attributs HTML pour la sauvegarde de l'état
	 * 
	 */
	def stateInsertAttr = { attrs, body ->
		def url = createLink(controller: 'composantVue', action: 'saveData')
		out << " state-url=${url} "
	}
}
