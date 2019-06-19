<html>
<head>
<meta name='layout' content='authenticated-chart' />
</head>

<body onload="onLoadDeviceChart();">
	<g:applyLayout name="applicationHeader">
		<g:form name="navigation-chart-form" action="productionChart" class="aui">
			<h3>Production investissement participatif</h3>
		
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
	
	</g:applyLayout>
	
</body>
</html>