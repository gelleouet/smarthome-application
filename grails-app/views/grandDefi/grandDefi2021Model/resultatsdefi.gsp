<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<g:render template="/grandDefi/defiMenu"/>
		
		<div class="card flex-fill w-100">
			<div class="card-body">
				<h4><app:icon name="bar-chart-2"/> Consommations totales d'énergie </h4>
				<g:render template="/grandDefi/grandDefi2021Model/resultatConsoDefi" model="[data: energie, defi: currentDefi]"/>	
			</div>
		</div>
		
		<div class="card flex-fill w-100">
			<div class="card-body">
				<h4><app:icon name="bar-chart-2"/> Consommations totales d'eau </h4>
				<g:render template="/grandDefi/grandDefi2021Model/resultatConsoDefi" model="[data: eau, defi: currentDefi]"/>	
			</div>
		</div>
		
		<div class="card flex-fill w-100">
			<div class="card-body">
				<h4><app:icon name="award"/> Classement général</h4>
				<g:render template="/grandDefi/grandDefi2021Model/resultatClassementDefi" model="[data: global, defi: currentDefi, type: 'equipe']"/>	
			</div>
		</div>
		
		<g:each var="profil" in="${ profils }">
			<div class="card flex-fill w-100">
				<div class="card-body">
					<h4><g:if test="${ profil.icon }"><asset:image src="${profil.icon }" class="gd-icon-profil"/></g:if> Classement ${ profil.libelle }</h4>
					<g:render template="/grandDefi/grandDefi2021Model/resultatClassementDefi" model="[data: request["profil${ profil.id }"], defi: currentDefi, type: profil.libelle]"/>	
				</div>
			</div>
		</g:each>
		
	</g:applyLayout>
</body>
</html>