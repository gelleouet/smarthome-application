<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'jobs', navigation: 'Système']">
	
		<div class="row">
			<div class="col-8">
			</div>
			<div class="col-4 text-right">
			</div>
		</div>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Instance</th>
		            <th>Cron</th>
		            <th class="column-1-buttons"></th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ jobs }">
			        <tr>
			            <td><${ bean.key }</td>
			            <td>${ bean.value }</td>
			            <td class="column-1-buttons command-column">
			            	<g:link class="btn btn-light confirm-button" title="Exécuter" action="execute" params="[jobInstance: '${ bean.key }']">
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