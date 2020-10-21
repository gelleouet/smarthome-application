<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Utilisateurs', navigation: 'Système']">
	
	
		<div class="row">
			<div class="col-8">
				<g:form class="form-inline" action="users" name="user-form">
					<fieldset>
						<input autofocus="true" class="form-control" type="text" placeholder="Rechercher nom, prénom, email" name="search" value="${ command.search }"/>
						<button class="btn btn-light"><app:icon name="search"/></button>
					</fieldset>
				</g:form>
			</div>
			<div class="col-4 text-right">
				<g:link action="create" class="btn btn-light"><app:icon name="plus"/> Ajouter</g:link>
			</div>
		</div>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }" paginateForm="user-form">
		    <thead>
		        <tr>
		            <th>Nom</th>
		            <th>Email</th>
		            <th>Activation</th>
		            <th>Connexion</th>
		            <th>Statuts</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="user" in="${ userInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${user.id }">${user.prenomNom }</g:link></td>
			            <td>${user.username }</td>
			            <td><app:formatUser date="${user.lastActivation }"/></td>
			            <td><app:formatTimeAgo date="${user.lastConnexion }"/></td>
			            <td><g:render template="/user/userStatut" model="[user: user]"/></td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>