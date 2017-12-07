<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ deviceTypeProvider.id ? 'Fournisseur : ' + deviceTypeProvider.libelle : 'Nouveau fournisseur' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="deviceTypeProvider" method="post" class="aui" action="save">
			<g:hiddenField name="id" value="${deviceTypeProvider.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<button class="aui-button aui-button-primary">Enregistrer</button>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>