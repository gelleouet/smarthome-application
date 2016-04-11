<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ scriptRule.id ? 'Règle métier : ' + scriptRule.ruleName : 'Nouvelle règle métier' }</h3>
		
		<g:form controller="scriptRule" method="post" class="aui">
			<g:hiddenField name="id" value="${scriptRule.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:if test="${scriptRule.id }">
						<g:actionSubmit value="Mettre à jour" action="saveEdit" class="aui-button aui-button-primary" />
					</g:if>
					<g:else>
						<g:actionSubmit value="Créer" action="saveCreate" class="aui-button aui-button-primary" />
					</g:else>
					
					<g:link action="scriptRules" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>