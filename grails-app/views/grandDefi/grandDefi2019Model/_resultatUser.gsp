<%@ page import="smarthome.core.chart.GoogleChart" %>

<g:if test="${ data.error }">
	<div class="mt-4">
		<g:applyLayout name="messageWarning">
			${ data.error } 
			<g:link controller="compteur" action="compteur" class="btn btn-link"><app:icon name="settings"/> Configurer votre compteur</g:link>
		</g:applyLayout>
	</div>
</g:if>
<g:else>
	<div class="row mt-4">
		<div class="col-4">
			
			<div id="${ GoogleChart.randomChartId() }" data-chart-type="${ data.chartTotal.chartType }">
				<div data-chart-datas="true" class="d-none">	
					chartDatas = new google.visualization.DataTable(${ raw(data.chartTotal.toJsonDataTable().toString(false)) });
	   				chartDatas = new google.visualization.DataView(chartDatas)
				   	
				   	<g:render template="/chart/google/annotation" model="[chart: data.chartTotal]"/>
				   	
					chartOptions = {
						backgroundColor: 'transparent',
						'width': '100%',
				        'height': '300',
				        'legend': {position: 'top'},
				        'chartArea': {
				        	width: '60%',
				        },
				        'vAxes': {
							<g:render template="/chart/google/vaxis" model="[chart: data.chartTotal]"/>
					    },
				        'series': {
							<g:render template="/chart/google/series" model="[chart: data.chartTotal]"/>
						},
					    'hAxis': {
					    	gridlines: { color: 'none'},
					    }
					}
				</div>
			</div>
			
			<ul class="list-group list-group-flush">
			  <li class="list-group-item border-top-0">
			  	<div class="row">
			  		<div class="col-6">
			  			<h5>Référence</h5>
			  		</div>
			  		<div class="col-6 text-right">
			  			<h4 class="text-black-50 font-weight-bold">
			  				<g:render template="formatConsommation" model="[value: data.consos.reference]"/>
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
			  			<h4 class="text-primary font-weight-bold">
			  				<g:render template="formatConsommation" model="[value: data.consos.action]"/>
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
			  				<g:applyLayout name="arrow" model="[value: data.consos.difference != null ? data.consos.difference : '-', reference: 0]">
			  					<span style="font-size: x-small;">kWh</span>
			  				</g:applyLayout>
			  			</h4>
			  		</div>
			  	</div>
			  </li>
			</ul>
		</div> <!-- div.col -->
		
		<div class="col-8">
			<div id="${ GoogleChart.randomChartId() }" data-chart-type="${ data.chartConso.chartType }">
				<div data-chart-datas="true" class="d-none">	
					chartDatas = new google.visualization.DataTable(${ raw(data.chartConso.toJsonDataTable().toString(false)) });
	   				chartDatas = new google.visualization.DataView(chartDatas)
				   	
				   	<g:render template="/chart/google/annotation" model="[chart: data.chartConso]"/>
				   	
					chartOptions = {
						backgroundColor: 'transparent',
						'width': '100%',
				        'height': '400',
				        'legend': {position: 'top'},
				        'chartArea': {
				        	top: 50,
				        	width: '85%'
				        },
				        'vAxes': {
							<g:render template="/chart/google/vaxis" model="[chart: data.chartConso]"/>
					    },
				        'series': {
							<g:render template="/chart/google/series" model="[chart: data.chartConso]"/>
						},
					    'hAxis': {
					    	gridlines: { color: 'none'},
					    	format: '${ data.chartConso.hAxisFormat }',
					    	ticks: [${ data.chartConso.hAxisTicks }],
					    	slantedText: ${ data.chartConso.slantedText },	
					    }
					}
				</div>
			</div> <!-- div.chart -->
			
			<div class="row ml-4 mr-4" style="margin-top:-40px;">
				<div class="col border-bottom border-right text-center p-2">
					<h5 class="font-weight-bold">Evolution de mes consommations</h5>
				</div>
				<g:if test="${ data.consos.type?.toString() != 'global' }">
					<div class="col border-bottom border-right text-center p-2">
						<h5 class="font-weight-bold">Moyenne des évolutions des consommations</h5>
					</div>
				</g:if>
				<div class="col border-bottom border-right text-center p-2">
					<g:if test="${ data.consos.type?.toString() == 'global' }">
						<h5 class="font-weight-bold">Mes "économies"</h5>
						<small class="text-muted">(Moyenne des économies électricité et gaz naturel)</small>
					</g:if>
					<g:elseif test="${ (participant?.groupKey?.contains('ELEC') && data.consos.type?.toString() == 'electricite') || 
						(participant?.groupKey?.contains('GN') && data.consos.type?.toString() == 'gaz') }">
						<h5 class="font-weight-bold">Mes "économies"</h5>
						<small class="text-muted">(Différence entre mon évolution et la moyenne des évolutions)</small>
					</g:elseif>
					<g:else>
						<h5 class="font-weight-bold">Mes économies</h5>
					</g:else>
				</div>
				<div class="col border-bottom text-center p-2">
					<h5 class="font-weight-bold">Mon classement dans le ${ defi?.libelle }</h5>
				</div>
			</div>
			<div class="row ml-4 mr-4">
				<div class="col border-right text-center p-2">
					<h4 class="font-weight-bold">
						<g:applyLayout name="arrow" model="[value: data.consos.evolution != null ? data.consos.evolution : '-', reference: 0]"><span style="font-size:small;">%</span></g:applyLayout>
					</h4>
				</div>
				<g:if test="${ data.consos.type?.toString() != 'global' }">
					<div class="col border-right text-center p-2">
						<h4 class="font-weight-bold">
							<g:applyLayout name="arrow" model="[value: data.consos.moyenne != null ? data.consos.moyenne : '-', reference: 0]">
								<span style="font-size:small;">%</span>
							</g:applyLayout>
						</h4>
					</div>
				</g:if>
				<div class="col border-right text-center p-2">
					<h4 class="font-weight-bold">
						<g:applyLayout name="arrow" model="[value: data.consos.economie != null ? data.consos.economie : '-', reference: 0]">
							<span style="font-size:small;">%</span>
						</g:applyLayout>
					</h4>
				</div>
				<div class="col text-center p-2 bg-menu">
					<g:if test="${ data.consos.classement }">
						<h4 class="font-weight-bold text-menu">${ data.consos.classement } / ${ data.consos.total }</h4>
					</g:if>
				</div>
			</div>
		</div> <!-- div.col -->
	</div> <!-- div.row -->
</g:else>