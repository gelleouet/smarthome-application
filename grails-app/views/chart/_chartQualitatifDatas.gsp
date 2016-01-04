
<div data-chart-datas="true" class="hidden">	

	<g:if test="${ datas.size() > 0 && datas[0] instanceof smarthome.automation.DeviceValue }">
		<!-- objet DeviceValue : pas de projections on affiche toutes les valeurs avec la date -->
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'Date', type: 'datetime'},
	   		 {label: 'Valeur', type: 'number'}],
	   		<g:each var="data" in="${datas}">
	   			[new Date(<g:formatDate date="${data.dateValue}" format="yyyy,${data.dateValue.getAt(Calendar.MONTH)},d,H,m,s,0"/>),  ${data.value}],
	   		</g:each>
	   	]);
	</g:if>
	<g:elseif test="${ datas.size() > 0 && datas[0] instanceof Map && datas[0]['month'] }">
		<!-- objet map donc projection : objet month et year donc projection par mois -->
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'Date', type: 'datetime'},
	   		 {label: 'Min', type: 'number'},
	   		 {label: 'Max', type: 'number'},
	   		 {label: 'Moyenne', type: 'number'}],
	   		 <g:each var="data" in="${datas}">
	   			[new Date(${data.year},${data.month-1},1),  ${data.min}, ${data.max}, ${data.avg}],
	   		</g:each>
	   	]);
	</g:elseif>
	<g:elseif test="${ datas.size() > 0 && datas[0] instanceof Map && datas[0]['day'] }">
		<!-- objet map donc projection : objet day donc projection par jour -->
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'Date', type: 'datetime'},
	   		 {label: 'Min', type: 'number'},
	   		 {label: 'Max', type: 'number'},
	   		 {label: 'Moyenne', type: 'number'}],
	   		 <g:each var="data" in="${datas}">
	   			[new Date(<g:formatDate date="${data.day}" format="yyyy,${data.day.getAt(Calendar.MONTH)},d"/>),  ${data.min}, ${data.max}, ${data.avg}],
	   		</g:each>
	   	]);
	</g:elseif>
	<g:else>
		<!-- ne sait pas traiter, donc affiche graphe vide -->
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'Date', type: 'datetime'},
	   		 {label: '${label}', type: 'number'}],
	   		]);
	</g:else>
   	
	chartOptions = {
		'title': '${label }',
		'pointSize': '2',
		'width': '${ params.chartWidth ?: '100%' }',
        'height': '${ params.chartHeight ?: '600' }',
        'explorer': {},
        legend: {position: 'top'},
	}
</div>


