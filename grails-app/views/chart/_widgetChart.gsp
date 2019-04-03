<%@ page import="smarthome.automation.ChartTypeEnum" %>

<h3>${ chart.label }</h3>

<div id="chartDiv-${ chart.id }" data-chart-type="${ ChartTypeEnum.valueOf(chart.chartType).factory }">
	<div>
		<g:render template="/chart/datas/chartMultipleDatas"
			model="[command: command, datas: datas]"/>
	</div>
</div>

<div style="text-align:right; font-weight:bold;">
	<g:link class="link" action="chartsGrid" controller="chart" params="[groupe: chart.groupe]">
		<span class="aui-icon aui-icon-small aui-iconfont-arrows-right"></span> Voir le détail
	</g:link>
</div>