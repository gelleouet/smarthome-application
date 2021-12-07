function onLoadCompteur() {
	$(document).on('click', '#compteur-index-valid-all-button', function(event) {
		if (confirm('Voulez-vous tout valider ?')) {
			ajaxSubmitForm($(this), 'data-url', '#compteurIndex-form', '', function() {
				$('#compteurIndex-submit-button').click()
			})
		}
	});
	
	$(document).on('click', '#compteur-index-delete-all-button', function(event) {
		if (confirm('Voulez-vous tout supprimer ?')) {
			ajaxSubmitForm($(this), 'data-url', '#compteurIndex-form', '', function() {
				$('#compteurIndex-submit-button').click()
			})
		}
	});
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
			// rafraichit la table
			$('#index-historique-submit-button').click()
		})
	});
	
	$(document).on('click', '#cancel-compteur-index-dialog-button', function(event) {
		hideDialog('compteur-index-dialog')
	});
	
	$(document).on('click', '#compteur-index-delete-button', function(event) {
		if (confirm("Voulez-vous supprimer l'index ?")) {
			ajaxGet($(this), 'data-url', {}, '', function() {
				// rafraichit la table
				$('#index-historique-submit-button').click()
			})
		}
	});
}