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
	$(document).on('click', '#commit-metadata-button', function() {
		if (confirm('Voulez-vous changer la configuration sur le périphérique ?')) {
			ajaxSubmitForm($(this), 'data-url', '#device-form');
		}
	});
});


$(window).on('load', function() {
	$(document).on('click', '#level-alert-delete-button', function() {
		ajaxSubmitForm($(this), 'data-url', '#device-form', '#ajaxAlerts');
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
	$('.slider').each(function() {
		var slider = $(this)
		
		slider.slider({
			orientation: slider.attr('data-orientation'),
			range: slider.attr('data-range'),
			min: parseInt(slider.attr('data-min')),
			max: parseInt(slider.attr('data-max')),
			step: parseInt(slider.attr('data-step')),
			value: parseInt(slider.attr('data-value')),
			slide: function(event, ui) {
				$("#" + slider.attr('data-element-id')).html(ui.value);
			},
			change: function(event, ui) {
				if (confirm('Voulez-vous continuer ?')) {
					var form = $('#' + slider.attr('data-form-id'))
					var actionName = slider.attr('data-action-name')
					form.find('#actionName').val(actionName)
					form.find('#value').val(ui.value)
					ajaxSubmitForm(slider, 'data-url', form);
				}
			}
		});
	})
});


function onDropDeviceToTableauBord(event, ui) {
	var datas = {
		id: ui.draggable.attr('data-draggable-value'),
		tableauBord: $(event.target).attr('data-droppable-value')
	}
	
	ajaxGet(event.target, 'data-ondrop-url', datas, "", function(data) {
		ui.draggable.remove()
	})
}


function onDropDeviceToGroupe(event, ui) {
	var datas = {
		id: ui.draggable.attr('data-draggable-value'),
		groupe: $(event.target).attr('data-droppable-value')
	}
	
	ajaxGet(event.target, 'data-ondrop-url', datas, "", function(data) {
		var container = $(event.target).parent().find("div.filActualite")
		ui.helper.remove()
		ui.draggable.appendTo(container)
	})
}


function showDeviceValueDialog() {
	AJS.dialog2('#device-value-dialog').show();
}


function closeDeviceValueDialog(refreshChart) {
	AJS.dialog2('#device-value-dialog').hide()
	
	if (refreshChart) {
		$('#navigation-chart-form').submit()
	}
}


function showAddDeviceValueDialog() {
	AJS.dialog2('#add-device-value-dialog').show();
	initDateTimePicker()
}


function closeAddDeviceValueDialog(refreshChart) {
	AJS.dialog2('#add-device-value-dialog').hide()
	
	if (refreshChart) {
		$('#navigation-chart-form').submit()
	}
}


function onLoadDeviceChart() {
	onLoadChart()
}
