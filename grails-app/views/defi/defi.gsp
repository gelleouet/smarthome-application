<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Défis', navigation: 'Compte']">
		<g:form controller="defi" method="post" action="save">
		
			<div class="row mb-4">
				<div class="col-8">
					<h4>${ defi?.id ? 'Défi : ' + defi.libelle : 'Nouveau défi' }</h4>
				</div>
				<div class="col-4 text-right">
					<div class="btn-toolbar">
						<div class="btn-group">
						</div>
					</div>
				</div>
			</div>
		
			<g:hiddenField name="id" value="${defi?.id}" />
	
			<g:render template="form"/>
			
			<br/>
			<br/>
	
			<button class="btn btn-primary">Enregistrer</button>
			<g:link action="defis" class="btn btn-link">Annuler</g:link>
		</g:form>
		
	</g:applyLayout>
</body>
</html>