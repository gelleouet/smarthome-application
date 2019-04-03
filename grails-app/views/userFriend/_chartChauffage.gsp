<%@ page import="smarthome.automation.ChartTypeEnum" %>

<div id="chartDiv-${ chartId }" data-chart-type="${ ChartTypeEnum.Pie.factory }">
	<div data-chart-datas="true" class="hidden">	
		chartDatas = google.visualization.arrayToDataTable([
	   		['Chauffage', 'count'],
	   		<g:each var="item" in="${ datas }" status="status">
		   		['${ item.chauffage }', ${ item.count }],
	   		</g:each>
	   	]);
	   	
		chartOptions = {
			title: 'Répartition chauffage',
			'width': '${ params.chartWidth ?: '100%' }',
	        'height': '${ params.chartHeight ?: '350' }',
		}
	</div>
</div>

