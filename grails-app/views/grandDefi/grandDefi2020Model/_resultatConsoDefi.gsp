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
	</div> <!-- div.col -->
</div> <!-- div.row -->

<g:set var="consos" value="${ data.consos.values?.sort{ it.profil.libelle } }"/>

<div class="row">
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
	<div class="col border-bottom p-2 text-center">
		<h5 class="font-weight-bold">Défi</h5>
	</div>
</div>
<div class="row">
	<div class="col border-bottom border-right p-2">
		<h5>Référence</h5>
		<small class="text-muted" style="font-size:9pt;">Du <app:formatUser date="${ defi?.referenceDebut }"/> au <app:formatUser date="${ defi?.referenceFin }"/></small>
	</div>
	<g:each var="conso" in="${ consos }" status="status">
		<div class="col border-bottom border-right p-2 text-center">
			<h4 class="text-black-50 font-weight-bold">
				<g:render template="formatConsommation" model="[value: conso."reference_${data.type}"(), precision: 0, unite: data.type.converter.unite()]"/>
			</h4>
		</div>
	</g:each>
	<div class="col border-bottom p-2 text-center bg-menu">
		<h4 class="font-weight-bold text-menu">
			<g:render template="formatConsommation" model="[value: defi?."reference_${data.type}"(), precision: 0, unite: data.type.converter.unite()]"/>
		</h4>
	</div>
</div>
<div class="row">
	<div class="col border-bottom border-right p-2">
		<h5>Action</h5>
		<small class="text-muted" style="font-size:9pt;">Du <app:formatUser date="${ defi?.actionDebut }"/> au <app:formatUser date="${ defi?.actionFin }"/></small>
	</div>
	<g:each var="conso" in="${ consos }" status="status">
		<div class="col border-bottom border-right p-2 text-center">
			<h4 class="text-primary font-weight-bold">
				<g:render template="formatConsommation" model="[value: conso."action_${data.type}"(), precision: 0, unite: data.type.converter.unite()]"/>
			</h4>
		</div>
	</g:each>
	<div class="col border-bottom p-2 text-center bg-menu">
		<h4 class="font-weight-bold text-menu">
			<g:render template="formatConsommation" model="[value: defi?."action_${data.type}"(), precision: 0, unite: data.type.converter.unite()]"/>
		</h4>
	</div>
</div>
<div class="row">
	<div class="col border-bottom border-right p-2">
		<h5>Différence</h5>
	</div>
	<g:each var="conso" in="${ consos }" status="status">
		<div class="col border-bottom border-right p-2 text-center">
			<h4 class="font-weight-bold">
				<g:applyLayout name="arrow" model="[value: conso."difference_${data.type}"() != null ? conso."difference_${data.type}"() : '-', reference: 0]">
					<span style="font-size: x-small;">${ data.type.converter.unite() ?: 'kWh' }</span>
				</g:applyLayout>
			</h4>
		</div>
	</g:each>
	<div class="col border-bottom p-2 text-center bg-menu">
		<h4 class="font-weight-bold text-menu">
			<g:applyLayout name="arrow" model="[value: defi?."difference_${data.type}"() != null ? defi?."difference_${data.type}"()  : '-', reference: 0]">
				<span style="font-size: x-small;">${ data.type.converter.unite() ?: 'kWh' }</span>
			</g:applyLayout>
		</h4>
	</div>
</div>
