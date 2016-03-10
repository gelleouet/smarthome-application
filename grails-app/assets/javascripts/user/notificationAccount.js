function closeNotificationAccountDialog() {
	AJS.dialog2('#notification-account-dialog').hide();
}

function showNotificationAccountDialog() {
	AJS.dialog2('#notification-account-dialog').show();
}


function onNotificationAccountChange(select) {
	var datas = {
		className: select.value
	}
	ajaxGet(select, 'data-url', datas, '#ajaxNotificationSenderForm');
}

