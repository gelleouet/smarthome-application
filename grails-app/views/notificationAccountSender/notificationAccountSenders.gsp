<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Connecteurs', navigation: 'Système']">
	
		<div class="row">
			<div class="col-8">
				<g:form class="form-inline" action="notificationAccountSenders">
					<fieldset>
						<input autofocus="true" class="form-control" type="text" placeholder="Rechercher ..." name="notificationAccountSenderSearch" value="${ notificationAccountSenderSearch }"/>
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
		            <th>Libellé</th>
		            <th>Implémentation</th>
		            <th>Rôle</th>
		            <th>Cron</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ notificationAccountSenderInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">${ bean.libelle }</g:link></td>
			            <td>${ bean.implClass }</td>
			            <td>${ bean.role }</td>
			            <td>${ bean.cron }</td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>