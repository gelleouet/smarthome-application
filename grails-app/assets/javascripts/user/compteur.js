function onLoadCompteur() {
	
}


function onLoadSaisieIndex() {
	$(document).on('click', '#show-compteur-index-dialog-button', function(event) {
		ajaxGet($(this), 'data-url', {}, '#ajaxDialog', function() {
			showDialog('compteur-index-dialog')
		})
	});
	
	$(document).on('click', '#ok-compteur-index-dialog-button', function(event) {
		ajaxSubmitForm($(this), 'data-url', '#compteur-index-dialog-form', '', function() {
			hideDialog('compteur-index-dialog')
		})
	});
	
	$(document).on('click', '#cancel-compteur-index-dialog-button', function(event) {
		hideDialog('compteur-index-dialog')
	});
}