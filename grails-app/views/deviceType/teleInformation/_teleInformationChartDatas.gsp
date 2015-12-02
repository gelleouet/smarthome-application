<div data-chart-datas="true" class="hidden">
		
	chartDatas = google.visualization.arrayToDataTable([
   		[{label: 'Date', type: 'datetime'},
   		 {label: 'Heures creuses (Wh)', type: 'number'},
   		 {label: 'Heures pleines (Wh)', type: 'number'},
   		],
   		<g:each var="map" in="${ datas?.groupBy{ it.dateValue } }">
   			[new Date(<g:formatDate date="${ map.key}" format="yyyy,M,d,H,m,s,0"/>),  
   				${map.value.find({ it.name == 'hcinst' })?.value},
   				${map.value.find({ it.name == 'hpinst' })?.value},
   			],
   		</g:each>
   	]);
    	
	chartOptions = {
		  'title': '${label }',
		  'pointSize': '2',
	      'hAxis': {'title': 'Date'},
	      'width': '${ params.chartWidth ?: '100%' }',
          'height': '${ params.chartHeight ?: '600' }',
  	};
	
</div>