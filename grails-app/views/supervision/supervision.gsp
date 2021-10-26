<html>
<head>
<meta name='layout' content='main' />
</head>

<body onload="onLoadSupervision()">

	<g:applyLayout name="page-default">
	
		<h3>Supervision utilisateurs</h3>
	
	
		<div class="card card-margin-top">
			<div class="card-body">
				<g:form action="supervision" name="supervision-form" class="form-inline">
					
					<div class="row w-100">
						<div class="col-10">
							<g:select name="defiId" value="${ command.defiId }" from="${ defis }" class="form-control combobox"
	            				optionKey="id" optionValue="libelle"  noSelection="['': ' ']"/>
	            				
							<g:select name="ville" value="${ command.ville }" from="${ communes }" class="form-control combobox"
	            				optionKey="libelle" optionValue="libelle"  noSelection="['': ' ']"/>
	            			
	            			<g:select name="profilId" value="${ command?.profilId }" from="${ profils }" class="form-control combobox"
								optionKey="id" optionValue="libelle" noSelection="['': ' ']"/>
	            				
							<g:select name="deviceTypeId" value="${ command?.deviceTypeId }" from="${ compteurTypes }" class="form-control combobox"
								optionKey="id" optionValue="libelle" noSelection="['': ' ']"/>
							
							<g:textField name="userSearch" value="${ command?.userSearch}" class="form-control" placeholder="Nom prénom"/>
							
							<button class="btn btn-light"><app:icon name="search"/></button>
						</div>
						<div class="col text-right">
							<a id="supervision-export-button" class="btn btn-light" data-url="${ g.createLink(action: 'dialogExport') }"><app:icon name="download-cloud"/> Exporter</a>
						</div>
					</div>	
				</g:form>
				
				
				<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }" paginateForm="supervision-form">
				    <thead>
				        <tr>
				        	<th>Utilisateur</th>
				            <th>Objet</th>
				            <th>Type</th>
				            <th>Date</th>
				            <th class="column-2-buttons"></th>
				        </tr>
				    </thead>
				    <tbody>
				    	<g:each var="device" in="${ devices }">
					        <tr>
					        	<td>
					        		<g:link controller="user" action="edit" id="${ device.user.id }">${ device.user.profil?.libelle } > ${ device.user.nomPrenom }</g:link>
					        	</td>
					            <td>
					            	<g:link controller="device" action="edit" id="${ device.id }">${ device.label }</g:link>
					            </td>
					            <td>${ device.deviceType.libelle }</td>
					            <td><app:formatTimeAgo date="${ device.dateValue }"/></td>
					            <td class="column-2-buttons command-column">
					            	<g:link class="btn btn-light" title="Graphique" controller="device" action="deviceChart" params="['device.id': device.id]">
					            		<app:icon name="bar-chart"/>
					            	</g:link>	
					            	<g:link class="btn btn-light" title="Saisie index" action="saisieIndex" controller="compteur" params="['device.id': device.id]">
					            		<app:icon name="edit"/>
					            	</g:link>	
					            </td>
					        </tr>
				        </g:each>
				    </tbody>
				</app:datatable>
			</div>
		</div>
	</g:applyLayout>
	
</body>
</html>