$(window).on('load', function() {
	$(document).on('change', '#deviceType\\.id', function() {
		var url = $(this).attr('metadata-url');
		var datas = {
				id:  $(this).find('option:selected').val()
		}
		
		ajaxRerender(url, datas, '#deviceMetadatas')
	});
});


$(window).on('load', function() {
	$(document).on('click', '#delete-event-button', function() {
		if (confirm('Voulez-vous continuer ?')) {
			ajaxSubmitForm($(this), 'data-url', '#device-form', '#deviceEvents');
		}
	});
});


$(window).on('load', function() {
	$(document).on('click', '#commit-metadata-button', function() {
		if (confirm('Voulez-vous changer la configuration sur le périphérique ?')) {
			ajaxSubmitForm($(this), 'data-url', '#device-form');
		}
	});
});


$(window).on('load', function() {
	$(document).on('click', '.ajax-invoke-action-button', function() {
		if (confirm('Voulez-vous continuer ?')) {
			var form = $('#' + $(this).attr('data-form-id'))
			var input = form.find('#actionName')
			var actionName = $(this).attr('data-action-name')
			input.val(actionName)
			ajaxSubmitForm($(this), 'data-url', form);
		}
	});
});



$(window).on('load', function() {
	$(document).on('change', '#triggered-device-select', function() {
		ajaxSubmitForm($(this), 'data-url', '#device-form', '#deviceEvents');
	});
});






