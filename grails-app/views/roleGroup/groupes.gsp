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
		                <h3>Groupes</h3>
		            </div>		            
		        </div>
		        <div class="aui-toolbar2-secondary">
		        	<g:form>
		            <div class="aui-buttons">
						<g:actionSubmit class="aui-button" value="Ajouter un groupe" action="create"/>
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		
		<h4>
			<g:form class="aui" action="groupes">
				<fieldset>
					<input autofocus="true" class="text long-field" type="text" placeholder="Rechercher groupe" name="groupeSearch" value="${ groupeSearch }"/>
				</fieldset>
			</g:form>
		</h4>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th id="id-nom">Nom</th>
		            <th id="id-statut">Permissions</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="groupe" in="${ roleGroupInstanceList }">
			        <tr>
			            <td headers="id-nom"><g:link action="edit" id="${groupe.id }">${ groupe.name}</g:link></td>
			            <td headers="id-statut">
			            	<g:render template="permissions" model="[roles: groupe.getAuthorities()]"/>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>