<%@ page import="smarthome.automation.ChartTypeEnum" %>

<html>
<head>
<meta name='layout' content='authenticated' />
<script type="text/javascript" src="https://www.google.com/jsapi"></script>
</head>

<body>
	<g:applyLayout name="applicationContent">
	
		<g:if test="${ !chartInstanceList?.size() }">
			<div class="aui-message">
				<h6>Vous n'avez pas encore de graphiques enregistrés sur votre compte. Vous pouvez en créer <g:link action="create">en cliquant ici</g:link>
				</h6>
			</div>
		</g:if>
	
		<g:each var="groupe" in="${ chartInstanceList?.groupBy({ it.groupe })?.sort{ it.key } }">
			
			<h3 class="separator">${ groupe.key ?: 'Autres' }</h3>
		
			<g:each var="chart" in="${ groupe.value.sort{ it.label } }">
				<div class="chart-grid">
					<div class="chart-grid-header">
						<div class="chart-grid-header-title">
							<div>${ chart.label }</div>
						</div>
					</div>
					<div class="chart-grid-body">
						<div class="chart-grid-body-content">
							<div>
								<div class="chart-grid-body-user">
									<div id="chartDiv-${ chart.id }" data-chart-type="${ ChartTypeEnum.valueOf(chart.chartType).factory }">
										<h6>Loading chart...</h6>
										<div class="aui-progress-indicator">
										    <span class="aui-progress-indicator-value"></span>
										</div>
										<div async-url="${ createLink(action: 'chartPreview', id: chart.id, params: [chartHeight: 400]) }" on-async-complete="buildGoogleChart('#chartDiv-${ chart.id }')"></div>
									</div>
								</div>
							</div>
						</div>
						<div class="chart-grid-body-menu">
							<g:link action="edit" id="${ chart.id }" class="aui-button aui-button-subtle" title="Modifier"><span class="aui-icon aui-icon-small aui-iconfont-edit"></span></g:link>
							<g:link action="chartView" id="${ chart.id }" class="aui-button aui-button-subtle" title="Graphique"><span class="aui-icon aui-icon-small aui-iconfont-macro-gallery"></span></g:link>
						</div>
					</div>
				</div>
			</g:each>
		</g:each>
	
	</g:applyLayout>
	
	
	<asset:script type="text/javascript">
		google.load("visualization", "1.0", {packages:["corechart"]});
		// ne pas appeler la méthode car déjà déclenché à la suite des appels ajax
		//google.setOnLoadCallback(buildGoogleCharts);
	</asset:script>
</body>
</html>