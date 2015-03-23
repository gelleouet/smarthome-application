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
		                <h3>Permissions</h3>
		            </div>		            
		        </div>
		        <div class="aui-toolbar2-secondary">
		        	<g:form>
		            <div class="aui-buttons">
						<g:actionSubmit class="aui-button" value="Ajouter une permission" action="create"/>
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		
		<h4>
			<g:form class="aui" action="roles">
				<fieldset>
					<input autofocus="true" class="text long-field" type="text" placeholder="Rechercher permission" name="roleSearch" value="${ roleSearch }"/>
				</fieldset>
			</g:form>
		</h4>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th id="id-nom">Nom</th>
		            <th id="id-acl">ACL</th>
		            <th id="id-workflow">Workflow</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="role" in="${ roleInstanceList }">
			        <tr>
			            <td headers="id-nom"><g:link action="edit" id="${role.id }">${ role.authority }</g:link></td>
			            <td headers="id-acl"><g:if test="${ role.acl }">X</g:if></td>
			            <td headers="id-workflow">
			            	<g:if test="${ role.workflowAcl }">
			            		<span class="aui-lozenge aui-lozenge-complete">${role.workflowAcl}</span>
			            	</g:if>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>