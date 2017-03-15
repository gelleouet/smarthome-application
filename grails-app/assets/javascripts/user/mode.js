$(window).on('load', function() {
	$(document).on('click', '#add-mode-button', function() {
		ajaxSubmitForm($(this), 'data-url', '#profil-form', '#ajaxModes');
	});
	$(document).on('click', '#delete-mode-button', function() {
		ajaxSubmitForm($(this), 'data-url', '#profil-form', '#ajaxModes');
	});
});

