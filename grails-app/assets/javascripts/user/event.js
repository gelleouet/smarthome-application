function onLoadEventEdit() {
	$(document).on('click', '#delete-trigger-button', function() {
		if (confirm('Voulez-vous continuer ?')) {
			ajaxSubmitForm($(this), 'data-url', '#device-event-form', '#eventTriggers');
		}
	});
}

function onChangeEventTriggerDomainClassName(select) {
	ajaxSubmitForm(select, 'data-url', '#device-event-form', '#eventTriggers');
}


function onChangeEventTriggerDomainId(select) {
	ajaxSubmitForm(select, 'data-url', '#device-event-form', '#eventTriggers');
}


function onChangeEventTriggerActionName(select) {
	ajaxSubmitForm(select, 'data-url', '#device-event-form', '#eventTriggers');
}


function closeNotificationDialog() {
	AJS.dialog2('#notification-dialog').hide();
}

function showNotificationDialog() {
	AJS.dialog2('#notification-dialog').show();
}

