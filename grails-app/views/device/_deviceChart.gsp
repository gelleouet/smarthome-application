<div id="chartDiv-${ command.device.id }-${ suffixId }" data-chart-type="${ command.deviceImpl.defaultChartType().factory }">
	<br/>
	<h6 class="h6">Loading chart...</h6>
	<div class="aui-progress-indicator">
	    <span class="aui-progress-indicator-value"></span>
	</div>
	<g:render template="/chart/datas/chartDatas"
		model="[label: command.device.label, datas: chart.values, chart: chart, deviceOwner: (command.device.user.id == secUser?.id)]"/>
</div>
