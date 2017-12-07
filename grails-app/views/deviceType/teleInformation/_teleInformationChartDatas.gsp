<%@ page import="smarthome.automation.ChartViewEnum" %>

<div data-chart-datas="true" class="hidden">
	
	<g:if test="${ command.viewMode == ChartViewEnum.day }">
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'Date', type: 'datetime'},
	   		 {label: 'Heures creuses (Wh)', type: 'number'},
	   		 {label: 'Heures pleines (Wh)', type: 'number'},
	   		 {label: 'Intensité (A)', type: 'number'},
	   		],
	   		<g:each var="map" in="${ datas?.groupBy{ it.dateValue } }">
	   			[new Date(<g:formatDate date="${ map.key}" format="yyyy,${map.key.getAt(Calendar.MONTH)},d,HH,m,s,0"/>),  
	   				${map.value.find({ it.name == 'hcinst' })?.value},
	   				${map.value.find({ it.name == 'hpinst' })?.value},
	   				${map.value.find({ !it.name })?.value},
	   			],
	   		</g:each>
	   	]);
	    	
		chartOptions = {
			  'title': '${label }',
			  'pointSize': '2',
		      'width': '${ params.chartWidth ?: '100%' }',
	          'height': '${ params.chartHeight ?: '600' }',
	          legend: {position: 'top'},
	          'series': {
	          	0: {targetAxisIndex: 0},
	          	1: {targetAxisIndex: 0},
	          	2: {targetAxisIndex: 1},
	          },
	          vAxes: {
	          	0: {title: 'Index (Wh)'},
	          	1: {title: 'Intensité (A)'}
	          },
		      'chartArea': {
		      	width: '90%'
		      }
	  	};
	  	
	  	chartType = 'LineChart';
	</g:if>
	<g:elseif test="${ command.viewMode == ChartViewEnum.month }">
		var datas = google.visualization.arrayToDataTable([
	   		[{label: 'Date', type: 'datetime'},
	   		 {label: 'Heures creuses (kWh)', type: 'number'},
	   		 {label: 'Heures pleines (kWh)', type: 'number'},
	   		 {label: 'Intensité max (A)', type: 'number'},
	   		],
	   		<g:each var="data" in="${ datas?.groupBy{ it.day } }">
	   			<g:set var="hc" value="${ data.value.find({ it.name == 'hchc' }) }"/>
	   			<g:set var="hp" value="${ data.value.find({ it.name == 'hchp' }) }"/>
	   			
	   			[new Date(<g:formatDate date="${ data.key}" format="yyyy,${data.key.getAt(Calendar.MONTH)},d"/>),  
	   				${ (((hc?.max ?: 0) - (hc?.min ?: 0))  / 1000 as Double).round(1) },
	   				${ (((hp?.max ?: 0) - (hp?.min ?: 0))  / 1000 as Double).round(1) },
	   				${ data.value.find({ !it.name })?.max ?: 0 },
	   			],
	   		</g:each>
	   	]);
	   	
	   	chartDatas = new google.visualization.DataView(datas);
      	chartDatas.setColumns([0,
      					1,{ calc: "stringify",
                         sourceColumn: 1,
                         type: "string",
                         role: "annotation" },
                       2,{ calc: "stringify",
                         sourceColumn: 2,
                         type: "string",
                         role: "annotation" },
                       3,{ calc: "stringify",
                         sourceColumn: 3,
                         type: "string",
                         role: "annotation" }]);
	    	
		chartOptions = {
			  'title': '${label }',
		      'width': '${ params.chartWidth ?: '100%' }',
	          'height': '${ params.chartHeight ?: '600' }',
	          legend: {position: 'top'},
	          isStacked: true,
	          'chartArea': {
		      	width: '90%'
		      },
		      'series': {
	          	0: {targetAxisIndex: 0},
	          	1: {targetAxisIndex: 0},
	          	2: {targetAxisIndex: 1, type: 'line'},
	          },
	          vAxes: {
	          	0: {title: 'Index (kWh)'},
	          	1: {title: 'Intensité (A)'}
	          },
	          'seriesType': 'bars',
	  	};
	  	
	  	chartType = 'ColumnChart';
	</g:elseif>
	<g:elseif test="${ command.viewMode == ChartViewEnum.year }">
		var datas = google.visualization.arrayToDataTable([
	   		[{label: 'Date', type: 'datetime'},
	   		 {label: 'Heures creuses (kWh)', type: 'number'},
	   		 {label: 'Heures pleines (kWh)', type: 'number'},
	   		 {label: 'Intensité max (A)', type: 'number'},
	   		],
   			<g:each var="data" in="${ datas?.groupBy{ (it.year * 100) + it.month } }">
   				<g:set var="hc" value="${ data.value.find({ it.name == 'hchc' }) }"/>
	   			<g:set var="hp" value="${ data.value.find({ it.name == 'hchp' }) }"/>
	   			
	   			[new Date(${ data.value[0].year }, ${ data.value[0].month-1 }, 1),  
	   				${ (((hc?.max ?: 0) - (hc?.min ?: 0))  / 1000 as Double).round(1) },
	   				${ (((hp?.max ?: 0) - (hp?.min ?: 0))  / 1000 as Double).round(1) },
	   				${ data.value.find({ !it.name })?.max ?: 0 },
	   			],
	   		</g:each>
	   	]);
	   	
	   	chartDatas = new google.visualization.DataView(datas);
	   	
		chartOptions = {
			  'title': '${label }',
		      'width': '${ params.chartWidth ?: '100%' }',
	          'height': '${ params.chartHeight ?: '600' }',
	          legend: {position: 'top'},
	          isStacked: true,
	          'chartArea': {
		      	width: '90%'
		      },
		      'series': {
	          	0: {targetAxisIndex: 0},
	          	1: {targetAxisIndex: 0},
	          	2: {targetAxisIndex: 1, type: 'line'},
	          },
	          vAxes: {
	          	0: {title: 'Index (kWh)'},
	          	1: {title: 'Intensité (A)'}
	          },
	          'seriesType': 'bars',
	  	};
	  	
	  	chartType = 'ColumnChart';
	</g:elseif>
	
</div>