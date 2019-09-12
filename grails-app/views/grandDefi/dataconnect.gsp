<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<h3 class="mb-3">DataConnect Enedis</h3>
		
		<div class="card">
			<div class="card-body">
				<h5 class="font-weight-bold">Enedis gère le réseau d’électricité jusqu’au compteur d’électricité.
					Pour consulter vos consommations, autorisez Enedis à nous transmettre vos données Linky.</h5>
		
				<p>En cliquant sur le bouton "J'accède à mon espace client", vous allez accéder à votre compte personnel
						Enedis où vous pourrez donner votre accord pour qu’Enedis nous transmette vos données.</p>
						
				<p>Pour donner votre autorisation, vous devez créer un compte personnel Enedis. Il vous permet également de suivre et gérer vos données de consommation d’électricité.
				Munissez-vous de votre facture d’électricité pour créer votre espace.<p>
		
				<h6>Enedis est le gestionnaire du réseau public de distribution d’électricité sur 95% du territoire français continental.</h6>
				
				<div class="text-center mt-4">
					<g:link action="authorize" controller="dataConnect">
						<asset:image src="vert-enedis.png" width="200px"/>
					</g:link>
					
					<g:link action="compteur" class="btn btn-link">Annuler</g:link>
				</div>
			</div>
		</div>
		
	</g:applyLayout>
</body>
</html>