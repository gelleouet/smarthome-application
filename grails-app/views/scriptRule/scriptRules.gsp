<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Règles métier', navigation: 'Système']">
	
		<div class="row">
			<div class="col-8">
				<g:form class="form-inline" action="scriptRules">
					<fieldset>
						<input autofocus="true" class="form-control" type="text" placeholder="Rechercher ..." name="scriptRuleSearch" value="${ scriptRuleSearch }"/>
						<button class="btn btn-light"><app:icon name="search"/></button>
					</fieldset>
				</g:form>
			</div>
			<div class="col-4 text-right">
				<g:link action="create" class="btn btn-light"><app:icon name="plus"/> Ajouter</g:link>
			</div>
		</div>
	
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Classe</th>
		            <th>Description</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ scriptRuleInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">${ bean.ruleName }</g:link></td>
			            <td>${ bean.description }</td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>