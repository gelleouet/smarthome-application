

/**
 * Réduit ou affiche le panel sidebar
 * Enregistre l'état du composant pour l'utilisateur connecté
 */
$(document).on('click', '#aside-hide', function () {
	$('.page-panel-sidebar').toggleClass('page-panel-sidebar-slide');
	
	if ($('.page-panel-sidebar').hasClass('page-panel-sidebar-slide')) {
		$('.page-panel-sidebar div').hide();
	} else {
		$('.page-panel-sidebar div').show();
	}
	
	//sauvegarde l'état du composant
	saveStateUser('page-panel-sidebar', 'class',
			$('.page-panel-sidebar').hasClass('page-panel-sidebar-slide') ? 'page-panel-sidebar-slide' : '');
});



/**
 * Enregistre l'état d'un composant
 * 
 * @param url
 * @param name
 * @param dataName
 * @param dataValue
 */
function saveStateUser(name, dataName, dataValue) {
	var statePage = $('#content[role=main]').attr('state-page');
	var stateUrl = $('#content[role=main]').attr('state-url');
	
	jQuery.ajax({
		type: 'POST',
		url: stateUrl,
		data: {
			name: name,
			page: statePage,
			dataName: dataName,
			dataValue: dataValue
		}
	});
}



/**
 * Gestion personnalisation des widgets
 */
$(window).on('load', function() {
	$('.widget-panel').sortable({
		connectWith: '.widget-panel',
		handle: '.widget-header',
		placeholder: "widget-placeholder",
		
		stop: function(event, ui) {
			console.log(ui.item.attr('id'));
		},
	});
});




