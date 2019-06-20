<html>
<head>
<meta name='layout' content='authenticated-chart' />
</head>

<body onload="onLoadDeviceChart();">
	<g:applyLayout name="applicationHeader">
		<g:form name="navigation-chart-form" action="productionChart" class="aui">
			<h3>Production investissement citoyen</h3>
		
			<div class="aui-group aui-group-split">
				<div class="aui-item">
					<g:render template="/chart/chartToolbar"/>
				</div>
				<div class="aui-item">

				</div>
			</div>	
		</g:form>
	</g:applyLayout>


	<g:applyLayout name="applicationContent">
	
		<div id="chart-production-investissement" data-chart-type="${ chart.chartType }">
			<g:render template="/chart/datas/chartQualitatifDatas" model="[chart: chart, command: command]"/>
		</div>
		
		<g:each var="actionChart" in="${ chart.actionCharts }" status="status">
			<div id="chart-production-investissement-${ status }" data-chart-type="${ actionChart.chartType }">
				<g:render template="/chart/datas/chartQualitatifDatas" model="[chart: actionChart, command: command]"/>
			</div>
		</g:each>
	
	</g:applyLayout>
	
</body>
</html>