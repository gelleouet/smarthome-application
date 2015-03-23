<html>
<head>
	<meta name="layout" content="anonymous">
</head>
<body>
	<g:set var="error">
		Une erreur grave est survenue ou la page n'existe pas !
	</g:set>

	<g:applyLayout name="applicationContent">
		<g:link uri="/">Revenir à la page d'accueil</g:link>
		
		<g:if env="development">
			<g:renderException exception="${exception}" />
		</g:if>
	</g:applyLayout>
</body>
</html>