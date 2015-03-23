package smarthome.core

class DataTableTagLib {
	static namespace = "app"


	/**
	 * Render une table avec le plugin JQuery DataTable http://datatables.net/
	 * Si une url est passée en paramètre, alors les données sont chargées depuis cette URL (au format JSON).
	 * Sinon l'élément DOM est directement transformé avec les données qu'il contient déjà
	 * Rajoute un élément de pagination
	 * 
	 * @attr datatableId REQUIRED l'ID de l'élément table à transformer
	 * @attr recordsTotal REQUIRED le nombre total d'items
	 * @attr cssClass par défaut aui
	 * @attr cssStyle
	 */
	def datatable = { attrs, body ->
		def paginateDebut = 0
		def paginateFin = 0
		
		if (attrs.recordsTotal > 0) {
			def offset =  params.int('offset')
			def max = params.int('max')
			
			if (offset != null && max != null) {
				paginateDebut = offset + 1
				paginateFin = Math.min(attrs.recordsTotal, offset + max)
			}
		}
		
		// le body contient déjà la structure de la table sauf la balise <table>
		out << "<table class=\"${attrs.cssClass ?: 'aui'} app-datatable\" id=\"$attrs.datatableId\" style=\"${attrs.cssStyle ?: ''}\" paginateTotal=\"${ attrs.recordsTotal }\" paginateDebut=\"${ paginateDebut }\" paginateFin=\"${ paginateFin }\">"
		out << body()
		out << "</table>"

		// on rajoute juste après le code javascript pour transformer la table
		out << render(template: '/templates/datatable', model: attrs)
	}
}
