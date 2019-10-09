<%@ page import="smarthome.automation.ChartViewEnum" %>
<%@ page import="smarthome.core.ChartUtils" %>
<%@ page import="smarthome.core.chart.GoogleChart" %>

<g:set var="hasChartAction" value="${ deviceOwner && sec.ifAnyGranted(roles: 'ROLE_ADMIN') }"/>

<div data-chart-datas="true" class="d-none" data-on-build-chart="onBuildQualitatifChart"
	data-url-delete-value="${ hasChartAction ? g.createLink(controller: 'device', action: 'deleteDeviceValue') : '' }"
	data-url-change-value="${ hasChartAction ? g.createLink(controller: 'device', action: 'dialogDeviceValue') : '' }"
	data-immediate="true">	

   	chartDatas = new google.visualization.DataTable(${ raw(chart.toJsonDataTable().toString(false)) });
   	chartDatas = new google.visualization.DataView(chartDatas)
   	
   	<g:if test="${ chart.joinChart }">
   		chartJoinDatas = new google.visualization.DataTable(${ raw(chart.joinChart.toJsonDataTable().toString(false)) });	
   	</g:if>
   	
   	<g:render template="/chart/google/annotation" model="[chart: chart]"/>
   	
	chartOptions = {
		<g:if test="${ chart?.title }">
		'title': '${ chart.title }',
		'titleTextStyle': {fontSize: 'auto'},
		</g:if>
		'width': '${ params.chartWidth ?: '100%' }',
        'height': '${ params.chartHeight ?: '600' }',
        'legend': {position: 'top'},
        'curveType': 'function',
        'chartArea': {
        	width: '90%'
        },
        'tooltip': {
			'isHtml': true,
			'trigger': 'selection'
		},
		'explorer': {
			'axis': 'horizontal',
			'actions': ['dragToZoom', 'rightClickToReset']
		},
		'series': {
			<g:render template="/chart/google/series" model="[chart: chart]"/>
		},
		'vAxes': {
			<g:render template="/chart/google/vaxis" model="[chart: chart]"/>
	    },
	    'hAxis': {
	    	title: '${ chart.hAxisTitle(command) }',
	    	gridlines: { color: 'none'},
		    slantedText: true,
		    format: '${ new GoogleChart().format(command) }',
	        ticks: [${ new GoogleChart().ticks(command) }]
	    }
	    <g:if test="${ chart.joinChart }">
	    'interpolateNulls': true,
	    </g:if>
	}
	
	<g:if test="${ chart.chartType }">
		chartType = '${ chart.chartType }'
	</g:if>
</div>


