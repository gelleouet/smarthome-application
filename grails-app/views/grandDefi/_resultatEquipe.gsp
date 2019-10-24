<%@ page import="smarthome.core.chart.GoogleChart" %>

<div class="row mt-4">
	<div class="col-4">
		
		<div id="${ GoogleChart.randomChartId() }" data-chart-type="${ data.chartTotal.chartType }">
			<div data-chart-datas="true" class="d-none">	
				chartDatas = new google.visualization.DataTable(${ raw(data.chartTotal.toJsonDataTable().toString(false)) });
   				chartDatas = new google.visualization.DataView(chartDatas)
			   	
			   	<g:render template="/chart/google/annotation" model="[chart: data.chartTotal]"/>
			   	
				chartOptions = {
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
		  			<h4 class="text-black-50 font-weight-bold">${ data.consos.reference != null ? data.consos.reference : '-' }<span style="font-size: x-small;">kWh</span></h4>
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
		  			<h4 class="text-primary font-weight-bold">${ data.consos.action != null ? data.consos.action : '-' }<span style="font-size: x-small;">kWh</span></h4>
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
					'width': '100%',
			        'height': '300',
			        'legend': {position: 'top'},
			        'chartArea': {
			        	width: '60%',
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
		
		<g:set var="consos" value="${ data.consos.values?.sort{ it.profil.libelle } }"/>

		<div class="row mt-3 ml-4 mr-4">
			<div class="col border-bottom border-right p-2">
				
			</div>
			<g:each var="conso" in="${ consos }" status="status">
				<div class="col border-bottom border-right p-2 text-center">
					<h5 class="font-weight-bold">
						${ conso.profil.libelle }
						<g:if test="${ conso.profil.icon }"><asset:image src="${conso.profil.icon }" class="gd-icon-profil"/></g:if>
					</h5>
				</div>
			</g:each>
			<div class="col border-bottom p-2 text-center bg-menu">
				<h5 class="font-weight-bold text-menu">Equipe</h5>
			</div>
		</div>
		<div class="row ml-4 mr-4">
			<div class="col border-bottom border-right p-2">
				<h5 class="text-muted">Economie (en %)</h5>
			</div>
			<g:each var="conso" in="${ consos }" status="status">
				<div class="col border-bottom border-right p-2 text-center">
					<g:set var="value" value="${ conso."economie_${data.consos.type}"()  }"/>
					<h4 class="font-weight-bold">
						<g:applyLayout name="arrow" model="[value: value != null ? value : '-', reference: 0]">
							<span style="font-size:small;">%</span>
						</g:applyLayout>
					</h4>
				</div>
			</g:each>
			<div class="col border-bottom p-2 text-center bg-menu">
				<g:set var="value" value="${ data.consos.economie  }"/>
				<h4 class="font-weight-bold text-menu">
					<g:applyLayout name="arrow" model="[value: value != null ? value : '-', reference: 0]">
						<span style="font-size:small;">%</span>
					</g:applyLayout>
				</h4>
			</div>
		</div>
		<div class="row ml-4 mr-4">
			<div class="col border-right p-2">
				<h5 class="text-muted">Classement</h5>
			</div>
			<g:each var="conso" in="${ consos }" status="status">
				<div class="col border-right p-2 text-center">
					<g:set var="classement" value="${ conso."classement_${data.consos.type}"()  }"/>
					<h4 class="font-weight-bold">${ classement != null ? classement : '-' } / ${ data.totalClassement ?: '-' }</h4>
				</div>
			</g:each>
			<div class="col p-2 text-center bg-menu">
				<g:set var="classement" value="${ data.consos.classement  }"/>
				<h4 class="font-weight-bold text-menu">${ classement != null ? classement : '-' } / ${ data.totalClassement ?: '-' }</h4>
			</div>
		</div>
	</div> <!-- div.col -->
</div> <!-- div.row -->
