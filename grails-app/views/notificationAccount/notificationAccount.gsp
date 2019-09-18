<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Services', navigation: 'Compte']">
		
		<h4 class="mb-4">${ notificationAccount.id ? 'Service : ' + notificationAccount.notificationAccountSender.libelle : 'Nouveau service' }</h4>
		
		<g:form controller="notificationAccount" method="post" class="aui" name="notificationAccount-form">
			<g:hiddenField name="id" value="${notificationAccount.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<g:actionSubmit value="Enregister" action="save" class="btn btn-primary" />
			<g:link action="notificationAccounts" class="btn btn-link">Annuler</g:link>
		</g:form>
		
	</g:applyLayout>
</body>
</html>