<%@ page import="smarthome.automation.ChartTypeEnum" %>

<html>
<head>
<meta name='layout' content='authenticated-chart' />
</head>

<body onload="onLoadDeviceChart();">
	<g:applyLayout name="applicationHeader">
		<g:form name="navigation-chart-form" action="investissementChart" class="aui">
			<div class="aui-group aui-group-split">
				<div class="aui-item">
					<h3>Répartition investissement citoyen</h3>
				</div>
				<div class="aui-item">
				</div>
			</div>	
		</g:form>
	</g:applyLayout>


	<g:applyLayout name="applicationContent">
	
		<div class="aui-group">
			<div class="aui-item">
				<div id="chart-repartition-investissement-participatif-part" data-chart-type="${ ChartTypeEnum.Pie.factory }">
					<div data-chart-datas="true" class="hidden">	
						chartDatas = google.visualization.arrayToDataTable([
					   		['Producteur', 'Part'],
					   		<g:each var="action" in="${ actions }" status="status">
						   		['${ action.producteur.libelle }', ${ action.nbaction }],
					   		</g:each>
					   	]);
					   	
						chartOptions = {
							title: 'Répartition part',
							'width': '100%',
					        'height': '400',
					        pieHole: 0.5
						}
					</div>
				</div>
			</div> <!-- div.aui-item -->
			
			<div class="aui-item">
				<div id="chart-repartition-investissement-participatif-prix" data-chart-type="${ ChartTypeEnum.Pie.factory }">
					<div data-chart-datas="true" class="hidden">	
						chartDatas = google.visualization.arrayToDataTable([
					   		['Producteur', 'Investissement', {role: 'tooltip'}],
					   		<g:each var="action" in="${ actions }" status="status">
						   		['${ action.producteur.libelle }', ${ action.investissement() }, '${ action.producteur.libelle} : ${ g.formatNumber(number: action.investissement(), format:"0.##") }€'],
					   		</g:each>
					   	]);
					   	
						chartOptions = {
							title: 'Répartition investissement (€)',
							'width': '100%',
					        'height': '400',
					        pieHole: 0.5
						}
					</div>
				</div>
			</div> <!-- div.aui-item -->
		</div>
		
		
		
		<div class="aui-group">
			<div class="aui-item">
				<div id="chart-repartition-investissement-participatif-surface" data-chart-type="${ ChartTypeEnum.Pie.factory }">
					<div data-chart-datas="true" class="hidden">	
						chartDatas = google.visualization.arrayToDataTable([
					   		['Producteur', 'Surface', {role: 'tooltip'}],
					   		<g:each var="action" in="${ actions }" status="status">
						   		['${ action.producteur.libelle }', ${ action.surface() }, '${ action.producteur.libelle} : ${ g.formatNumber(number: action.surface(), format:"0.##") }m²'],
					   		</g:each>
					   	]);
					   	
						chartOptions = {
							title: 'Répartition surface (m²)',
							'width': '100%',
					        'height': '400',
					        pieHole: 0.5
						}
					</div>
				</div>
			</div> <!-- div.aui-item -->
			
			<div class="aui-item">
				<div id="chart-repartition-investissement-participatif-production" data-chart-type="${ ChartTypeEnum.Pie.factory }">
					<div data-chart-datas="true" class="hidden">	
						chartDatas = google.visualization.arrayToDataTable([
					   		['Producteur', 'Production', {role: 'tooltip'}],
					   		<g:each var="action" in="${ actions }" status="status">
						   		['${ action.producteur.libelle }', ${ action.production() }, '${ action.producteur.libelle} : ${ g.formatNumber(number: action.production(), format:"0.##") }kWh'],
					   		</g:each>
					   	]);
					   	
						chartOptions = {
							title: 'Répartition production (kWh)',
							'width': '100%',
					        'height': '400',
					        pieHole: 0.5
						}
					</div>
				</div>
			</div> <!-- div.aui-item -->
		</div>
	
		
	</g:applyLayout>
	
</body>
</html>