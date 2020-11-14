<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-default">
	
		<h3 class="mb-3">Validation des index</h3>
		
		<div class="card">
			
			<div class="card-header">
				<div class="card-actions float-right">
				</div>
			</div> <!-- div.card-header -->
		
			<div class="card-body">
			
				<g:form action="compteurIndexs" name="compteurIndex-form" class="form-inline">
					<div class="row w-100">
						<div class="col">
							<g:select name="deviceTypeId" value="${ command?.deviceTypeId }" from="${ compteurTypes }"
								optionKey="id" optionValue="libelle" class="form-control" noSelection="['': ' ']"/>
							<g:select name="profilId" value="${ command?.profilId }" from="${ profils }"
								optionKey="id" optionValue="libelle" class="form-control" noSelection="['': ' ']"/>
							<g:textField name="userSearch" value="${ command?.userSearch}" class="form-control"/>
							<button class="btn btn-light"><app:icon name="search"/></button>
						</div>
						<div class="col text-right">
						</div>
					</div>	
				</g:form>
		
				<br/>
			
				<app:datatable datatableId="datatable" recordsTotal="${ indexs?.totalCount ?: 0 }" paginateForm="compteurIndex-form">
				    <thead>
				        <tr>
				            <th>Propriétaire</th>
				            <th>Date index</th>
				            <th>Type</th>
				            <th>Index 1</th>
				            <th>Index 2</th>
				            <th>Param 1</th>
				            <th class="column-1-buttons"></th>
				        </tr>
				    </thead>
				    <tbody>
				    	<g:each var="index" in="${ indexs }">
					        <tr>
					            <td><g:link action="compteurIndex" id="${ index.id }">${ index.device.user.profil?.libelle } > ${ index.device.user.prenomNom }</g:link></td>
					            <td><g:formatDate date="${ index.dateIndex }" format="dd/MM/yyyy"/></td>
					            <td>${ index.device.deviceType.libelle }</td>
					            <td>${ index.index1 as Long }</td>
					            <td>${ index.index2 as Long }</td>
					            <td>${ index.param1 }</td>
					            <td class="column-1<-buttons command-column">
					            	<g:link class="btn btn-light confirm-button" title="Supprimer" action="deleteIndex" id="${ index.id }">
					            		<app:icon name="trash"/>
					            	</g:link>
					            </td>
					        </tr>
				        </g:each>
				    </tbody>
				</app:datatable>
			</div><!-- div.card-body -->
			
		</div> <!-- div.card -->
		
	</g:applyLayout>
</body>
</html>