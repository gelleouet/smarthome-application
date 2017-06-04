<%@ page import="smarthome.automation.ChartViewEnum" %>

<div data-chart-datas="true" class="hidden">	

	<g:if test="${ command.viewMode == ChartViewEnum.day }">
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'Date', type: 'datetime'},
	   		 {label: 'Valeur', type: 'number'}],
	   		<g:each var="data" in="${datas}">
	   			[new Date(<g:formatDate date="${data.dateValue}" format="yyyy,${data.dateValue.getAt(Calendar.MONTH)},d,H,m,s,0"/>),  ${data.value}],
	   		</g:each>
	   	]);
	</g:if>
	<g:elseif test="${ command.viewMode == ChartViewEnum.month }">
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'Date', type: 'date'},
	   		 {label: 'Min', type: 'number'},
	   		 {label: 'Max', type: 'number'},
	   		 {label: 'Moyenne', type: 'number'}],
	   		 <g:each var="data" in="${datas}">
	   			[new Date(<g:formatDate date="${data.day}" format="yyyy,${data.day.getAt(Calendar.MONTH)},d"/>),  ${data.min}, ${data.max}, ${data.avg}],
	   		</g:each>
	   	]);
	</g:elseif>
	<g:elseif test="${ command.viewMode == ChartViewEnum.year }">
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'Date', type: 'date'},
	   		 {label: 'Min', type: 'number'},
	   		 {label: 'Max', type: 'number'},
	   		 {label: 'Moyenne', type: 'number'}],
	   		 <g:each var="data" in="${datas}">
	   			[new Date(${data.year},${data.month-1},1),  ${data.min}, ${data.max}, ${data.avg}],
	   		</g:each>
	   	]);
	</g:elseif>
   	
	chartOptions = {
		'title': '${label }',
		'pointSize': '2',
		'width': '${ params.chartWidth ?: '100%' }',
        'height': '${ params.chartHeight ?: '600' }',
        'legend': {position: 'top'},
        'curveType': 'function',
        'chartArea': {
        	width: '90%'
        }
	}
</div>


