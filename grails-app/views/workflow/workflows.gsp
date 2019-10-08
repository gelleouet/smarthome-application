<html>
<head>
<meta name='layout' content='main' />
</head>

<body onLoad="onLoadWorkflows()">
	<g:applyLayout name="page-settings" model="[titre: 'Workflows', navigation: 'Système']">
	
		<div class="row">
			<div class="col-8">
				<g:form class="form-inline" action="workflows">
					<fieldset>
						<input autofocus="true" class="form-control" type="text" placeholder="Rechercher ..." name="workflowSearch" value="${ workflowSearch }"/>
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
		            <th class="column-2-buttons"></th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ workflowInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">${ bean.libelle }</g:link></td>
			            <td>${ bean.description }</td>
			            <td class="column-2-buttons command-column">
			            	<g:link class="btn btn-light confirm-button" title="Supprimer" action="delete" id="${ bean.id }">
			            		<app:icon name="trash"/>
			            	</g:link>
			            	<a id="diagram-button" class="btn btn-light" title="Diagramme BPMN" data-url="${ g.createLink(action: 'dialogDiagram', id: bean.id) }">
			            		<app:icon name="share-2"/>
			            	</a>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>