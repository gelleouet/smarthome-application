<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Règles métier', navigation: 'Système']">
		
		<div class="row mb-4">
			<div class="col-8">
				<h4>${ scriptRule.id ? 'Règle métier : ' + scriptRule.ruleName : 'Nouvelle règle métier' }</h4>
			</div>
			<div class="col-4 text-right">
				<div class="btn-toolbar">
					<div class="btn-group">
					</div>
				</div>
			</div>
		</div>
		
		
		<g:form controller="scriptRule" method="post">
			<g:hiddenField name="id" value="${scriptRule.id}" />
	
			<g:render template="form"/>
			
			<br/>
	
			<g:if test="${scriptRule.id }">
				<g:actionSubmit value="Enregistrer" action="saveEdit" class="btn btn-primary" />
			</g:if>
			<g:else>
				<g:actionSubmit value="Enregistrer" action="saveCreate" class="btn btn-primary" />
			</g:else>
			
			<g:link action="scriptRules" class="btn btn-link">Annuler</g:link>
		</g:form>
		
	</g:applyLayout>
</body>
</html>