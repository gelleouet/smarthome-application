<%@ page import="smarthome.automation.ChartViewEnum" %>
<%@ page import="smarthome.core.ChartUtils" %>
<%@ page import="smarthome.core.chart.GoogleChart" %>

<g:set var="hasChartAction" value="${ deviceOwner && sec.ifAnyGranted(roles: 'ROLE_ADMIN') }"/>

<div data-chart-datas="true" class="d-none" data-on-build-chart="onBuildQualitatifChart"
	data-url-delete-value="${ hasChartAction ? g.createLink(controller: 'device', action: 'deleteDeviceValue') : '' }"
	data-url-change-value="${ hasChartAction ? g.createLink(controller: 'device', action: 'dialogDeviceValue') : '' }"
	data-immediate="true" data-selection-field="${ chart.selectionField }">	

   	chartDatas = new google.visualization.DataTable(${ raw(chart.toJsonDataTable().toString(false)) });
   	
   	<g:if test="${ chart.joinChart }">
   		chartJoinDatas = new google.visualization.DataTable(${ raw(chart.joinChart.toJsonDataTable().toString(false)) });	
   	</g:if>
   	
   	<g:if test="${ compareChart }">
		chartOldDatas = new google.visualization.DataTable(${ raw(compareChart.toJsonDataTable().toString(false)) })
	</g:if>
	<g:else>
		<g:render template="/chart/google/annotation" model="[chart: chart]"/>
	</g:else>
   	
   	
	chartOptions = {
		<g:if test="${ chart?.title }">
		'title': '${ chart.title }',
		'titleTextStyle': {fontSize: 'auto'},
		</g:if>
		'width': '${ params.chartWidth ?: '100%' }',
        'height': '${ params.chartHeight ?: '600' }',
        'legend': {position: 'top'},
        'selectionMode': 'multiple',
        'curveType': '${ chart.curveType ?: 'none' }',
        'chartArea': {
        	width: '85%'
        },
        'tooltip': {
			'isHtml': true,
			'trigger': 'focus'
		},
		'explorer': {
			'axis': 'horizontal',
			keepInBounds: true,
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
		    slantedText: ${ chart.slantedText },
		    format: '${ chart.hAxisFormat ?: chart.format(command) }',
	        ticks: [${ chart.hAxisTicks ?: chart.ticks(command) }]
	    },
	    
	    <g:if test="${ chart.joinChart }">
	    'interpolateNulls': true,
	    </g:if>
	   
	    <g:if test="${ compareChart }">
	    diff: {
		  	oldData: {
		  		tooltip: {
		  			prefix: null
		  		}
		  	},
		  	newData: {
		  		tooltip: {
		  			prefix: null
		  		}
		  	},
		},
		</g:if>
	}
	
	<g:if test="${ chart.chartType }">
		chartType = '${ chart.chartType }'
	</g:if>
</div>


