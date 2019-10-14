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
						Vous n'êtes enregistrés sur aucun défi.
					</g:applyLayout>
				</div>
			</div>
		</g:if>
		<g:elseif test="${ ! resultat?.canDisplay() }">
			<div class="card flex-fill w-100">
				<div class="card-body">
					<g:applyLayout name="messageWarning">
						Les résultats ne sont pas encore calculés.
					</g:applyLayout>
				</div>
			</div>
		</g:elseif>
		
		<div class="card flex-fill w-100">
			<div class="card-body">
				<h4><app:icon name="bar-chart-2"/> Consommations globales</h4>
				<g:render template="resultatEquipe" model="[data: global, defi: currentDefi]"/>	
			</div>
		</div>
		
		<div class="card flex-fill w-100">
			<div class="card-body">
				<h4><app:icon name="zap"/> Consommations d'électricité</h4>
				<g:render template="resultatEquipe" model="[data: electricite, defi: currentDefi]"/>					
			</div>
		</div>
		
	</g:applyLayout>
</body>
</html>