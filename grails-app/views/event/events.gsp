<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Evénements', navigation: 'Smarthome']">
	
		<div class="row">
			<div class="col-8">
				<g:form class="form-inline" action="events">
					<fieldset>
						<input autofocus="true" class="form-control" type="text" placeholder="Rechercher ..." name="eventSearch" value="${ eventSearch }"/>
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
			            <td>${ bean.cron } <g:if test="${ bean.synchroSoleil }"><span class="badge badge-primary">${ bean.lastHeureDecalage ?: '-' }</span></g:if></td>
			            <td>${ app.formatTimeAgo(date: bean.lastEvent) }</td>
			            <td>${ bean.actif ? 'X' : '' }</td>
			            <td>
			            	<g:if test="${ bean.inverseMode }">not (</g:if>
			            	<g:each var="mode" in="${ bean.modes }">
			            		<span class="badge badge-primary">${ mode.mode.name }</span>
			            	</g:each>
			            	<g:if test="${ bean.inverseMode }">)</g:if>
						</td>
			            <td class="column-2-buttons command-column">
			            	<g:link class="btn btn-light confirm-button" title="Suppimer" action="delete" id="${ bean.id }">
			            		<app:icon name="trash"/>
			            	</g:link>
			            	<g:link class="btn btn-light confirm-button" title="Exécuter" action="execute" id="${ bean.id }">
			            		<app:icon name="play"/>
			            	</g:link>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>