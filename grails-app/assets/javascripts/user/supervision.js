function onLoadSupervision() {
	$(document).on('click', '#supervision-export-dialog-button', function(event) {
		$('#supervision-export-dialog-form').submit()
	})
	
	$(document).on('click', '#supervision-export-button', function(event) {
		ajaxSubmitForm($(this), 'data-url', '#supervision-form', '#ajaxDialog', function() {
			showDialog('supervision-export-dialog')
		})
	})
}

