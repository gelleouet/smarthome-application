<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ agentInstance.id ? 'Agent : ' + agentInstance.'TODO' : 'Nouveau agentInstance' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="agentInstance" method="post" class="aui">
			<g:hiddenField name="id" value="${agentInstance.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:if test="${agentInstance.id }">
						<g:actionSubmit value="Mettre à jour" action="saveEdit" class="aui-button aui-button-primary" />
					</g:if>
					<g:else>
						<g:actionSubmit value="Créer" action="saveCreate" class="aui-button aui-button-primary" />
					</g:else>
					
					<g:link action="agentInstances" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>