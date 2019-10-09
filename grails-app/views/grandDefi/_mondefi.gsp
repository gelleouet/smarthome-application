<div class="row mt-4">
	<div class="col-4">
		
		<div id="chartDiv-total-${ chartId }" data-chart-type="${ chartTotal.chartType }">
			<div data-chart-datas="true" class="d-none">	
				chartDatas = new google.visualization.DataTable(${ raw(chartTotal.toJsonDataTable().toString(false)) });
   				chartDatas = new google.visualization.DataView(chartDatas)
			   	
			   	<g:render template="/chart/google/annotation" model="[chart: chartTotal]"/>
			   	
				chartOptions = {
					'width': '100%',
			        'height': '300',
			        'legend': {position: 'top'},
			        'chartArea': {
			        	width: '60%',
			        },
			        'vAxes': {
						<g:render template="/chart/google/vaxis" model="[chart: chartTotal]"/>
				    },
			        'series': {
						<g:render template="/chart/google/series" model="[chart: chartTotal]"/>
					},
				    'hAxis': {
				    	gridlines: { color: 'none'},
				    }
				}
			</div>
		</div>
		
		<ul class="list-group list-group-flush">
		  <li class="list-group-item">
		  	<div class="row">
		  		<div class="col-7">
		  			<h5>Référence</h5>
		  			<small style="font-size: x-small;">du <app:formatUser date="${ defi.referenceDebut }"/> au <app:formatUser date="${ defi.referenceFin }"/></small>
		  		</div>
		  		<div class="col-5 text-right">
		  			<h4>${ consos.totalReference }<span style="font-size: x-small;">kWh</span></h4>
		  		</div>
		  	</div>
		  </li>
		  <li class="list-group-item">
		  	<div class="row">
		  		<div class="col-7">
		  			<h5>Action</h5>
		  			<small style="font-size: x-small;">du <app:formatUser date="${ defi.actionDebut }"/> au <app:formatUser date="${ defi.referenceFin }"/></small>
		  		</div>
		  		<div class="col-5 text-right">
		  			<h4>${ consos.totalAction }<span style="font-size: x-small;">kWh</span></h4>
		  		</div>
		  	</div>
		  </li>
		  <li class="list-group-item">
		  	<div class="row">
		  		<div class="col-7">
		  			<h5>Différence</h5>
		  		</div>
		  		<div class="col-5 text-right">
		  			<g:if test="${ consos.totalDiff > 0 }">
		  				<h4><app:icon name="arrow-up-right"/> +${ consos.totalDiff }<span style="font-size: x-small;">kWh</span></h4>
		  			</g:if>
		  			<g:else>
		  				<h4><app:icon name="arrow-down-right"/> ${ consos.totalDiff }<span style="font-size: x-small;">kWh</span></h4>
		  			</g:else>
		  		</div>
		  	</div>
		  </li>
		</ul>
	</div>
	
	
	<div class="col-8">
		<div id="chartDiv-conso-${ chartId }" data-chart-type="${ chartConso.chartType }">
			<div data-chart-datas="true" class="d-none">	
				chartDatas = new google.visualization.DataTable(${ raw(chartConso.toJsonDataTable().toString(false)) });
   				chartDatas = new google.visualization.DataView(chartDatas)
			   	
			   	<g:render template="/chart/google/annotation" model="[chart: chartConso]"/>
			   	
				chartOptions = {
					'width': '100%',
			        'height': '450',
			        'legend': {position: 'top'},
			        'chartArea': {
			        	top: 50,
			        	width: '85%'
			        },
			        'vAxes': {
						<g:render template="/chart/google/vaxis" model="[chart: chartConso]"/>
				    },
			        'series': {
						<g:render template="/chart/google/series" model="[chart: chartConso]"/>
					},
				    'hAxis': {
				    	gridlines: { color: 'none'},
				    }
				}
			</div>
		</div>
	</div>
</div>
