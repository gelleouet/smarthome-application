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
						<g:actionSubmit class="aui-button" value="Ajouter un événement" action="edit"/>
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		
		<h4>
			<g:form class="aui" action="events">
				<fieldset>
					<input autofocus="true" class="text long-field" type="text" placeholder="Rechercher ..." name="eventSearch" value="${ eventSearch }"/>
					<button class="aui-button aui-button-subtitle"><span class="aui-icon aui-icon-small aui-iconfont-search"></span></button>
				</fieldset>
			</g:form>
		</h4>
		
		<br/>
		
		<div style="overflow-x:auto;">
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Description</th>
		            <th>Planification</th>
		            <th>Exécution</th>
		            <th>Actif ?</th>
		            <th>Modes</th>
		            <th class="column-2-buttons"></th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ eventInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">${ bean.libelle }</g:link></td>
			            <td>${ bean.cron } <g:if test="${ bean.synchroSoleil }"><span class="aui-lozenge aui-lozenge-complete">${ bean.lastHeureDecalage ?: '-' }</span></g:if></td>
			            <td>${ app.formatTimeAgo(date: bean.lastEvent) }</td>
			            <td>${ bean.actif ? 'X' : '' }</td>
			            <td>
			            	<g:if test="${ bean.inverseMode }">not (</g:if>
			            	<g:each var="mode" in="${ bean.modes }">
			            		<span class="aui-lozenge aui-lozenge-complete">${ mode.mode.name }</span>
			            	</g:each>
			            	<g:if test="${ bean.inverseMode }">)</g:if>
						</td>
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
		</div>
		
	</g:applyLayout>
	
</body>
</html>