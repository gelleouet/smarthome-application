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
		                <h3>Evénements</h3>
		            </div>		            
		        </div>
		        <div class="aui-toolbar2-secondary">
		        	<g:form >
		            <div class="aui-buttons">
						<g:actionSubmit class="aui-button" value="Ajouter un événement" action="create"/>
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		
		<h4>
			<g:form class="aui" action="deviceEvents">
				<fieldset>
					<input autofocus="true" class="text long-field" type="text" placeholder="Rechercher ..." name="deviceEventSearch" value="${ deviceEventSearch }"/>
					<button class="aui-button aui-button-subtitle"><span class="aui-icon aui-icon-small aui-iconfont-search"></span></button>
				</fieldset>
			</g:form>
		</h4>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Objet</th>
		            <th>Description</th>
		            <th>Planification</th>
		            <th>Dernière exécution</th>
		            <th>Actif ?</th>
		            <th class="column-2-buttons"></th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ deviceEventInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">${ bean.device.label }</g:link></td>
			            <td>${ bean.libelle }</td>
			            <td>${ bean.cron } <g:if test="${ bean.synchroSoleil }"><span class="aui-lozenge aui-lozenge-complete">${ bean.lastHeureDecalage ?: '-' }</span></g:if></td>
			            <td>${ app.formatTimeAgo(date: bean.lastEvent) }</td>
			            <td>${ bean.actif ? 'X' : '' }</td>
			            <td class="column-2-buttons command-column">
			            	<g:link class="aui-button aui-button-subtle confirm-button" title="Suppimer" action="delete" id="${ bean.id }">
			            		<span class="aui-icon aui-icon-small aui-iconfont-delete">
			            	</g:link>
			            	<g:link class="aui-button aui-button-subtle confirm-button" title="Exécuter" action="execute" id="${ bean.id }">
			            		<span class="aui-icon aui-icon-small aui-iconfont-build"></span>
			            	</g:link>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>