$(window).on('load', function() {
	$(document).on('click', 'a.confirm-button', function(event) {
		if (! confirm('Voulez-vous continuer ?')) {
			event.preventDefault();
		}
	});
});


