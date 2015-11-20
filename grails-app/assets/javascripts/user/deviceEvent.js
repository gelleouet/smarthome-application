
$(window).on('load', function() {
	$(document).on('click', '#delete-trigger-button', function() {
		if (confirm('Voulez-vous continuer ?')) {
			ajaxSubmitForm($(this), 'data-url', '#device-event-form', '#eventTriggers');
		}
	});
});


$(window).on('load', function() {
	$(document).on('change', '#triggered-device-select', function() {
		ajaxSubmitForm($(this), 'data-url', '#device-event-form', '#eventTriggers');
	});
});

