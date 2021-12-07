<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<g:render template="/grandDefi/defiMenu"/>
		
		<div class="card flex-fill w-100">
			<div class="card-body">
				<h4><app:icon name="bar-chart-2"/> Consommations globales</h4>
				<g:render template="/grandDefi/grandDefi2021Model/evolutionEquipe" model="[data: global, defi: currentDefi]"/>	
			</div>
		</div>
		
		<div class="card flex-fill w-100">
			<div class="card-body">
				<h4><asset:image src="linky.png" height="40px" width="40px" class="img-thumbnail rounded"/>
				<app:icon name="zap"/> Consommations d'électricité</h4>
				<g:render template="/grandDefi/grandDefi2021Model/resultatEquipe" model="[data: electricite, defi: currentDefi]"/>					
			</div>
		</div>
		
		<div class="card flex-fill w-100">
			<div class="card-body">
				<h4><asset:image src="gazpar.png" height="45px" width="45px" class="img-thumbnail rounded"/>
				<app:icon name="fire" lib="awesome"/> Consommations de gaz</h4>
				<g:render template="/grandDefi/grandDefi2021Model/resultatEquipe" model="[data: gaz, defi: currentDefi]"/>	
			</div>
		</div>
		
		<div class="card flex-fill w-100">
			<div class="card-body">
				<h4><asset:image src="compteur-eau.jpg" height="45px" width="45px" class="img-thumbnail rounded"/>
				<app:icon name="droplet"/> Consommations d'eau</h4>
				<g:render template="/grandDefi/grandDefi2021Model/resultatEquipe" model="[data: eau, defi: currentDefi]"/>	
			</div>
		</div>
		
	</g:applyLayout>
</body>
</html>