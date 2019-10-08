<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Scénarios', navigation: 'Smarthome']">
	
		<div class="row">
			<div class="col-8">
				<g:form class="form-inline" action="scenarios">
					<fieldset>
						<input autofocus="true" class="form-control" type="text" placeholder="Rechercher ..." name="scenarioSearch" value="${ scenarioSearch }"/>
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
		            <th>Nom</th>
		            <th>Description</th>
		            <th>Dernière exécution</th>
		            <th class="column-1-buttons"></th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ scenarioInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">${ bean.label }</g:link></td>
			            <td>${ bean.description }</td>
			            <td>${ app.formatTimeAgo(date: bean.lastExecution) }</td>
			            <td class="column-1-buttons command-column">
			            	<g:link class="btn btn-light confirm-button" title="Supprimer" action="delete" id="${ bean.id }">
			            		<app:icon name="trash"/>
			            	</g:link>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>