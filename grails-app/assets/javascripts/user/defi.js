function onLoadEditDefi() {
	$(document).on('click', '#calculer-resultat-defi-button', function(event) {
		if (confirm('Voulez-vous continuer ?')) {
			ajaxGet($(this), 'data-url', {}, '#participant-defi-div')
		}
	});
	
	$(document).on('click', '#effacer-resultat-defi-button', function(event) {
		if (confirm('Voulez-vous continuer ?')) {
			ajaxGet($(this), 'data-url', {}, '#participant-defi-div')
		}
	});
	
	$(document).on('click', '#delete-participant-button', function(event) {
		if (confirm('Voulez-vous continuer ?')) {
			ajaxGet($(this), 'data-url', {}, '#participant-defi-div')
		}
	});
	
	$(document).on('click', '#defi-equipe-button', function(event) {
		ajaxGet($(this), 'data-url', {}, '#ajaxDialog', function(data) {
			showDialog('defi-equipe-dialog')
		});
	});
	
	$(document).on('click', '#defi-participant-button', function(event) {
		ajaxGet($(this), 'data-url', {}, '#ajaxDialog', function(data) {
			showDialog('defi-participant-dialog')
		});
	});
	
	$(document).on('click', '#ok-defi-equipe-dialog-button', function(event) {
		ajaxSubmitForm($(this), 'data-url', '#defi-equipe-dialog-form', '#participant-defi-div', function() {
			hideDialog('defi-equipe-dialog')
		})
	});
	
	$(document).on('click', '#ok-defi-participant-dialog-button', function(event) {
		ajaxSubmitForm($(this), 'data-url', '#defi-participant-dialog-form', '#participant-defi-div', function() {
			hideDialog('defi-participant-dialog')
		})
	});
	
	$(document).on('click', '#cancel-defi-equipe-dialog-button', function(event) {
		hideDialog('defi-equipe-dialog')
	});
	
	$(document).on('click', '#cancel-defi-participant-dialog-button', function(event) {
		hideDialog('defi-participant-dialog')
	});
}

