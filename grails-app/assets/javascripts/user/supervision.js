function onLoadSupervision() {
	
	$(document).on('click', '#supervision-export-dialog-button', function(event) {
    	ajaxSubmitForm($(this), 'data-url', '#supervision-form', '#ajaxDialog', function(data) {
    		showDialog("supervision-export-dialog")
    	});
    })
}
