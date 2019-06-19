<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ producteurEnergie.id ? 'Production solaire : ' + producteurEnergie.libelle : 'Nouvelle production' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="producteurEnergie" method="post" class="aui">
			<g:hiddenField name="id" value="${producteurEnergie.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:if test="${producteurEnergie.id }">
						<g:actionSubmit value="Enregistrer" action="save" class="aui-button aui-button-primary" />
					</g:if>
					<g:else>
						<g:actionSubmit value="Créer" action="save" class="aui-button aui-button-primary" />
					</g:else>
					<g:link action="producteurEnergies" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>