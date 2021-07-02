<html>
<head>
<meta name='layout' content='authenticated-chart' />
</head>

<body onload="onLoadSupervision()">

	<g:applyLayout name="applicationHeader">
		<g:form action="supervision" class="aui" name="supervision-form">
			<h3>Supervision multi-utilisateurs</h3>
			
			<div class="aui-group aui-group-split">
				<div class="aui-item">
					<fieldset>
						<g:select name="deviceTypeClass" value="${ command?.deviceTypeClass }" from="${ deviceImpls }"
							optionKey="implClass" optionValue="libelle" class="select" noSelection="['': '']" style="vertical-align: bottom;"/>
						<g:select name="userId" value="${ command?.userId }" from="${ users }"
							optionKey="id" optionValue="prenomNom" class="select" noSelection="['': '']" style="vertical-align: bottom;"/>
						<g:textField name="search" value="${ command?.search }" class="text medium-field" placeholder="Objet..."/>
						<button class="aui-button"><span class="aui-icon aui-icon-small aui-iconfont-search"></span></button>
					</fieldset>
				</div>
				<div class="aui-item">
						<a id="supervision-export-dialog-button" data-url="${ g.createLink(action: 'dialogExport') }" class="aui-button" title="Exporter"><span class="aui-icon aui-icon-small aui-iconfont-export"></span> Exporter</a>
				</div>
			</div>	
		</g:form>
	</g:applyLayout>


	<g:applyLayout name="applicationContent" >
		<div style="overflow-x:auto;">
			<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }" paginateForm="supervision-form">
			    <thead>
			        <tr>
			            <th></th>
			            <th>Objet</th>
			            <th>Type</th>
			            <th>Utilisateur</th>
			            <th>Date</th>
			            <th>Valeur</th>
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
	</g:applyLayout>
	
</body>
</html>