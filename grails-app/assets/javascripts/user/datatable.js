/**
 * Formattage des tables métier
 */
function formatDataTable() {
	$('.app-datatable').each(function(index) {
		var $element = $(this)
		
		$element.DataTable({
			'destroy': true,
			'lengthMenu': 50,
			'paging': $element.attr('data-client-pagination') == 'true',
			'lengthChange': false,
			'searching': false,
			'pageLength': 25,
			'order': [],
			'language': {
			    'emptyTable': 'Aucune donnée trouvée'
			  }
		});
	}) 
	
	// réécriture des infos de pagination liées à chaque table
	$('.dataTables_info').each(function() {
		var datable = $(this).siblings('table').first();
		var msgInfo = '';
		
		if (datable.attr('paginatetotal') > 0 && datable.attr('paginatedebut') > 0) {
			msgInfo = datable.attr('paginatedebut') + ' à ' + datable.attr('paginatefin') + ' sur ' + datable.attr('paginatetotal');
		}
		
		$(this).html(msgInfo);
	});
}