<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ groupe.id ? 'Groupe : ' + groupe.name : 'Nouveau groupe' }</h3>
		
		<g:form controller="roleGroup" method="post" class="aui">
			<g:hiddenField name="id" value="${groupe.id}" />
	
			<fieldset>
				<div class="field-group">
					<label for=name>Groupe<span
						class="aui-icon icon-required"> required</span></label>
					<g:textField name="name" value="${groupe.name}" class="text long-field" required="true" />
				</div>
				<div class="field-group">
					<label for="roles">Permissions</label>
					<app:picklist options="${ allRole }" idField="id" labelField="authority" selectId="roles" selectedOptions="${ groupRole }"/>
				</div>
			</fieldset>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:if test="${groupe.id }">
						<g:actionSubmit value="Mettre à jour" action="saveEdit" class="aui-button aui-button-primary" />
					</g:if>
					<g:else>
						<g:actionSubmit value="Créer" action="saveCreate" class="aui-button aui-button-primary" />
					</g:else>
					
					<g:link action="groupes" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>