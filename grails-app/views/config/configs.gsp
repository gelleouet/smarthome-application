<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Configuration', navigation: 'Système']">
	
		<div class="row">
			<div class="col-8">
				<g:form class="form-inline" action="configs">
					<fieldset>
						<input autofocus="true" class="form-control" type="text" placeholder="Rechercher ..." name="search" value="${ command.search }"/>
						<button class="btn btn-light"><app:icon name="search"/></button>
					</fieldset>
				</g:form>
			</div>
			<div class="col-4 text-right">
				<g:link action="edit" class="btn btn-light"><app:icon name="plus"/> Ajouter</g:link>
			</div>
		</div>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Nom</th>
		            <th>Valeur</th>
		            <th class="column-1-buttons"></th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ configInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">${ bean.name }</g:link></td>
			            <td>${ bean.value }</td>
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