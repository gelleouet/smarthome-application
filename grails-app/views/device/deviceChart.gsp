<html>
<head>
<meta name='layout' content='authenticated' />
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
</head>

<body>
	<g:applyLayout name="applicationContent">
		<g:render template="/chart/chartToolbar"/>
			
		<div id="chartDiv" data-chart-type="${ command.deviceImpl.defaultChartType() }">
			<br/>
			<h6>Loading chart...</h6>
			<div class="aui-progress-indicator">
			    <span class="aui-progress-indicator-value"></span>
			</div>
			<g:render template="${ command.deviceImpl.chartDataTemplate() }" model="[label: command.device.label, datas: datas]"/>
		</div>
	</g:applyLayout>
	
	<asset:script type="text/javascript">
		google.load("visualization", "1.0", {packages:["corechart"]});
		google.setOnLoadCallback(buildGoogleCharts);
	</asset:script>
	
</body>
</html>