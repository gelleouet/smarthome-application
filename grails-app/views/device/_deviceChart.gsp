<div id="chartDiv" data-chart-type="${ command.deviceImpl.defaultChartType().factory }">
	<br/>
	<h6 class="h6">Loading chart...</h6>
	<div class="aui-progress-indicator">
	    <span class="aui-progress-indicator-value"></span>
	</div>
	<g:render template="${ command.deviceImpl.chartDataTemplate() }"
		model="[label: command.device.label, datas: datas, chart: chart]"/>
</div>
