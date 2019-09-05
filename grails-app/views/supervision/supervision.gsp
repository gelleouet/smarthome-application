<html>
<head>
<meta name='layout' content='main' />
</head>

<body>

	<g:applyLayout name="page-default">
	
		<h3>Supervision multi-utilisateurs</h3>
	
	
		<div class="card card-margin-top">
			<div class="card-body">
				<g:form action="supervision" name="supervision-form" class="form-inline">
					
					<div class="row w-100">
						<div class="col">
							<g:select name="deviceTypeClass" value="${ command?.deviceTypeClass }" from="${ deviceImpls }"
								optionKey="implClass" optionValue="libelle" class="form-control" noSelection="['': '']"/>
							<g:select name="userId" value="${ command?.userId }" from="${ users }"
								optionKey="id" optionValue="prenomNom" class="form-control" noSelection="['': '']"/>
							<button class="btn btn-light"><app:icon name="search"/></button>
							
							<button id="export-admin-button" value="Exporter" name="_action_exportAdmin" class="d-none"></button>
						</div>
						<div class="col text-right">
							<g:field type="date" name="dateDebut" class="form-control" value="${ app.formatPicker(date: command?.dateDebut) }"/>
							<g:field type="date" name="dateFin" class="form-control" value="${ app.formatPicker(date: command?.dateFin) }"/>
							<a class="btn btn-light" onclick="$('#export-admin-button').click(); return false;" title="Exporter"><app:icon name="download"/> Exporter</a>
						</div>
					</div>	
				</g:form>
				
				
				<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }" paginateForm="supervision-form">
				    <thead>
				        <tr>
				            <th></th>
				            <th>Objet</th>
				            <th>Type</th>
				            <th>Utilisateur</th>
				            <th>Date</th>
				            <th>Valeur</th>
				            <th>Etat</th>
				            <th class="column-2-buttons"></th>
				        </tr>
				    </thead>
				    <tbody>
				    	<g:each var="device" in="${ devices }">
					        <tr>
					        	<td><asset:image src="${ device.newDeviceImpl().icon() }" class="device-icon-list"/></td>
					            <td>
					            	<g:link controller="device" action="edit" id="${ device.id }">${ device.label }</g:link>
					            </td>
					            <td>${ device.deviceType.libelle }</td>
					            <td>${ device.user.prenomNom }</td>
					            <td><app:formatTimeAgo date="${ device.dateValue }"/></td>
					            <td>${ device.value }</td>
					            <td>
					            	<g:set var="battery" value="${ device.metavalue('battery') }"/>
					            	<g:set var="signal" value="${ device.metavalue('signal') }"/>
					            	
					            	<g:if test="${ device.agent.online }">
					            		<span class="aui-lozenge aui-lozenge-success">online</span>
					            	</g:if>
					            	<g:else>
					            		<span class="aui-lozenge">offline</span>
					            	</g:else>
					            	<g:if test="${ battery?.value }">
					            		<g:if test="${ battery.value.isDouble() && battery.value.toDouble() <= 2.0 }">
					            			<span class="aui-lozenge aui-lozenge-error">Batterie ${ battery.value }</span>
					            		</g:if>
					            		<g:else>
					            			<span class="aui-lozenge aui-lozenge-success">Batterie ${ battery.value }</span>
					            		</g:else>
					            	</g:if>
					            	<g:if test="${ signal?.value }">
					            		<g:if test="${ signal.value.isDouble() && signal.value.toDouble() == 0.0 }">
					            			<span class="aui-lozenge aui-lozenge-error">Signal ${ signal.value }</span>
					            		</g:if>
					            		<g:if test="${ signal.value.isDouble() && signal.value.toDouble() <= 2.0 }">
					            			<span class="aui-lozenge aui-lozenge-moved">Signal ${ signal.value }</span>
					            		</g:if>
					            		<g:else>
					            			<span class="aui-lozenge aui-lozenge-success">Signal ${ signal.value }</span>
					            		</g:else>
					            	</g:if>
					            </td>
					            <td class="column-2-buttons command-column">
					            	<g:link class="aui-button aui-button-subtle" title="Graphique" controller="device" action="deviceChart" params="['device.id': device.id]">
					            		<span class="aui-icon aui-icon-small aui-iconfont-macro-gallery"></span>
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