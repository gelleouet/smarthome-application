<%@ page import="smarthome.core.chart.GoogleChart" %>

<div class="row mt-4">
	<div class="col-6">
		
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
	
	<div class="col-6">
		
		<g:set var="equipe1" value="${ data.classement ? data.classement[0].resultat : null }"/>
	
		<h3 class="ml-3">${ data.classement ? data.classement[0].libelle : '' }</h3>
	
		<ul class="list-group list-group-flush">
		  <li class="list-group-item border-top-0">
		  	<div class="row">
		  		<div class="col-6">
		  			<h5>Référence</h5>
		  		</div>
		  		<div class="col-6 text-right">
		  			<h4 class="font-weight-bold">
		  				<g:render template="formatConsommation" model="[value: equipe1?.reference_global(), precision: 0]"/>
		  			</h4>
		  		</div>
		  	</div>
		  	<small class="text-muted" style="font-size:9pt;">Du <app:formatUser date="${ defi?.referenceDebut }"/> au <app:formatUser date="${ defi?.referenceFin }"/></small>
		  </li>
		  <li class="list-group-item">
		  	<div class="row">
		  		<div class="col-6">
		  			<h5>Action</h5>
		  		</div>
		  		<div class="col-6 text-right">
		  			<h4 class="font-weight-bold">
		  				<g:render template="formatConsommation" model="[value: equipe1?.action_global(), precision: 0]"/>
		  			</h4>
		  		</div>
		  	</div>
		  	<small class="text-muted" style="font-size:9pt;">Du <app:formatUser date="${ defi?.actionDebut }"/> au <app:formatUser date="${ defi?.actionFin }"/></small>
		  </li>
		  <li class="list-group-item">
		  	<div class="row">
		  		<div class="col-6">
		  			<h5>Différence</h5>
		  		</div>
		  		<div class="col-6 text-right">
		  			<h4 class="font-weight-bold">
		  				<g:applyLayout name="arrow" model="[value: equipe1?.difference_global() != null ? equipe1?.difference_global() : '-', reference: 0]"><span style="font-size: x-small;">kWh</span></g:applyLayout>
		  			</h4>
		  		</div>
		  	</div>
		  </li>
		</ul>
	</div> <!-- div.col -->
</div> <!-- div.row -->
