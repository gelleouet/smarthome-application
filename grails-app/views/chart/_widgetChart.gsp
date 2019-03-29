<%@ page import="smarthome.automation.ChartTypeEnum" %>

<h3>${ chart.label }</h3>

<div id="chartDiv-${ chart.id }" data-chart-type="${ ChartTypeEnum.valueOf(chart.chartType).factory }">
	<div>
		<g:render template="/chart/datas/chartMultipleDatas" model="[command: command, datas: datas]"/>
	</div>
</div>