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


function onLoadGoogleChart() {
	google.load("visualization", "1.0", {packages:["corechart"]});
	google.setOnLoadCallback(buildGoogleCharts);
}


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
	var divData = $(divChart).find('div[data-chart-datas]');
	var $divData = $(divData)
	var divId = $(divChart).attr('id');
	
	if ($divData) {
		var chartData = $divData.html();
		
		if (chartData) {
			var chartDatas, chartJoinDatas, chartOldDatas, chartOptions, chartType, chart;
			eval(chartData);
			
			if (!chartType) {
				chartType = $(divChart).attr('data-chart-type');
			}
			
			chart = eval("new google.visualization." + chartType + "(document.getElementById('" + divId + "'))");
			
			if ($divData.attr('data-on-build-chart')) {
				var onBuildChart = window[$divData.attr('data-on-build-chart')]
				onBuildChart($divData, chart, chartDatas)
			}
			
			if (chartOldDatas) {
				var diffDatas = chart.computeDiff(chartOldDatas, chartDatas)
				chart.draw(diffDatas, chartOptions);
			} else if (chartJoinDatas) {
				var joinDatas = google.visualization.data.join(chartDatas, chartJoinDatas, 'full',
						[[0,0]], [1,2,3,4], [1])
				chart.draw(joinDatas, chartOptions);
			} else {
				chart.draw(chartDatas, chartOptions);
			}
	      	
	      	// nettoie les éléments de contruction du chart
	      	//divData.remove();
			//$(divChart).removeAttr('data-chart-type');
		} else {
			console.log('build chart cancel (no data)');
		}
	} else {
		console.log('build chart cancel (no div data)');
	}
}


function onLoadChart() {
	$(document).on('change', "#navigation-chart-form #dateChart", function() {
		$("#navigation-chart-form").submit()
	});
	
	$(document).on('click', "#navigation-chart-form #navigation-chart-day-button", function() {
		$("#navigation-chart-form #viewMode").val('day')
	});
	$(document).on('click', "#navigation-chart-form #navigation-chart-month-button", function() {
		$("#navigation-chart-form #viewMode").val('month')
	});
	$(document).on('click', "#navigation-chart-form #navigation-chart-year-button", function() {
		$("#navigation-chart-form #viewMode").val('year')
	});
	
	$(document).on('click', "#navigation-chart-form #navigation-chart-prev-button", function() {
		$("#navigation-chart-form #navigation").val('prev')
	});
	$(document).on('click', "#navigation-chart-form #navigation-chart-next-button", function() {
		$("#navigation-chart-form #navigation").val('next')
	});
}


function onBuildQualitatifChart(divData, chart, chartDatas) {
	if (divData.attr('data-url-delete-value')) {
		chart.setAction({
				id: 'deleteValue',
				text: 'Supprimer valeur',
				action: function() { 
					var selection = chart.getSelection()
					var valueId = chartDatas.getRowProperty(selection[0].row, "deviceValueId")
					
					if (confirm('Voulez-vous supprimer cette valeur ?')) {
						ajaxGet(divData, 'data-url-delete-value', {id: valueId}, null, function() {
							$('#navigation-chart-form').submit()
						})
					}
				}
		})
	}
	
	if (divData.attr('data-url-change-value')) {
		chart.setAction({
			id: 'changeValue',
			text: 'Modifier valeur',
			action: function() { 
				var selection = chart.getSelection()
				var valueId = chartDatas.getRowProperty(selection[0].row, "deviceValueId")
				
				ajaxGet(divData, 'data-url-change-value', {id: valueId}, "#ajaxDialog", function() {
					showDeviceValueDialog()
				})
			}
		})
	}
}









