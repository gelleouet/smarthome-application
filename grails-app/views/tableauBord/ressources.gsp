<html>
<head>
<meta name='layout' content='main' />
</head>

<body onload="">
	<g:applyLayout name="page-default">
	
		<h3 class="mb-3">Ressources</h3>
		
		
		<div class="row">
			<div class="col-12 col-xl-6">
				<div class="card">
					<div class="card-header">
						<div class="card-actions float-right">
						</div>
						<h3><app:icon name="users"/> ECODO - Les ambassadrices</h3>
					</div> <!-- div.card-header -->
				
					<div class="card-body">
						<asset:image src="/ambassadrices.png" width="560"/>
						<ul>
							<li>Site : <a href="${ grailsApplication.config.social.web }">${ grailsApplication.config.social.web }</a></li>
							<li>Email : <a href="mailto:ambasadeur-ecodo@ebr-collectivite.fr">ambasadeur-ecodo@ebr-collectivite.fr</a></li>
							<li>Téléphone : 06 11 70 81 92</li>
						</ul>
					</div> <!-- div.card-body -->
				</div> <!-- div.card -->
			</div> <!-- div.col -->
			
			<div class="col-12 col-md-6">
				<div class="card">
					<div class="card-header">
						<div class="card-actions float-right">
						</div>
						<h3><app:icon name="youtube" lib="fa" class="fab"/> ECODO - Les écogestes</h3>
					</div> <!-- div.card-header -->
				
					<div class="card-body">
						<iframe width="560" height="315" src="https://www.youtube.com/embed/o6gyzmXweNs" title="ECODO - Les écogestes" 
							frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen>
						</iframe>
					</div> <!-- div.card-body -->
				</div> <!-- div.card -->
			</div> <!-- div.col -->
		</div> <!-- div.row -->
		
		
		<div class="row">
			<div class="col-12 col-xl-6">
				<div class="card">
					<div class="card-header">
						<div class="card-actions float-right">
						</div>
						<h3><app:icon name="youtube" lib="fa" class="fab"/> ECODO - Comment installer un mousseur sur vos robinets ?</h3>
					</div> <!-- div.card-header -->
				
					<div class="card-body">
						<iframe width="560" height="315" src="https://www.youtube.com/embed/alevmoEKyN8" title="ECODO - Comment installer un mousseur sur vos robinets ?" 
							frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen>
						</iframe>
					</div> <!-- div.card-body -->
				</div> <!-- div.card -->
			</div> <!-- div.col -->
			
			<div class="col-12 col-md-6">
				<div class="card">
					<div class="card-header">
						<div class="card-actions float-right">
						</div>
						<h3><app:icon name="youtube" lib="fa" class="fab"/> ECODO - Douchette ou réducteur de débit ?</h3>
					</div> <!-- div.card-header -->
				
					<div class="card-body">
						<iframe width="560" height="315" src="https://www.youtube.com/embed/rDCSY_d5xVM" title="ECODO - Douchette ou réducteur de débit ?" 
							frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen>
						</iframe>
					</div> <!-- div.card-body -->
				</div> <!-- div.card -->
			</div> <!-- div.col -->
		</div> <!-- div.row -->
		
		
		<div class="row">
			<div class="col-12 col-xl-6">
				<div class="card">
					<div class="card-header">
						<div class="card-actions float-right">
						</div>
						<h3><app:icon name="droplet"/> ECODO - Dispositifs d'économie d'eau</h3>
					</div> <!-- div.card-header -->
				
					<div class="card-body">
						<a href="${ assetPath(src: 'PlaquetteECODO-A4-2018.pdf') }" target="plaquette">
							<asset:image src="/plaquette1.png" width="560"/>
						</a>
					</div> <!-- div.card-body -->
				</div> <!-- div.card -->
			</div> <!-- div.col -->
			<div class="col-12 col-xl-6">
				<div class="card">
					<div class="card-header">
						<div class="card-actions float-right">
						</div>
						<h3><app:icon name="droplet"/> ECODO - Des gestes simples</h3>
					</div> <!-- div.card-header -->
				
					<div class="card-body">
						<a href="${ assetPath(src: 'PlaquetteECODO-A4-2018.pdf') }" target="plaquette">
							<asset:image src="/plaquette2.png" width="560"/>
						</a>
					</div> <!-- div.card-body -->
				</div> <!-- div.card -->
			</div> <!-- div.col -->
			
		</div> <!-- div.row -->
		
		
	</g:applyLayout>
</body>
</html>