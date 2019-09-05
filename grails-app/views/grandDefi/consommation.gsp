<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<h3>Ma consommation</h3>
		
		<div class="row">
			<div class="col d-flex">
				<div class="card flex-fill w-100 card-margin-top">
					<div class="card-body">
						<g:render template="/house/syntheseConsommationDay" model="[house: house, title: 'Consommation d\'électricité du jour']"/>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col d-flex">
				<div class="card flex-fill w-100">
					<div class="card-body">
						<g:render template="/house/syntheseConsommationMonth" model="[house: house, title: 'Consommation d\'électricité du mois']"/>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col d-flex">
				<div class="card flex-fill w-100">
					<div class="card-body">
						<g:render template="/house/syntheseConsommationYear"/>
					</div>
				</div>
			</div>
		</div>
		
	</g:applyLayout>
</body>
</html>