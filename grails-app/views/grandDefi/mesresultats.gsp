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
				<div class="row">
					<div class="col">
						<h4><app:icon name="zap"/> Consommations d'électricité</h4>
					</div>
					<div class="col text-right">
						<asset:image src="linky.png" height="50px"/>
					</div>
				</div>
				<g:render template="resultatUser" model="[data: electricite, defi: currentDefi,
					participant: participant]"/>					
			</div>
		</div>
		
		<div class="card flex-fill w-100">
			<div class="card-body">
				<div class="row">
					<div class="col">
						<h4><app:icon name="fire" lib="awesome"/> Consommations de gaz</h4>
					</div>
					<div class="col text-right">
						<asset:image src="gazpar.png" height="50px"/>
					</div>
				</div>
				<g:render template="resultatUser" model="[data: gaz, defi: currentDefi,
					participant: participant]"/>	
			</div>
		</div>
		
	</g:applyLayout>
</body>
</html>