<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ role.id ? role.authority : 'Nouvelle permission' }</h3>
		
		<g:form controller="role" method="post" class="aui">
			<g:hiddenField name="id" value="${role.id}" />
	
			<fieldset>
				<div class="field-group">
					<label for="authority">Permission<span
						class="aui-icon icon-required"> required</span></label>
					<g:textField name="authority" value="${role.authority}" class="text long-field" required="true" />
				</div>
			</fieldset>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:if test="${role.id }">
						<g:actionSubmit value="Mettre à jour" action="saveEdit" class="aui-button aui-button-primary" />
					</g:if>
					<g:else>
						<g:actionSubmit value="Créer" action="saveCreate" class="aui-button aui-button-primary" />
					</g:else>
					
					<g:link action="roles" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>