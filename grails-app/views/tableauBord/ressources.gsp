<html>
<head>
<meta name='layout' content='main' />
</head>

<body onload="">
	<g:applyLayout name="page-default">
	
		<h3 class="mb-3">Ressources</h3>
		
		<g:applyLayout name="messageInfo">
			Vous trouverez ici, suite au deuxième passage des ambassadrices, des indications, conseils et astuces pour économiser l’eau chez vous.
		</g:applyLayout>
		
		<div class="card">
			
			<div class="card-header">
				<div class="card-actions float-right">
				</div>
				<h3><app:icon name="users"/> Les ambassadrices</h3>
			</div> <!-- div.card-header -->
		
			<div class="card-body">
			
				<div class="row">
					<div class="col">
						<asset:image src="/ambassadrices.png" />
					</div>
					<div class="col">
						<h4>Contacts</h4>
						
						<ul>
							<li>Site : <a href="${ grailsApplication.config.social.web }">${ grailsApplication.config.social.web }</a></li>
							<li>Email : <a href="mailto:ambasadeur-ecodo@ebr-collectivite.fr">ambasadeur-ecodo@ebr-collectivite.fr</a></li>
							<li>Téléphone : 06 11 70 81 92</li>
						</ul>
					</div>
				</div>
				
			
			</div> <!-- div.card-body -->
			
		</div> <!-- div.card -->
		
	</g:applyLayout>
</body>
</html>