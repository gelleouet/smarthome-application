<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ notificationAccount.id ? 'Service : ' + notificationAccount.notificationAccountSender.libelle : 'Nouveau service' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="notificationAccount" method="post" class="aui" name="notificationAccount-form">
			<g:hiddenField name="id" value="${notificationAccount.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:actionSubmit value="Enregister" action="save" class="aui-button aui-button-primary" />
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>