<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Catalogue', navigation: 'Système']">
	
		<div class="row mb-4">
			<div class="col-8">
				<h4>${ deviceType?.id ? 'Produit : ' + deviceType.libelle : 'Nouveau produit' }</h4>
			</div>
			<div class="col-4 text-right">
				<div class="btn-toolbar">
					<div class="btn-group">
					</div>
				</div>
			</div>
		</div>
	
		
		<g:form controller="deviceType" method="post" action="save">
			<g:hiddenField name="id" value="${deviceType?.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<button class="btn btn-primary">Enregistrer</button>
			<g:link action="deviceTypes" class="btn btn-link">Annuler</g:link>
		</g:form>
		
	</g:applyLayout>
</body>
</html>