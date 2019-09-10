/**
 * Transforme un select simple en combobox avec recherche intégrée
 */
function combobox() {
	$("select.combobox").select2({
		openOnEnter: false,
		/*matcher: function(term, text, option) {
			// rien n'est saisi, tout passe
			if (! term) {
				return true;
			} else {
				// si rien trouvé plus haut : recherche normale
				
				return text.toLowerCase().indexOf(term.toLowerCase()) != -1;
			}
		}*/
	});
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