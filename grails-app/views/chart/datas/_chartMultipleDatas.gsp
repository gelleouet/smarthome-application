<%@ page import="smarthome.automation.ChartViewEnum" %>
<%@ page import="smarthome.core.ChartUtils" %>

<div data-chart-datas="true" class="hidden">	
	<g:set var="keys" value="${ command.chart.devices.sort{ it.position} }"/>

	<g:if test="${ command.viewMode == ChartViewEnum.day }">
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'Date', type: 'datetime'},
	   		<g:each var="item" in="${ keys }">
	   		{label: '${ item.metavalue ? item.device.metavalue(item.metavalue).label : item.device.label }', type: 'number'},
	   		</g:each>
	   		],
	   		<g:each var="item" in="${ keys }" status="status">
		   		<g:each var="data" in="${datas[item]}">
		   			[new Date(<g:formatDate date="${data.dateValue}" format="yyyy,${data.dateValue.getAt(Calendar.MONTH)},d,H,m,s,0"/>),${ChartUtils.jsonArrayValue(data.value, status, keys.size())}],
		   		</g:each>
	   		</g:each>
	   	]);
	</g:if>
	<g:elseif test="${ command.viewMode == ChartViewEnum.month }">
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'Date', type: 'date'},
	   		<g:each var="item" in="${ keys }">
	   		{label: '${ item.metavalue ? item.device.metavalue(item.metavalue).label : item.device.label }', type: 'number'},
	   		</g:each>
	   		],
	   		<g:each var="data" in="${ChartUtils.groupMapDatas(command, datas)}">
	   			[new Date(<g:formatDate date="${data.day}" format="yyyy,${data.day.getAt(Calendar.MONTH)},d"/>),${ChartUtils.jsonArrayValue(data, keys)}],
	   		</g:each>
	   	]);
	</g:elseif>
	<g:elseif test="${ command.viewMode == ChartViewEnum.year }">
		chartDatas = google.visualization.arrayToDataTable([
	   		[{label: 'Date', type: 'date'},
	   		<g:each var="item" in="${ keys }">
	   		{label: '${ item.metavalue ? item.device.metavalue(item.metavalue).label : item.device.label }', type: 'number'},
	   		</g:each>
	   		],
	   		<g:each var="data" in="${ChartUtils.groupMapDatas(command, datas)}">
	   			[new Date(${data.year},${data.month-1},1),${ChartUtils.jsonArrayValue(data, keys)}],
	   		</g:each>
	   	]);
	</g:elseif>
   	
	chartOptions = {
		'pointSize': '2',
		'width': '${ params.chartWidth ?: '100%' }',
        'height': '${ params.chartHeight ?: '600' }',
        'legend': {position: 'top'},
        'curveType': 'function',
        'chartArea': {
        	width: '90%'
        },
        'series': {
        	<g:each var="item" in="${ keys }" status="status">
	          	${status}: {type: '${ item.chartType }'},
	        </g:each>
	    },
	}
</div>


