<%@ page import="smarthome.automation.ChartViewEnum" %>

<div data-chart-datas="true" class="hidden">
	chartDatas = new google.visualization.DataTable(${ raw(chart.toJsonDataTable().toString(false)) })
	
	<g:if test="${ command.viewMode == ChartViewEnum.day }">
		chartOptions = {
			  title: '${label }',
			  pointSize: '2',
		      width: '${ params.chartWidth ?: '100%' }',
	          height: '${ params.chartHeight ?: '600' }',
	          legend: {position: 'top'},
	          selectionMode: 'multiple',
	          seriesType: 'steppedArea',
	          series: {
	          	0: {targetAxisIndex: 0},
	          	1: {targetAxisIndex: 0},
	          	2: {targetAxisIndex: 1, type: 'line', pointsVisible: false},
	          },
	          vAxes: {
	          	0: {title: 'Index (Wh)'},
	          	1: {title: 'Intensité (A)'}
	          },
		      chartArea: {
		      	width: '90%'
		      },
		      explorer: {
				axis: 'horizontal',
				keepInBounds: true,
				actions: ['dragToZoom', 'rightClickToReset']
			  },
	  	};
	  	
	  	chartType = 'ComboChart';
	</g:if>
	<g:else>
		chartDatas = new google.visualization.DataView(chartDatas)
      	
      	chartDatas.setColumns([0,
			1,{ calc: "stringify",
                sourceColumn: 1,
                type: "string",
                role: "annotation" },
            2,{ calc: "stringify",
                sourceColumn: 2,
                type: "string",
                role: "annotation" },
            3,{ calc: "stringify",
                sourceColumn: 3,
                type: "string",
                role: "annotation" }]
        )
	    	
		chartOptions = {
			  'title': '${label }',
		      'width': '${ params.chartWidth ?: '100%' }',
	          'height': '${ params.chartHeight ?: '600' }',
	          legend: {position: 'top'},
	          isStacked: true,
	          'chartArea': {
		      	width: '90%'
		      },
		      selectionMode: 'multiple',
		      'series': {
	          	0: {targetAxisIndex: 0},
	          	1: {targetAxisIndex: 0},
	          	2: {targetAxisIndex: 1, type: 'line'},
	          },
	          vAxes: {
	          	0: {title: 'Index (kWh)'},
	          	1: {title: 'Intensité (A)'}
	          },
	          'seriesType': 'bars',
	  	};
	  	
	  	chartType = 'ColumnChart';
	</g:else>
	
	chartSelectFunction = function(event) {
		$('#selectionConso').val('')
  		var interval = chartSelectionInterval(chart)
  		var sum = chartSelectionSumInterval(chartDatas, interval)
  		$('#selectionConso').val(sum ? sum : '')
  	}
</div>