function onLoadWorkflows() {
	$(document).on('click', '#diagram-button', function(event) {
		ajaxGet($(this), 'data-url', {}, '#ajaxDialog', function(data) {
			showDialog('diagram-workflow-dialog')
		});
	});
}
