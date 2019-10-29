<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<g:render template="defiMenu"/>
		
		<div class="card flex-fill w-100">
			<div class="card-body">
				<h4><app:icon name="bar-chart-2"/> Consommations globales</h4>
				<g:render template="resultatUser" model="[data: global, defi: currentDefi,
					participant: participant]"/>	
			</div>
		</div>
		
		<div class="card flex-fill w-100">
			<div class="card-body bg-linky">
				<h4><app:icon name="zap"/> Consommations d'électricité</h4>
				<g:render template="resultatUser" model="[data: electricite, defi: currentDefi,
					participant: participant]"/>					
			</div>
		</div>
		
		<div class="card flex-fill w-100">
			<div class="card-body">
				<h4><app:icon name="fire" lib="awesome"/> Consommations de gaz</h4>
				<g:render template="resultatUser" model="[data: gaz, defi: currentDefi,
					participant: participant]"/>	
			</div>
		</div>
		
	</g:applyLayout>
</body>
</html>