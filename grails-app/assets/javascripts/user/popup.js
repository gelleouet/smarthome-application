$(window).on('load', function() {
	$(document).on('click', '.confirm-button', function(event) {
		if (! confirm('Voulez-vous continuer ?')) {
			event.preventDefault();
		}
	});
});


