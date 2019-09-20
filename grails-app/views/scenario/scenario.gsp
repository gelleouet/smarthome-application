<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Scénarios', navigation: 'Smarthome']">
	
		<div class="row mb-4">
			<div class="col-8">
				<h4>${ scenario.id ? 'Scénario : ' + scenario.label : 'Nouveau scénario' }</h4>
			</div>
			<div class="col-4 text-right">
				<div class="btn-toolbar">
					<div class="btn-group">
					</div>
				</div>
			</div>
		</div>
		
		
		<g:form controller="scenario" method="post" class="aui">
			<g:hiddenField name="id" value="${scenario.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<g:if test="${scenario.id }">
				<g:actionSubmit value="Enregistrer" action="saveEdit" class="btn btn-primary" />
			</g:if>
			<g:else>
				<g:actionSubmit value="Enregistrer" action="saveCreate" class="btn btn-primary" />
			</g:else>
			
			<g:link action="scenarios" class="btn btn-link">Annuler</g:link>
		</g:form>
		
	</g:applyLayout>
</body>
</html>