<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Configuration', navigation: 'Système']">
		
		<div class="row mb-4">
			<div class="col-8">
				<h4>${ config?.id ? 'Config : ' + config.'name' : 'Nouvelle configuration' }</h4>
			</div>
			<div class="col-4 text-right">
				<div class="btn-toolbar">
					<div class="btn-group">
					</div>
				</div>
			</div>
		</div>
		
		<g:form controller="config" method="post" action="save">
			<g:hiddenField name="id" value="${config?.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<button class="btn btn-primary">Enregistrer</button>
			<g:link action="configs" class="btn btn-link">Annuler</g:link>
		</g:form>
		
	</g:applyLayout>
</body>
</html>