<%@ page import="smarthome.automation.ChartViewEnum" %>

<div data-chart-datas="true" class="hidden" data-on-build-chart="onBuildQualitatifChart"
	data-url-delete-value="${ g.createLink(controller: 'device', action: 'deleteDeviceValue') }"
	data-url-change-value="${ g.createLink(controller: 'device', action: 'dialogDeviceValue') }" data-immediate="true">	

   	chartDatas = new google.visualization.DataTable(${ raw(chart.toJsonDataTable().toString(false)) });
   	
   	<g:if test="${ chart.joinChart }">
   		chartJoinDatas = new google.visualization.DataTable(${ raw(chart.joinChart.toJsonDataTable().toString(false)) });	
   	</g:if>
   	
	chartOptions = {
		'pointSize': '2',
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
	    <g:if test="${ chart.joinChart }">
	    'interpolateNulls': true,
	    </g:if>
	}
	
	<g:if test="${ chart.chartType }">
		chartType = '${ chart.chartType }'
	</g:if>
</div>

