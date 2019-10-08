<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: '${className}s', navigation: 'navigation']">
		<g:form controller="${propertyName}" method="post" action="save">
		
			<div class="row mb-4">
				<div class="col-8">
					<h4>\${ ${propertyName}.id ? '${className} : ' + ${propertyName}.'TODO' : 'Nouveau ${propertyName}' }</h4>
				</div>
				<div class="col-4 text-right">
					<div class="btn-toolbar">
						<div class="btn-group">
						</div>
					</div>
				</div>
			</div>
		
			<g:hiddenField name="id" value="\${${propertyName}.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<button class="btn btn-primary">Enregistrer</button>
			<g:link action="${propertyName}s" class="btn btn-link">Annuler</g:link>
		</g:form>
		
	</g:applyLayout>
</body>
</html>