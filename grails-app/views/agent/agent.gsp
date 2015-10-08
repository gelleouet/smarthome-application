<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ 'Agent : ' + agent.agentModel } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="agent" method="post" class="aui">
			<g:hiddenField name="id" value="${agent.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:actionSubmit value="Enregistrer" action="save" class="aui-button aui-button-primary" />
					<g:link action="agents" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>