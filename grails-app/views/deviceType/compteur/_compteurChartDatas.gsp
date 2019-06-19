<%@ page import="smarthome.automation.ChartViewEnum" %>

<div data-chart-datas="true" class="hidden">
	chartDatas = new google.visualization.DataTable(${ raw(chart.toJsonDataTable().toString(false)) })
	
	<g:if test="${ command.viewMode == ChartViewEnum.day }">
		chartOptions = {
			  title: '${label }',
		      width: '${ params.chartWidth ?: '100%' }',
	          height: '${ params.chartHeight ?: '600' }',
	          legend: {position: 'top'},
	          selectionMode: 'multiple',
	          vAxes: {
	          	0: {title: 'Consommation (${ command.device.metadata('unite')?.value })'},
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
	  	
	  	chartType = 'SteppedAreaChart';
	</g:if>
	<g:else>
		chartDatas = new google.visualization.DataView(chartDatas)
      	
      	chartDatas.setColumns([0,
			1,{ calc: "stringify",
                sourceColumn: 1,
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
	          },
	          vAxes: {
	          	0: {title: 'Consommation (${ command.device.metadata('unite')?.value })'},
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