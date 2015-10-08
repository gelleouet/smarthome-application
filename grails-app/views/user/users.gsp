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
					<input autofocus="true" class="text long-field" type="text" placeholder="Rechercher nom, prénom, email" name="userSearch" value="${ userSearch }"/>
					<button class="aui-button aui-button-subtitle"><span class="aui-icon aui-icon-small aui-iconfont-search"></span></button>
				</fieldset>
			</g:form>
		</h4>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th id="id-nom">Nom / Prénom</th>
		            <th id="id-email">Email</th>
		            <th id="id-activation">Dernière activation</th>
		            <th id="id-statut">Statuts</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="user" in="${ userInstanceList }">
			        <tr>
			            <td headers="id-nom"><g:link action="edit" id="${user.id }">${user.nom } ${user.prenom }</g:link></td>
			            <td headers="id-email">${user.username }</td>
			            <td headers="id-activation"><app:formatUser date="${user.lastActivation }"/></td>
			            <td headers="id-statut"><g:render template="/user/userStatut" model="[user: user]"/></td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>