<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Notifications', navigation: 'Compte']">
		
		<h4 class="mb-4">${ notification?.id ? 'Notification : ' + notification.description : 'Nouvelle notification' }</h4>
		
		<g:form controller="notification" method="post" name="notification-form">
			<g:hiddenField name="id" value="${notification?.id}" />
	
			<g:render template="form"/>
			
			<br/>
			
			<g:actionSubmit value="Enregistrer" action="save" class="btn btn-primary" />
			<g:link action="notifications" class="btn btn-link">Annuler</g:link>
		</g:form>
		
	</g:applyLayout>
</body>
</html>