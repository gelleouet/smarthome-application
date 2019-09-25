<%@ page import="smarthome.automation.ChartViewEnum" %>

<div data-chart-datas="true" class="d-none">	

	chartDatas = new google.visualization.DataTable(${ raw(chart.toJsonDataTable().toString(false)) });

	chartOptions = {
		'width': '${ params.chartWidth ?: '100%' }',
        'height': '${ params.chartHeight ?: '600' }',
        'legend': {position: 'top'},
        'curveType': 'function',
        'chartArea': {
        	width: '90%'
        }
	}
</div>


