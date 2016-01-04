
<div data-chart-datas="true" class="hidden">	

	<g:if test="${ datas.size() > 0 && datas[0] instanceof smarthome.automation.DeviceValue }">
		<!-- objet DeviceValue : pas de projections on affiche toutes les valeurs avec la date -->
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'ID', type: 'string'},
	   		 {label: 'Date', type: 'datetime'},
	   		 {label: 'Heure', type: 'number'},
	   		 {label: 'Périphérique', type: 'string'},
	   		 {label: 'Quantité', type: 'number'}],
	   		<g:each var="data" in="${datas?.groupBy{ deviceValue -> use (groovy.time.TimeCategory) { deviceValue.day + deviceValue.hourOfDay.hours} }}">
	   			['', new Date(<g:formatDate date="${data.key}" format="yyyy,${data.key.getAt(Calendar.MONTH)},d,HH"/>), ${ data.value[0].hourOfDay }, '${label}', ${data.value.size()}],
	   		</g:each>
	   	]);
	</g:if>
	<g:elseif test="${ datas.size() > 0 && datas[0] instanceof Map && datas[0]['month'] }">
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'ID', type: 'string'},
	   		 {label: 'Date', type: 'datetime'},
	   		 {label: 'Heure', type: 'number'},
	   		 {label: '${ label }', type: 'string'},
	   		 {label: 'Quantité', type: 'number'}],
	   		<g:each var="data" in="${datas}">
	   			['', new Date(${data.year},${data.month-1},1), ${ data.hour }, '${label}', ${data.count}],
	   		</g:each>
	   	]);
	</g:elseif>
	<g:elseif test="${ datas.size() > 0 && datas[0] instanceof Map && datas[0]['day'] }">
		<!-- objet map donc projection : objet day donc projection par jour -->
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'ID', type: 'string'},
	   		 {label: 'Date', type: 'datetime'},
	   		 {label: 'Heure', type: 'number'},
	   		 {label: 'Périphérique', type: 'string'},
	   		 {label: 'Quantité', type: 'number'}],
	   		 <g:each var="data" in="${datas}">
	   			['', new Date(<g:formatDate date="${data.day}" format="yyyy,${data.day.getAt(Calendar.MONTH)},d"/>), ${ data.hour }, '${label}', ${data.count}],
	   		</g:each>
	   	]);
	</g:elseif>
	<g:else>
		<!-- ne sait pas traiter, donc affiche graphe vide -->
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'ID', type: 'string'},
	   		 {label: 'Date', type: 'datetime'},
	   		 {label: 'Heure', type: 'number'},
	   		 {label: 'Périphérique', type: 'string'},
	   		 {label: 'Quantité', type: 'number'}],
	   		]);
	</g:else>
   	
	chartOptions = {
		'title': '${label }',
		'width': '${ params.chartWidth ?: '100%' }',
        'height': '${ params.chartHeight ?: '600' }',
        'explorer': {},
        legend: {position: 'top'},
	}
</div>


