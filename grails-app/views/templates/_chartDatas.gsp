<asset:script type="text/javascript">
	
		
	function loadChartDatas() {
		var datas = google.visualization.arrayToDataTable([
    		[{label: 'Date', type: 'datetime'},
    		 {label: '${label}', type: 'number'}],
    		<g:each var="data" in="${datas}">
    			[new Date(<g:formatDate date="${data.dateValue}" format="yyyy,M,d,H,m,s,0"/>),  ${data.value}],
    		</g:each>
    	]);
    	
    	var format = new google.visualization.NumberFormat({'pattern': '#.#'});
    	format.format(datas, 1);
    	
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