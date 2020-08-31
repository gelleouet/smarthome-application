<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Catalogue', navigation: 'Système']">
	
		<div class="row">
			<div class="col-8">
				<g:form class="form-inline" action="deviceTypes">
					<fieldset>
						<input autofocus="true" class="form-control" type="text" placeholder="Rechercher ..." name="deviceTypeSearch" value="${ deviceTypeSearch }"/>
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
		            <th>Libellé</th>
		            <th>Implémentation</th>
		            <th>Qualitatif?</th>
		            <th>Planning?</th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ deviceTypeInstanceList }">
			        <tr>
			            <td><g:link action="edit" id="${bean.id }">
		            		<asset:image src="${ bean.newDeviceType().icon() }" class="device-icon-list"/>
			            	${ bean.libelle }
			            </g:link></td>
			            <td>${ bean.implClass }</td>
			            <td>${ bean.qualitatif ? 'X' : '' }</td>
			            <td>${ bean.planning ? 'X' : '' }</td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>