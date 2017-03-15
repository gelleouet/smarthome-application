function onChangeMode(event) {
	ajaxSubmitForm($(event.target), 'data-url', '#change-mode-form', '#ajaxHouseModeChange');
}

