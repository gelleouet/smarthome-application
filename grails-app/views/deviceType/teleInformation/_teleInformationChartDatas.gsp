<div data-chart-datas="true" class="hidden">
		
	chartDatas = google.visualization.arrayToDataTable([
   		[{label: 'Date', type: 'datetime'},
   		 {label: 'Heures creuses (Wh)', type: 'number'},
   		 {label: 'Heures pleines (Wh)', type: 'number'},
   		 {label: 'Intensité (A)', type: 'number'},
   		],
   		<g:each var="map" in="${ datas?.groupBy{ it.dateValue } }">
   			[new Date(<g:formatDate date="${ map.key}" format="yyyy,M,d,H,m,s,0"/>),  
   				${map.value.find({ it.name == 'hcinst' })?.value},
   				${map.value.find({ it.name == 'hpinst' })?.value},
   				${map.value.find({ !it.name })?.value},
   			],
   		</g:each>
   	]);
    	
	chartOptions = {
		  'title': '${label }',
		  'pointSize': '2',
	      'hAxis': {'title': 'Date'},
	      'width': '${ params.chartWidth ?: '100%' }',
          'height': '${ params.chartHeight ?: '600' }',
          'series': {
          	0: {targetAxisIndex: 0},
          	1: {targetAxisIndex: 0},
          	2: {targetAxisIndex: 1},
          },
          vAxes: {
          	0: {title: 'Index (Wh)'},
          	1: {title: 'Intensité (A)'}
          },
  	};
	
</div>