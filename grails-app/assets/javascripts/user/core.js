
// IMPORTANT : Ne pas changer ca, si bascule dans document.ready, ca ne marche plus
(function($) {
	// Gestion d'un spinner à chaque appel Ajax
	// Affiche / Masque un élément d'id = ajaxSpinner
	$(document).ajaxStart(function() {
		$('#ajaxSpinner').show()
	}).ajaxStop(function() {
		$('#ajaxSpinner').fadeOut(200);
		formatDataTable();
		combobox();
		ajaxPagination();
		buildGoogleCharts();
		initIcon()
	});
})(jQuery);


/**
 * Code à exécuter dès qu"une page est chargée
 */
$( document ).ready(function() {
	// ne pas afficher de popup en cas d'erreurs mais log error
	$.fn.dataTable.ext.errMode = 'throw';
	
	formatDataTable();
	ajaxPagination();
	combobox();
	initDragula();
	initToggle()
	selectHashTab()
	divAsyncUrl()
	formAsync()
	//initPickList()
})


/**
 * Si un fragment (#tabs-....) est spécifié dans l'url, le tab doit être sélectionné automatiquement
 */
function selectHashTab() {
	if (window.location.hash) {
		var fragment = window.location.hash.substr(1)
		
		if (fragment.startsWith("tabs")) {
			AJS.tabs.change($('a[href="#' + fragment + '"]'));
		}
	}
}


function initDragAndDrop() {
	$(".smart-draggable" ).draggable({
		addClasses: false,
		opacity: 0.9,
		helper: "clone",
		revert: "invalid",
		cursor: "move",
		start: function(event, ui) {
			$(".smart-droppable").addClass("smart-droppable-active")
		},
		stop: function(event, ui) {
			$(".smart-droppable").removeClass("smart-droppable-active")
		}
	})
	
	$(".smart-droppable" ).droppable({
		addClasses: false,
		accept: ".smart-draggable",
		tolerance: "pointer",
		drop: function(event, ui) {
			var functionName = $(event.target).attr('data-ondrop')
			
			if (functionName && typeof window[functionName]) {
				window[functionName](event, ui)
			} else {
				console.warn("Drop canceled : no drop function !")
			}
		}
	})
}


function initDragula() {
	dragula([
		document.querySelector('.smart-droppable')
	]);
}


function initSortable() {
	$( ".widget-sortable" ).sortable({
		connectWith: ".widget-sortable",
		cursor: "move",
		placeholder: "widget-sortable-placeholder",
		stop: onStopSortable
	}).disableSelection();
}


function onStopSortable(event, ui) {
	var $container = $('.widget-container')
	var datas = {
		id: ui.item.attr('data-widget-id'),
		row: ui.item.index(),
		col: ui.item.parents(".widget-sortable").index()
	}
	
	ajaxGet($container, 'data-url-move', datas)
}


/**
 * Recherche des paginations en mode Ajax.
 * 
 * Transforme les liens GET en appel Ajax
 */
function ajaxPagination() {
	var divParent = $("div[ajax='true']");
	
	if (divParent) {
		// supprime les anciens listener
		divParent.off('click', '.pagination a');
		
		divParent.on('click', '.pagination a', function(event) {
			var link = $(event.target);
			var urlLink = link.attr('href');
			var div = link.parents("div[ajax='true']").eq(0);
			var form = div.attr('data-form-id') ? $('#' + div.attr('data-form-id')) : link.parents('form:first')
			
			jQuery.ajax({
				type: 'POST',
				data: form.serialize(),
				url: urlLink,
				success: function(data, textStatus){
					div.html(data);
				}
			});
			
			return false;
		});
	}
	
	var ajaxPaginations = $(".pagination[data-form-id != '']")
	
	if (ajaxPaginations) {
		ajaxPaginations.each(function() {
			var form = $('#' + $(this).attr('data-form-id'))
			
			// supprime les anciens listener
			$(this).off('click', 'a');
			
			$(this).on('click', 'a', function(event) {
				var link = $(event.target);
				var urlLink = link.attr('href');
				// change l'action par défaut du formulaire en lui mettant l'action de pagination
				form.attr('action', urlLink)
				form.submit();
				return false;
			});
		}) 
	}
}


/**
 * Chargement des DIV asynchrones
 */
function divAsyncUrl() {
	$("div[async-url]").each(function() {
		// récupère l'url dans le composant
		var div = $(this);
		var url = div.attr('async-url');
		var asyncComplete = div.attr('on-async-complete');
		
		jQuery.ajax({
			url: url,
			success: function(data, textStatus){
				div.html(data);
				if (asyncComplete) {
					eval(asyncComplete);
				}
			}
		});
	});
}


function ajaxGet(eltSrcId, urlAttr, datas, divDstId, onSuccess) {
	var urlAction = $(eltSrcId).attr(urlAttr);
	var global = !($(eltSrcId).attr('data-immediate') == 'true');
	
	jQuery.ajax({
		type: 'POST',
		data: datas,
		url: urlAction,
		global: global,
		success: function(data, textStatus) {
			if (divDstId) {
				$(divDstId).html(data);
			}
			if (onSuccess) {
				onSuccess(data);
			}
		},
		error: errorAjaxFunction
	});
}


/**
 * Chargement des formulaires asynchrone.
 * 
 */
function formAsync() {
	$('form[id^="deferedForm"]').submit();
}


var errorAjaxFunction = function(jqXHR, textStatus, errorThrown) {
	if (jqXHR.responseText) {
		$('#ajaxError').html(jqXHR.responseText);
	} else {
		$('#ajaxError').html(errorThrown);
	}
	$('#ajaxError').show();
	$('#ajaxError').fadeOut(3000);
};


function ajaxSubmitForm(eltSrcId, urlAttr, formId, divDstId, onSuccess) {
	var urlAction = $(eltSrcId).attr(urlAttr);
	var datas = $(formId).serializeArray();
	
	jQuery.ajax({
		type: 'POST',
		data: datas,
		url: urlAction,
		success: function(data, textStatus) {
			if (divDstId) {
				$(divDstId).html(data);
			}
			if (onSuccess) {
				onSuccess();
			}
		},
		error: errorAjaxFunction
	});
}


function ajaxRerender(url, datas, divDstId) {
	jQuery.ajax({
		type: 'POST',
		data: datas,
		url: url,
		success: function(data, textStatus) {
			$(divDstId).html(data);
		}
	});
}


function favoriteStar(starId, star) {
	if (star) {
		$(starId).removeClass("aui-iconfont-unstar").addClass("aui-iconfont-star").addClass('star')
	} else {
		$(starId).removeClass("aui-iconfont-star").removeClass("star").addClass('aui-iconfont-unstar')
	}
}


function showDialog(dialogId) {
	$('#' + dialogId).modal('show')
}


function hideDialog(dialogId) {
	$('#' + dialogId).modal('hide')
	$('#' + dialogId).modal('dispose')
}


function initIcon() {
	feather.replace()
}


function dateToStr(date) {
	var object = moment(date)
	return object.format("DD/MM/YYYY")
}

function datetimeToStr(date) {
	var object = moment(date)
	return object.format("DD/MM/YYYY HH:mm:ss")
}
