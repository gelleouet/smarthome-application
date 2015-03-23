/**
 * Formattage des tables métier
 */
function formatDataTable() {
	$('.app-datatable').DataTable({
		'destroy': true,
		'lengthMenu': 50,
		'paging': false,
		'lengthChange': true,
		'searching': false,
		'order': [],
		'language': {
		    'emptyTable': 'Aucune donnée trouvée'
		  }
	});
	
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