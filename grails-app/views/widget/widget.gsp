<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
		<h3>${ widget.id ? 'Widget : ' + widget.libelle : 'Nouveau widget' } <span id="ajaxSpinner" class="spinner"/></h3>
		
		<g:form controller="widget" action="saveEdit" method="post" class="aui">
			<g:hiddenField name="id" value="${widget.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<div class="buttons-container">
				<div class="buttons">
					<g:if test="${widget.id }">
						<button class="aui-button aui-button-primary">Mettre à jour</button>
					</g:if>
					<g:else>
						<button class="aui-button aui-button-primary">Créer</button>
					</g:else>
					
					<g:link action="widgets" class="cancel">Annuler</g:link>
				</div>
			</div>
		</g:form>
		
	</g:applyLayout>
</body>
</html>