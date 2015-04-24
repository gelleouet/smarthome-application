<div data-chart-datas="true" class="hidden">
	
	chartDatas = google.visualization.arrayToDataTable([
   		[{label: 'Date', type: 'datetime'},
   		 {label: '${label}', type: 'number'}],
   		 [new Date(<g:formatDate date="${ new Date() }" format="yyyy,M,d,H,m,s,0"/>),  5],
   	]);
   	
   	var format = new google.visualization.NumberFormat({'pattern': '#.#'});
   	format.format(chartDatas, 1);
	
	
	chartOptions = {
 		  'pointSize': '2',
 	      'hAxis': {'title': 'Date'},
 	      'width': '${ params.chartWidth ?: '100%' }',
          'height': '${ params.chartHeight ?: '600' }',
   	};
	
</div>