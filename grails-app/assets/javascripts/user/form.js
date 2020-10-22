$(document).on('keydown', 'form.enter-as-tab-form :input', function(event) {
	if (event.which == 13 && !event.ctrlKey) {
		focusNextElement($(this));
		return false;
	}
	return true
});


/**
 * Recherche du champ suivant pour lui donner le focus
 * 
 * @param element
 */
function focusNextElement(element) {
	var form = element.parents('form:eq(0)'),
		focusable, next;

    focusable = form.find(':input').filter(':visible').filter(':enabled');
    var index = focusable.index(element)
    next = focusable.slice(index + 1).not('select.select2-offscreen').first();
    if (next.length) {
        next.focus();
    }
}


/**
 * Transforme un select simple en combobox avec recherche intégrée
 */
function combobox() {
	$("select.combobox").each(function() {
		var $this = $(this)
		
		$this.select2({
			openOnEnter: false,
			tags: $this.attr('data-tags') == 'true'
			/*matcher: function(term, text, option) {
				// rien n'est saisi, tout passe
				if (! term) {
					return true;
				} else {
					// si rien trouvé plus haut : recherche normale
					
					return text.toLowerCase().indexOf(term.toLowerCase()) != -1;
				}
			}*/
		})
	})
}


function initToggle() {
	$('.smart-toggle').on("change", function(event) {
		var $toggle = $(this)
		
		// submit le form associé dès que la valeur change
		if ($toggle.attr('data-autosubmit') == 'true') {
			var toggle = $toggle[0]
			var isChecked = toggle.checked
			
			if (!confirm('Voulez-vous continuer ?')) {
				toggle.checked = !isChecked;
				return
			}
			
			var form = document.getElementById($toggle.attr('form'))
			var $form = $(form)
			ajaxGet(form, 'action', $form.serialize(), '')
		}
	})
}


function initDateTimePicker() {
	$('input.aui-datetime-picker').datetimepicker({
		allowTimes: [],
		step: 30,
		format: 'd/m/Y H:i'
	})
}


function initPickList() {
	$(".picklist").pickList({
		sourceListLabel:	"Disponibles",
		targetListLabel:	"Ajoutés",
		addClass: "pick-button",
		addAllClass: "pick-button",
		removeClass: "pick-button",
		removeAllClass: "pick-button",
	});
}


function selectRadio(elementId, value) {
	$("#" + elementId).prop("checked", value)
}


function inputMask() {
	$('input[data-mask]').each(function() {
		$(this).mask($(this).attr('data-mask'), {
			autoclear: false,
			selectOnFocus: true
		})
	})
}