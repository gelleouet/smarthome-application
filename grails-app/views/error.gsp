<html>
<head>
	<meta name="layout" content="anonymous">
</head>
<body>
	<g:set var="error">
		Erreur générale. Impossible d'exécuter la requête demandée.
	</g:set>
	
	<g:applyLayout name="applicationContent">
		<g:link uri="/">Revenir à la page d'accueil</g:link>
		
		<g:if env="development">
			<g:renderException exception="${exception}" />
		</g:if>
	</g:applyLayout>
</body>
</html>