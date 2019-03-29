/**
 * Enregistre l'état d'un composant
 * 
 * @param url
 * @param name
 * @param data
 */
function saveStateUser(name, page, data) {
	var stateUrl = $('#content[role=main]').attr('state-url');
	
	jQuery.ajax({
		type: 'POST',
		url: stateUrl,
		data: {
			name: name,
			page: page,
			data: data
		}
	});
}


/**
 * 
 * @returns
 */
function initLayoutButton() {
	$(document).on('click', '#layout-1-col-button', function() {
		setLayout(this)
	})
	$(document).on('click', '#layout-2-col-button', function() {
		setLayout(this)
	})
	$(document).on('click', '#layout-2-col-menu-button', function() {
		setLayout(this)
	})
}


/**
 * 
 * @param element
 * @returns
 */
function setLayout(button) {
	var $button = $(button)
	var layout = $button.attr('data-layout')
	var layoutFor = $button.attr('data-layout-for')
	
	var $element = $('#' + layoutFor)
	var stateName = $element.attr('data-state-name')
	var statePage = $element.attr('data-state-page')
	
	// supprime ancien layout
	/*var tokens = $element.attr('class').split(" ")
	
	for (var idx=0; idx<tokens.length; idx++) {
		if (tokens[idx].startsWith("layout-")) {
			$element.removeClass(tokens[idx])
		}
	}
	
	// rajoute nouveau layout
	$element.addClass(layout)*/
	
	// enregistrement user
	saveStateUser(stateName, statePage, layout);
	
	window.location.reload()
}




