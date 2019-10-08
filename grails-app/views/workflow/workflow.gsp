<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Workflows', navigation: 'Système']">
		<g:uploadForm controller="workflow" method="post" class="aui" name="workflow-form">
		
			<div class="row mb-4">
				<div class="col-8">
					<h4>${ workflow.id ? 'Workflow : ' + workflow.libelle : 'Nouveau workflow' }</h4>
				</div>
				<div class="col-4 text-right">
					<div class="btn-toolbar">
						<div class="btn-group">
						</div>
					</div>
				</div>
			</div>
		
			<g:hiddenField name="id" value="${workflow.id}" />
	
			<g:render template="form"/>
			
	
			<g:if test="${workflow.id }">
				<g:actionSubmit value="Enregistrer" action="saveEdit" class="btn btn-primary" />
			</g:if>
			<g:else>
				<g:actionSubmit value="Enregistrer" action="saveCreate" class="btn btn-primary" />
			</g:else>
			
			<g:link action="workflows" class="btn btn-link">Annuler</g:link>
		</g:uploadForm>
		
	</g:applyLayout>
</body>
</html>