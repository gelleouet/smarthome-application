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
	 * @attr cssClass
	 * @attr cssStyle
	 * @attr paginateForm
	 * @attr offset
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
		
		attrs.paginateDebut = paginateDebut
		attrs.paginateFin = paginateFin
		
		// le body contient déjà la structure de la table sauf la balise <table>
		out << "<div class=\"table-responsive-lg\">"
		out << "<table class=\"${attrs.cssClass ?: 'table table-hover'} dataTable\" id=\"$attrs.datatableId\" style=\"${attrs.cssStyle ?: ''}\" data-form-id=\"${ attrs.paginateForm ?: '' }\" paginateTotal=\"${ attrs.recordsTotal }\" paginateDebut=\"${ paginateDebut }\" paginateFin=\"${ paginateFin }\">"
		out << body()
		out << "</table>"
		out << "</div>"

		// on rajoute juste après le code javascript pour transformer la table
		out << render(template: '/templates/datatable', model: attrs)
	}
	
	
	/**
	 * Ajout d'une cellule pour un entête avec gestion des tris
	 * 
	 * @attrs cssClass
	 * @attrs cssStyle
	 * @attrs sortProperty
	 * @attrs sortProperties
	 * @attrs orderable
	 * 
	 */
	def th = { attrs, body ->
		List cssClasses = []
		
		if (attrs.cssClass) {
			cssClasses << attrs.cssClass
		}
		
		// le body contient le contenu de la cellule
		out << "<th"
		if (attrs.sortProperty) {
			out << """ data-sort-property="${ attrs.sortProperty }" """

			// si un tri est configuré sur la colonne et que celui est activé,
			// on ajuste les statuts de la celule pour que sa mise en forme corresponde
			// à l'état actuel
			if (attrs.sortProperties) {
				SortCommand sortActive = PaginableCommand.findSortProperty(attrs.sortProperty, attrs.sortProperties)
				
				if (sortActive) {
					cssClasses << "sorting_" + sortActive.order
				} else {
					cssClasses << "sorting"
				}
			} else {
				cssClasses << "sorting"
			}
		}
		if (attrs.orderable != null) {
			out << """ data-orderable="${ attrs.orderable }" """
			
			if (attrs.orderable && (!cssClasses.contains("sorting_asc") || !cssClasses.contains("sorting_desc"))) {
				cssClasses << "sorting"
			}
		}
		if (cssClasses) {
			out << """ class="${ cssClasses.unique().join(' ') }" """
		}
		if (attrs.cssStyle) {
			out << """ class="${ attrs.cssStyle }" """
		}
		out << ">"
		out << body()
		out << "</th>"
	}
}
