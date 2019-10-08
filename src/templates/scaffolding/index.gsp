<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: '${className}s', navigation: 'navigation']">
	
		<div class="row">
			<div class="col-8">
				<g:form class="form-inline" action="${propertyName}s">
					<fieldset>
						<input autofocus="true" class="form-control" type="text" placeholder="Rechercher ..." name="search" value="\${ command.search }"/>
						<button class="btn btn-light"><app:icon name="search"/></button>
					</fieldset>
				</g:form>
			</div>
			<div class="col-4 text-right">
				<g:link action="edit" class="btn btn-light"><app:icon name="plus"/> Ajouter</g:link>
			</div>
		</div>
	
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="\${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>ID</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="\${ ${propertyName}InstanceList }">
			        <tr>
			            <td><g:link action="edit" id="\${bean.id }">\${ bean.id }</g:link></td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>