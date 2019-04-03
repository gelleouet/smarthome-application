<%@ page import="smarthome.automation.ChartTypeEnum" %>

<div id="chartDiv-${ chartId }" data-chart-type="${ ChartTypeEnum.Bar.factory }">
	<div data-chart-datas="true" class="hidden">	
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'Utilisateur', type: 'string'},
	   		{label: 'Classement', type: 'number'},
	   		{role: 'annotation', type: 'number'}],
	   		<g:each var="item" in="${ datas }" status="status">
		   		['${ item.house.user.prenomNom }', ${ item.consoTotaleBySurface() }, '${ item.consoTotaleBySurface() }'],
	   		</g:each>
	   	]);
	   	
		chartOptions = {
			'width': '${ params.chartWidth ?: '100%' }',
	        'height': '${ params.chartHeight ?: '350' }',
	        'legend': {position: 'none'},
	        'chartArea': {
	        	width: '65%'
	        },
		    'hAxis': {
		    	title: 'Classement kWh/m²/an',
		    }
		}
	</div>
</div>

