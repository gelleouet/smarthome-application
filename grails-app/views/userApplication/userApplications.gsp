<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Applications', navigation: 'Compte']">
	
		<g:form class="form-inline" action="userApplications">
			<fieldset>
				<input autofocus="true" class="form-control" type="text" placeholder="Application ..." name="search" value="${ command.search }"/>
				<button class="btn btn-light"><app:icon name="search"/></button>
			</fieldset>
		</g:form>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Applications</th>
		            <th class="column-1-buttons"></th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ userApplicationInstanceList }">
			        <tr>
			            <td>
			            	<h4>${ bean.name } <span class="h6-normal">${ app.formatTimeAgo(date: bean.dateAuth) }</span></h4>
			            	
			            	<div style="margin-top: 10px;">
			            		<g:render template="${ bean.view() }" model="[userApplication: bean]"/>
			            	</div>
			            </td>
			            <td class="column-1-buttons command-column">
			            	<g:link class="btn btn-light confirm-button" title="Suppimer" action="delete" id="${ bean.id }">
			            		<app:icon name="trash"/>
			            	</g:link>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>