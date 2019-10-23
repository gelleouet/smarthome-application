<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Connecteurs', navigation: 'Système']">
	
		<div class="row mb-4">
			<div class="col-8">
				<h4>${ notificationAccountSender?.id ? 'Connecteur : ' + notificationAccountSender.libelle : 'Nouveau connecteur' }</h4>
			</div>
			<div class="col-4 text-right">
				<div class="btn-toolbar">
					<div class="btn-group">
					</div>
				</div>
			</div>
		</div>
	
		<g:form controller="notificationAccountSender" method="post">
			<g:hiddenField name="id" value="${notificationAccountSender?.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<g:actionSubmit value="Enregistrer" action="save" class="btn btn-primary" />
			<g:link action="notificationAccountSenders" class="btn btn-link">Annuler</g:link>
		</g:form>
		
	</g:applyLayout>
</body>
</html>