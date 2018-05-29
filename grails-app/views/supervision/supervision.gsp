<html>
<head>
<meta name='layout' content='authenticated-chart' />
</head>

<body>

	<nav class="aui-navgroup aui-navgroup-horizontal">
	    <div class="aui-navgroup-inner">
	        <div class="aui-navgroup-primary">
	            <ul class="aui-nav">
	                <li><g:link action="devicesGrid" controller="device" params="[favori: true]">Favoris</g:link></li>
	                <g:each var="tableauBord" in="${ tableauBords }">
						<li>
							<g:link action="devicesGrid" controller="device" params="[tableauBord: tableauBord]">${ tableauBord }</g:link>
						</li>	                
	                </g:each>
	            </ul>
	        </div><!-- .aui-navgroup-primary -->
	    </div><!-- .aui-navgroup-inner -->
	</nav>


	<g:applyLayout name="applicationHeader">
		<g:form action="supervision" class="aui" name="supervision-form">
			<h3>Supervision multi-utilisateurs</h3>
			
			<div class="aui-group aui-group-split">
				<div class="aui-item">
					<fieldset>
						<g:select name="deviceTypeClass" value="${ command?.deviceTypeClass }" from="${ deviceImpls }"
							optionKey="implClass" optionValue="libelle" class="select" noSelection="['': '']"/>
						<g:select name="userId" value="${ command?.userId }" from="${ users }"
							optionKey="id" optionValue="prenomNom" class="select" noSelection="['': '']"/>
						<button class="aui-button"><span class="aui-icon aui-icon-small aui-iconfont-search"></span></button>
						
						<button id="export-admin-button" value="Exporter" name="_action_exportAdmin" class="hidden"></button>
					</fieldset>
				</div>
				<div class="aui-item">
					<fieldset>
						<g:field type="date" name="dateDebut" class="text medium-field" value="${ app.formatPicker(date: command?.dateDebut) }"/>
						<g:field type="date" name="dateFin" class="text medium-field" value="${ app.formatPicker(date: command?.dateFin) }"/>
						<a class="aui-button" onclick="$('#export-admin-button').click(); return false;" title="Exporter"><span class="aui-icon aui-icon-small aui-iconfont-export"></span> Exporter</a>
					</fieldset>
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
			            <th>Etat</th>
			            <th class="column-2-buttons"></th>
			        </tr>
			    </thead>
			    <tbody>
			    	<g:each var="device" in="${ devices }">
				        <tr>
				        	<td><asset:image src="${ device.newDeviceImpl().icon() }" class="device-icon-list"/></td>
				            <td><g:link controller="device" action="edit" id="${ device.id }">${ device.label }</g:link></td>
				            <td>${ device.deviceType.libelle }</td>
				            <td>${ device.user.prenomNom }</td>
				            <td><app:formatTimeAgo date="${ device.dateValue }"/></td>
				            <td>${ device.value }</td>
				            <td>
				            	<g:set var="battery" value="${ device.metavalue('battery') }"/>
				            	<g:set var="signal" value="${ device.metavalue('signal') }"/>
				            	
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
	</g:applyLayout>
	
</body>
</html>