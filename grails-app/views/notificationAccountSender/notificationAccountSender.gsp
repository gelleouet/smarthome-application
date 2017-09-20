<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ notificationAccountSender.id ? 'Connecteur : ' + notificationAccountSender.libelle : 'Nouveau connecteur' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="notificationAccountSender" method="post" class="aui">
			<g:hiddenField name="id" value="${notificationAccountSender.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:actionSubmit value="Enregistrer" action="save" class="aui-button aui-button-primary" />
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>