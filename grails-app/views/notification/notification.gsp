<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ notification.id ? 'Notification : ' + notification.description : 'Nouvelle notification' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="notification" method="post" class="aui" name="notification-form">
			<g:hiddenField name="id" value="${notification.id}" />
	
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