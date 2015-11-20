<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ workflow.id ? 'Scénario : ' + workflow.label : 'Nouveau scénario' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="workflow" method="post" class="aui">
			<g:hiddenField name="id" value="${workflow.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:if test="${workflow.id }">
						<g:actionSubmit value="Mettre à jour" action="saveEdit" class="aui-button aui-button-primary" />
					</g:if>
					<g:else>
						<g:actionSubmit value="Créer" action="saveCreate" class="aui-button aui-button-primary" />
					</g:else>
					
					<g:link action="workflows" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>