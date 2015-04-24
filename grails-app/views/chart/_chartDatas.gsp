<div data-chart-datas="true" class="hidden">	

	chartDatas = google.visualization.arrayToDataTable([
   		[{label: 'Date', type: 'datetime'},
   		 {label: '${label}', type: 'number'}],
   		<g:each var="data" in="${datas}">
   			[new Date(<g:formatDate date="${data.dateValue}" format="yyyy,M,d,H,m,s,0"/>),  ${data.value}],
   		</g:each>
   	]);
    	
   	var format = new google.visualization.NumberFormat({'pattern': '#.#'});
   	format.format(chartDatas, 1);
    	
	chartOptions = {
		'title': '${label }',
		'pointSize': '2',
		'hAxis': {'title': 'Date'},
		'width': '${ params.chartWidth ?: '100%' }',
        'height': '${ params.chartHeight ?: '600' }',
	}
</div>