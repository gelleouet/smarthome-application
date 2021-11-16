/**
 * Formattage des tables métier
 */
function formatDataTable() {
	/*
	$('.app-datatable').each(function(index) {
		var $element = $(this)
		var isPaginateClient = $element.attr('data-client-pagination') == 'true' 
		var paginateInfo = ""
			
		if (isPaginateClient) {
			paginateInfo = "_START_ à _END_ sur _TOTAL_"
		} else if ($element.attr('paginatetotal') > 0 && $element.attr('paginatedebut') > 0) {
			paginateInfo = $element.attr('paginatedebut') + ' à ' + $element.attr('paginatefin') + ' sur ' + $element.attr('paginatetotal');
		}
			
		$element.DataTable({
			'destroy': true,
			'lengthMenu': 50,
			'paging': isPaginateClient,
			'lengthChange': false,
			'searching': false,
			'pageLength': 25,
			'order': [],
			'language': {
			    emptyTable: 'Aucune donnée trouvée',
			    info : paginateInfo,
			    paginate: {
			    	first: 'Premier',
			    	last: 'Dernier',
			    	previous: 'Précédent',
			    	next: 'Suivant'
			    }
			  }
		}).on('order.dt', function(event, settings, orderArray) {
			var $table = $(event.target)
			
			console.log(event)
			console.log(orderArray)
			
			// ne traite que les tables associées à un formulaire
			if ($table.attr('data-form-id')) {
				var idx = 0
				var datasEx = []
				
				orderArray.forEach(function(order) {
					// récupère l'élément th avec les infos de tri grâce 
					// au numéro de colonne
					var $th = $table.find("thead th:nth-child(" + (order.col + 1) + ")")
					
					if ($th.attr("data-sort-property")) {
						$th.attr("data-sort-property").split(",").forEach(function(token) {
							datasEx.push({name: "sortProperties[" + idx + "].property", value: token})
							datasEx.push({name: "sortProperties[" + idx + "].order", value: order.dir})
							idx++
						})
					}
				})
				
				// ne pas déclencher le tri si au moins une colonne n'a pas été paramétrée 
				// avec data-sort-property
				if (datasEx.length) {
					// c'est le formulaire qu'il faut poster. Pour l'appel Ajax, on se
					// branche sur lui avec son action
					var formId = '#' + $table.attr('data-form-id')
					
					// suppression des champs sortProperties
					$(formId).find("input[name^='sortProperties']").remove()
					
					if ($table.attr('data-rerender')) {
						ajaxSubmitFormEx(formId, 'action', formId, datasEx, '#' + $table.attr('data-rerender'))
					} else {
						submitForm(formId, datasEx)
					}
				}
			}
		})
	}) 
	*/
	
	// gère les clics des colonnes triables
	// le statut des colonnes est stocké dans le formulaire
	$('table[data-form-id] thead tr th[data-sort-property]').off('click')
	$('table[data-form-id] thead tr th[data-sort-property]').on('click', function(event) {
		var $th = $(event.target)
		var $table = $th.parents('table')
		var formId = '#' + $table.attr('data-form-id')
		var $form = $(formId)
		var properties = $th.attr('data-sort-property')
		var fieldArray = []
		
		// on charge les tris actuels dans une liste pour les traiter plus facilement
		$form.find("input[name^='sortProperties']").each(function(index, element) {
			var $field = $(element)
			
			if ($field.attr('name').endsWith('property')) {
				// recherche de l'autre champ contenant le sens du tri
				var $orderField = $form.find("input[name='" + $field.attr('name').replace('property', 'order') + "']")
				fieldArray.push({property: $field.val(), order: $orderField.val()})
			}
		})
		
		// si la touche shift est utilisée, cela veut dire qu'on doit ajouter le tri
		// à la liste en cours. sinon on doit reseter le tri et appliquer uniquement
		// le tri de la colonne cliquée
		// on supprime tout sauf la propriété de la colonne car si déjà présent,
		// il faut l'inverser
		if (!event.shiftKey) {
			fieldArray = fieldArray.filter(function(sort) {
				return properties.indexOf(sort.property) != -1
			})
		}
		
		// pour chaque tri à ajouter, on vérifie d'abord si présent. Si oui, on
		// inverse le tri, sinon on l'ajoute à la fin de la liste
		properties.split(",").forEach(function(token) {
			var existSort = fieldArray.find(function(sort) {
				return sort.property == token
			})
			
			if (existSort) {
				existSort.order = (existSort.order == 'asc' ? 'desc' : 'asc')
			} else {
				fieldArray.push({property: token, order: 'asc'})
			}
		})
		
		// mise à jour du formulaire avec les les propriétés du tri
		// pour faire simple, on supprime tout et on rajoute la liste
		$form.find("input[name^='sortProperties']").remove()
		
		fieldArray.forEach(function(sort, idx) {
			$form.append("<input type='hidden' name='sortProperties[" + idx + "].property' value='" + sort.property + "'/>")
			$form.append("<input type='hidden' name='sortProperties[" + idx + "].order' value='" + sort.order + "'/>")
		})
		
		// post le formulaire en mode normal ou ajax
		// en ajax, on branche l'événement sur le formulaire car c'est lui qui a l'url
		// pour basculer il faut englober la table dans un div[ajax] comme pour la pagination
		var $divAjax = $table.parents('div[ajax="true"][id]')
		
		if ($divAjax.length) {
			ajaxSubmitForm(formId, 'action', formId, '#' + $divAjax.attr('id'))
		} else {
			$form.submit()
		}
	})
	
	// englobage dans un un li
	$('ul.pagination a:not([aria-controls])').addClass('page-link').wrap('<li class="page-item"></li>')
	
	// remplace le bouton en cours
	$('ul.pagination span.currentStep').wrap('<li class="page-item active"></li>')
	$('ul.pagination span.currentStep').contents().unwrap().wrap('<a class="page-link"></a>');
}