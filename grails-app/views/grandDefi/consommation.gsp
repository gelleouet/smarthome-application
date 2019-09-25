<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<h3 class="mb-3">Ma consommation</h3>
		
		<div class="row">
			<div class="col d-flex">
				<div class="card flex-fill w-100">
					<div class="card-body">
						<g:render template="/widget/syntheseConsommationDay"
							model="[compteur: house?.compteur, title: 'Consommation d\'électricité des dernières 24 heures',
								currentDate: new Date()-1, icon: 'zap']"/>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col d-flex">
				<div class="card flex-fill w-100">
					<div class="card-body">
						<g:render template="/widget/syntheseConsommationMonth"
							model="[compteur: house?.compteur, title: 'Consommation d\'électricité par jour',
								icon: 'zap']"/>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col d-flex">
				<div class="card flex-fill w-100">
					<div class="card-body">
						<g:render template="/widget/syntheseConsommationMonth"
							model="[compteur: house?.compteurGaz, title: 'Consommation de gaz par jour',
								icon: 'fire', iconLib: 'awesome']"/>
					</div>
				</div>
			</div>
		</div>
		
	</g:applyLayout>
</body>
</html>