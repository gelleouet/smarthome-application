<html>
<head>
	<meta name="layout" content="main">
</head>
<body>
	<g:if test="${ !error }">
		<g:set var="error">
			Erreur générale. Impossible d'exécuter la requête demandée.
		</g:set>
	</g:if>
	
	<g:applyLayout name="page-focus">
	
		<div class="text-center mt-4">
			<h1 class="h2">Accès refusé. Autorisations insuffisantes</h1>
		</div>
		
		<div class="card">
			<div class="card-body">
				<g:applyLayout name="content-error"/>
				
				<div class="text-center">
					<g:link uri="/" class="btn btn-link">Revenir à la page d'accueil</g:link>
				</div>
			</div>
		</div>
		
		<g:if env="development">
			<g:renderException exception="${exception}" />
		</g:if>
	</g:applyLayout>
</body>
</html>