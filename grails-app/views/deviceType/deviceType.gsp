<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ deviceType.id ? 'Produit : ' + deviceType.libelle : 'Nouveau produit' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="deviceType" method="post" class="aui">
			<g:hiddenField name="id" value="${deviceType.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:if test="${deviceType.id }">
						<g:actionSubmit value="Enregistrer" action="saveEdit" class="aui-button aui-button-primary" />
					</g:if>
					<g:else>
						<g:actionSubmit value="Créer" action="saveCreate" class="aui-button aui-button-primary" />
					</g:else>
					
					<g:link action="deviceTypes" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>