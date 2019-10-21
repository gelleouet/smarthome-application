<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Services', navigation: 'Compte']">
	
		<div class="row">
			<div class="col-8">
				<g:form class="form-inline" action="notificationAccounts">
					<fieldset>
						<input autofocus="true" class="form-control" type="text" placeholder="Rechercher ..." name="libelle" value="${ command?.libelle }"/>
						<button class="btn btn-light"><app:icon name="search"/></button>
					</fieldset>
				</g:form>
			</div>
			<div class="col-4 text-right">
				<g:link action="create" class="btn btn-light"><app:icon name="plus"/> Ajouter</g:link>
			</div>
		</div>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Libellé</th>
		            <th class="column-2-buttons"></th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ notificationAccountInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">${ bean.notificationAccountSender.libelle }</g:link></td>
			            <td class="command-column">
			            	<g:link class="btn btn-light confirm-button" title="Suppimer" action="delete" id="${ bean.id }">
			            		<app:icon name="trash"/>
			            	</g:link>
			            	<g:link class="btn btn-light confirm-button" title="Exécuter" action="execute" id="${ bean.id }">
			            		<app:icon name="play"/>
			            	</g:link>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>