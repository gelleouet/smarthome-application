<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
	
		<div class="aui-toolbar2">
		    <div class="aui-toolbar2-inner">
		        <div class="aui-toolbar2-primary">
		            <div>
		                <h3>Utilisateurs</h3>
		            </div>		            
		        </div>
		        <div class="aui-toolbar2-secondary">
		        	<g:form controller="user" >
		            <div class="aui-buttons">
						<g:actionSubmit class="aui-button" value="Ajouter un utilisateur" action="create"/>
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		
		<h4>
			<g:form class="aui" action="users">
				<fieldset>
					<input autofocus="true" class="text long-field" type="text" placeholder="Rechercher nom, prénom, email" name="search" value="${ command.search }"/>
					<button class="aui-button aui-button-subtitle"><span class="aui-icon aui-icon-small aui-iconfont-search"></span></button>
				</fieldset>
			</g:form>
		</h4>
		
		<br/>
		
		<div style="overflow-x:auto;">
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Nom</th>
		            <th>Email</th>
		            <th>Activation</th>
		            <th>Connexion</th>
		            <th>Statuts</th>
		            <th class="column-1-buttons"></th>
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
			            <td class="column-1-buttons command-column">
			            	<g:link class="aui-button aui-button-subtle confirm-button" title="Basculer vers l'utilisateur" action="switchUser" id="${ user.id }">
			            		<span class="aui-icon aui-icon-small aui-iconfont-group"></span>
			            	</g:link>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		</div>
		
	</g:applyLayout>
	
</body>
</html>