function closeNotificationAccountDialog() {
	AJS.dialog2('#notification-account-dialog').hide();
}

function showNotificationAccountDialog() {
	AJS.dialog2('#notification-account-dialog').show();
}


function onNotificationAccountSenderChange(select) {
	ajaxSubmitForm(select, 'data-url', '#notificationAccount-form', '#ajaxNotificationSenderForm')
}

