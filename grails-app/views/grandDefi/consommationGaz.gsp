<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<div class="row">
			<div class="col d-flex">
				<div class="card flex-fill w-100">
					<div class="card-body">
						<g:render template="/widget/syntheseConsommationMonth"
							model="[compteur: house?.compteurGaz, title: 'Consommation de gaz du mois',
								icon: 'fire', iconLib: 'awesome', typeCompteur: 'gaz']"/>
					</div>
				</div>
			</div>
		</div>
		
	</g:applyLayout>
</body>
</html>