<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Notifications', navigation: 'Compte']">
	
		<div class="row">
			<div class="col-8">
				<g:form class="form-inline" action="notifications">
					<fieldset>
						<input autofocus="true" class="form-control" type="text" placeholder="Rechercher ..." name="description" value="${ command?.description }"/>
						<button class="btn btn-light"><app:icon name="search"/></button>
					</fieldset>
				</g:form>
			</div>
			<div class="col-4 text-right">
				<g:link action="edit" class="btn btn-light"><app:icon name="plus"/> Ajouter</g:link>
			</div>
		</div>
		
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>ID</th>
		            <th>Service</th>
		            <th class="column-2-buttons"></th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ notificationInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">${ bean.description }</g:link></td>
			            <td>${ bean.notificationAccount.notificationAccountSender.libelle }</td>
			            <td class="column-2-buttons command-column">
			            	<g:link class="btn btn-light confirm-button" title="Suppimer" action="delete" id="${ bean.id }">
			            		<app:icon name="trash"/>
			            	</g:link>
			            	
			            	<g:link class="btn btn-light confirm-button" title="Tester" action="executeTest" id="${ bean.id }">
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