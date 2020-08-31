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
						<g:set var="lastDate" value="${ house?.compteur?.dateValue?.clone() ?: new Date() }"/>
						<g:set var="titre" value="Consommation d'électricité du ${ g.formatDate(date: lastDate, format: 'dd/MM/yyyy') }"/>
						
						<g:render template="/widget/syntheseConsommationDay"
							model="[compteur: house?.compteur, title: titre,
								currentDate: lastDate, icon: 'zap', typeCompteur: 'electricite']"/>
					</div>
				</div>
			</div>
		</div>
		
		<div class="row">
			<div class="col d-flex">
				<div class="card flex-fill w-100">
					<div class="card-body">
						<g:render template="/widget/syntheseConsommationMonth"
							model="[compteur: house?.compteur, title: 'Consommation d\'électricité du mois',
								icon: 'zap', typeCompteur: 'electricite']"/>
					</div>
				</div>
			</div>
		</div>
		
	</g:applyLayout>
</body>
</html>