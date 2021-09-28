<%@ page import="smarthome.automation.ChartViewEnum" %>
<%@ page import="smarthome.core.ChartUtils" %>
<%@ page import="smarthome.core.chart.GoogleChart" %>

<g:set var="hasChartAction" value="${ deviceOwner && sec.ifAnyGranted(roles: 'ROLE_ADMIN') }"/>

<div data-chart-datas="true" class="hidden" data-on-build-chart="onBuildQualitatifChart"
	data-url-delete-value="${ hasChartAction ? g.createLink(controller: 'device', action: 'deleteDeviceValue') : '' }"
	data-url-change-value="${ hasChartAction ? g.createLink(controller: 'device', action: 'dialogDeviceValue') : '' }"
	data-immediate="true">	

   	chartDatas = new google.visualization.DataTable(${ raw(chart.toJsonDataTable().toString(false)) });
   	
   	<g:if test="${ chart.joinChart }">
   		chartJoinDatas = new google.visualization.DataTable(${ raw(chart.joinChart.toJsonDataTable().toString(false)) });	
   	</g:if>
   	
	chartOptions = {
		<g:if test="${ chart?.title }">
		'title': '${ chart.title }',
		'titleTextStyle': {fontSize: '20'},
		</g:if>
		<g:else>
		'titlePosition': 'none',
		</g:else>
		'width': '${ params.chartWidth ?: '100%' }',
        'height': '${ params.chartHeight ?: (chartHeight ?: '600') }',
        'legend': {position: '${ chart.showLegend ? 'top' : 'none' }'},
        'curveType': 'function',
        'axisTitlesPosition': 'none',
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
			<g:each var="serie" in="${ chart.series }" status="status">
				${ status }: {color: '${ serie.color }', lineDashStyle: ${ chart.lineDashStyle(serie) },
					type: '${ serie.type ?: "line" }', targetAxisIndex: ${ serie.axisIndex ?: 0}},
			</g:each>
		},
		'vAxes': {
			<g:each var="axis" in="${ chart.vAxis }" status="status">
				${ status }: {title: '${ axis.title }', maxValue: '${ axis.maxValue ?: 'automatic' }'},
			</g:each>
	    },
	    'hAxis': {
	    	textPosition: '${ chart.showHAxis ? 'out' : 'none' }',
	    	title: '${ chart.showHAxis ? chart.hAxisTitle(command) : '' }',
	    	gridlines: { color: 'none'},
		    slantedText: true,
		    format: '${ new GoogleChart().format(command) }',
	        ticks: [${ new GoogleChart().ticks(command) }]
	    },
	    <g:if test="${ chart.joinChart }">
	    'interpolateNulls': true,
	    </g:if>
	}
	
	<g:if test="${ chart.chartType }">
		chartType = '${ chart.chartType }'
	</g:if>
</div>


