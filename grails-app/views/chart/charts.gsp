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
		                <h3>Graphiques</h3>
		            </div>		            
		        </div>
		        <div class="aui-toolbar2-secondary">
		        	<g:form >
		            <div class="aui-buttons">
						<g:actionSubmit class="aui-button" value="Ajouter un graphique" action="create"/>
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		
		<h4>
			<g:form class="aui" action="charts">
				<fieldset>
					<input autofocus="true" class="text long-field" type="text" placeholder="Rechercher ..." name="search" value="${ command.search }"/>
				</fieldset>
			</g:form>
		</h4>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Libellé</th>
		            <th>Graphique</th>
		            <th>Groupe</th>
		            <th class="column-1-buttons"></th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ chartInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">
			            	<asset:image src="/chart/${ bean.chartType }.png" class="device-icon-list"/>
			            	${ bean.label }
			            </g:link></td>
			            <td>${ bean.chartType }</td>
			            <td>${ bean.groupe }</td>
			            <td class="column-1-buttons command-column">
			            	<g:link class="aui-button aui-button-subtle confirm-button" title="Supprimer" action="delete" id="${ bean.id }">
			            		<span class="aui-icon aui-icon-small aui-iconfont-delete"></span>
			            	</g:link>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>