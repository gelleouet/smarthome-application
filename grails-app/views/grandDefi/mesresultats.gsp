<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<div class="row mb-3">
			<div class="col-8">
				<h3>${ currentDefi?.libelle ?: 'Mes défis' }
					<g:if test="${ false }">
					 	<span class="text-muted" style="font-size:small;">du <app:formatUser date="${ currentDefi.actionDebut }"/> au <app:formatUser date="${ currentDefi.actionFin }"/></span>
					</g:if>
				</h3>
			</div>
			<div class="col-4">
				<div class="btn-toolbar justify-content-end">
					<div class="btn-group">
						<button class="btn btn-primary dropdown-toggle" data-toggle="dropdown">Autres défis</button>
						<div class="dropdown-menu">
							<g:each var="defi" in="${ defis?.sort { it.libelle } }">
								<g:link class="dropdown-item" action="defis" params="['defi.id': defi.id]">${ defi.libelle }</g:link>
							</g:each>
						</div>
					</div>
				</div>
			</div>
		</div>
		
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
		
			<g:render template="defiNavigation"/>
		
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