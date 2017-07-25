function onLoadWorkflows() {
	$(document).on('click', '#diagram-button', function(event) {
		ajaxGet($(this), 'data-url', {}, '#ajaxDialog', function(data) {
			AJS.dialog2('#diagram-workflow-dialog').show();
		});
	});
}
