<asset:script type="text/javascript">
	
		
	function loadChartDatas() {
		var datas = google.visualization.arrayToDataTable([
    		[{label: 'Date', type: 'datetime'},
    		 {label: 'Intensité instantanée (A)', type: 'number'},
    		 {label: 'Puissance apparente (VA)', type: 'number'},
    		 {label: 'Heures creuses (Wh)', type: 'number'},
    		 {label: 'Heures pleines (Wh)', type: 'number'},
    		],
    		<g:each var="map" in="${ datas?.groupBy{ it.dateValue } }">
    			[new Date(<g:formatDate date="${ map.key}" format="yyyy,M,d,H,m,s,0"/>),  
    				${map.value.find({ !it.name })?.value},
    				${map.value.find({ it.name == 'papp' })?.value},
    				${map.value.find({ it.name == 'hcinst' })?.value},
    				${map.value.find({ it.name == 'hpinst' })?.value},
    			],
    		</g:each>
    	]);
    	
    	//var format = new google.visualization.NumberFormat({'pattern': '#'});
    	//format.format(datas, 1);
    	
    	return datas;
	}
	
	
	function loadChartOptions() {
		return {
    		  'title': '${label }',
    		  'pointSize': '2',
    	      'hAxis': {'title': 'Date'},
    	      'width': '100%',
              'height': '600',
      	};
	}
	
</asset:script>