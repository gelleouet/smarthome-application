<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ workflowInstance.id ? 'Workflow : ' + workflowInstance.'TODO' : 'Nouveau workflowInstance' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="workflowInstance" method="post" class="aui">
			<g:hiddenField name="id" value="${workflowInstance.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:if test="${workflowInstance.id }">
						<g:actionSubmit value="Mettre à jour" action="saveEdit" class="aui-button aui-button-primary" />
					</g:if>
					<g:else>
						<g:actionSubmit value="Créer" action="saveCreate" class="aui-button aui-button-primary" />
					</g:else>
					
					<g:link action="workflowInstances" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>