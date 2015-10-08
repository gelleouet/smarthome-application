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
	$(document).on('change', '#triggered-device-select', function() {
		ajaxSubmitForm($(this), 'data-url', '#device-form', '#deviceEvents');
	});
});






