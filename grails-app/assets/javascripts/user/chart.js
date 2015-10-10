/**
 * Suppression des devices d'un chart
 */
$(window).on('load', function() {
	$(document).on('click', '#delete-chart-device', function() {
		if (confirm('Voulez-vous continuer ?')) {
			ajaxSubmitForm($(this), 'data-url', '#chart-form', '#ajaxPeripherique');
		}
	});
});


/**
 * Changement du device, rafraichit la liste pour les metavalues
 */
$(window).on('load', function() {
	$(document).on('change', "#chart-form select[name$='device\\.id']", function() {
		ajaxSubmitForm($(this), 'data-url', '#chart-form', '#ajaxPeripherique');
	});
});



/**
 * Construction de tous les charts
 */
function buildGoogleCharts() {
	$('div[data-chart-type]').each(function(index, divChart) {
		buildGoogleChart(divChart);
	});
}

/**
 * Construction des charts sur les div
 */
function buildGoogleChart(divChart) {
	// récupère le type de chart
	var chartType = $(divChart).attr('data-chart-type');
	var divData = $(divChart).find('div[data-chart-datas]');
	
	if ($(divData)) {
		var chartData = $(divData).html();
		
		if (chartData) {
			var chartDatas, chartOptions;
			eval(chartData);
	      	var chart = eval("new google.visualization." + chartType + "(document.getElementById('" + $(divChart).attr('id') + "'))");
	      	chart.draw(chartDatas, chartOptions);
	      	
	      	// nettoie les éléments de contruction du chart
	      	$(divData).remove();
			$(divChart).removeAttr('data-chart-type');
		} else {
			console.log('build chart cancel (no data)');
		}
	} else {
		console.log('build chart cancel (no div data)');
	}
}






