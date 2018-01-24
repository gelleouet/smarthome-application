<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body onLoad="onLoadWorkflows()">
	<g:applyLayout name="applicationConfigure">
	
		<div class="aui-toolbar2">
		    <div class="aui-toolbar2-inner">
		        <div class="aui-toolbar2-primary">
		            <div>
		                <h3>Workflows</h3>
		            </div>		            
		        </div>
		        <div class="aui-toolbar2-secondary">
		        	<g:form >
		            <div class="aui-buttons">
						<g:actionSubmit class="aui-button" value="Ajouter un workflow" action="create"/>
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		
		<h4>
			<g:form class="aui" action="workflows">
				<fieldset>
					<input autofocus="true" class="text long-field" type="text" placeholder="Rechercher ..." name="workflowSearch" value="${ workflowSearch }"/>
					<button class="aui-button aui-button-subtitle"><span class="aui-icon aui-icon-small aui-iconfont-search"></span></button>
				</fieldset>
			</g:form>
		</h4>
		
		<br/>
		
		
		<div style="overflow-x:auto;">
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>ID</th>
		            <th>Libellé</th>
		            <th>Description</th>
		            <th class="column-2-buttons"></th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ workflowInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">${ bean.id }</g:link></td>
			            <td>${ bean.libelle }</td>
			            <td>${ bean.description }</td>
			            <td class="column-2-buttons command-column">
			            	<g:link class="aui-button aui-button-subtle confirm-button" title="Supprimer" action="delete" id="${ bean.id }">
			            		<span class="aui-icon aui-icon-small aui-iconfont-delete"></span>
			            	</g:link>
			            	<a id="diagram-button" class="aui-button aui-button-subtle" title="Diagramme BPMN" data-url="${ g.createLink(action: 'dialogDiagram', id: bean.id) }">
			            		<span class="aui-icon aui-icon-small aui-iconfont-workflow"></span>
			            	</a>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		</div>
		
	</g:applyLayout>
	
</body>
</html>