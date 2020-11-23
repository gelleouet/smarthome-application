<%@ page import="smarthome.core.chart.GoogleChart" %>

<div class="row mt-4">
	<div class="col">
		
		<div id="${ GoogleChart.randomChartId() }" data-chart-type="${ data.chartClassement.chartType }">
			<div data-chart-datas="true" class="d-none">	
				chartDatas = new google.visualization.DataTable(${ raw(data.chartClassement.toJsonDataTable().toString(false)) });
   				chartDatas = new google.visualization.DataView(chartDatas)
			   	
			   	<g:render template="/chart/google/annotation" model="[chart: data.chartClassement]"/>
			   	
				chartOptions = {
					'width': '100%',
			        'height': '300',
			        'legend': {position: 'none'},
			        'chartArea': {
			        	top: 0,
			        	width: '60%',
			        },
			        'vAxes': {
						<g:render template="/chart/google/vaxis" model="[chart: data.chartClassement]"/>
				    },
			        'series': {
						<g:render template="/chart/google/series" model="[chart: data.chartClassement]"/>
					},
				    'hAxis': {
				    	title: '${ data.chartClassement.hAxisTitle }',
				    }
				}
			</div>
		</div>
		
	</div> <!-- div.col -->
	
</div> <!-- div.row -->
