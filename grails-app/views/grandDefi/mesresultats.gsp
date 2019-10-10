<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<g:render template="defiMenu"/>
		
		<g:if test="${ !currentDefi }">
			<div class="card flex-fill w-100">
				<div class="card-body">
					<g:applyLayout name="messageWarning">
						Vous n'êtes enregistrés sur aucun défi !
					</g:applyLayout>
				</div>
			</div>
		</g:if>
		<g:else>
			<div class="card flex-fill w-100">
				<div class="card-body">
					<h4><app:icon name="zap"/> Consommations d'électricité</h4>
					<g:render template="resultatUser" model="[chartId: currentDefi.id + '-electricite',
						chartTotal: electricite.chartTotal, chartConso: electricite.chartConso,
						consos: electricite.consos, error: electricite.error, defi: currentDefi]"/>					
				</div>
			</div>
			
			<div class="card flex-fill w-100">
				<div class="card-body">
					<h4><app:icon name="fire" lib="awesome"/> Consommations de gaz</h4>
					<g:render template="resultatUser" model="[chartId: currentDefi.id + '-gaz',
						chartTotal: gaz.chartTotal, chartConso: gaz.chartConso,
						consos: gaz.consos, error: gaz.error, defi: currentDefi]"/>	
				</div>
			</div>
			
			<div class="card flex-fill w-100">
				<div class="card-body">
					<h4><app:icon name="bar-chart-2"/> Consommations globales</h4>
					<g:render template="resultatUser" model="[chartId: currentDefi.id + '-global',
						chartTotal: global.chartTotal, chartConso: global.chartConso,
						consos: global.consos, error: '', defi: currentDefi]"/>	
				</div>
			</div>
		</g:else>
		
		
	</g:applyLayout>
</body>
</html>