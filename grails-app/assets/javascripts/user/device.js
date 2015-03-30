$(window).on('load', function() {
	$(document).on('change', '#deviceType\\.id', function() {
		var url = $(this).attr('metadata-url');
		var datas = {
				id:  $(this).find('option:selected').val()
		}
		
		ajaxRerender(url, datas, '#deviceMetadatas')
	});
});






