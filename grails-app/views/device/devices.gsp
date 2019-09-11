<html>
<head>
<meta name='layout' content='main' />
</head>

<body>
	<g:applyLayout name="page-settings" model="[titre: 'Objets connectés', navigation: 'automation']">
	
		<div class="row">
			<div class="col-8">
				<g:form class="form-inline" action="devices">
					<fieldset>
						<input autofocus="true" class="form-control" type="text" placeholder="Nom, groupe ..." name="deviceSearch" value="${ deviceSearch }"/>
						<button class="btn btn-light"><app:icon name="search"/></button>
					</fieldset>
				</g:form>
			</div>
			<div class="col-4 text-right">
				<g:link action="create" class="btn btn-light"><app:icon name="plus"/> Ajouter</g:link>
			</div>
		</div>
		
		<br/>
		
		<app:datatable datatableId="datatable" recordsTotal="${ recordsTotal }">
		    <thead>
		        <tr>
		            <th>Nom</th>
		            <th>Groupe</th>
		            <th>Mac</th>
		            <th>Type</th>
		            <th>Date</th>
		            <th class="column-2-buttons"></th>
		        </tr>
		    </thead>
		    <tbody>
		    	<g:each var="bean" in="${ deviceInstanceList }">
			        <tr>
			            <td>
			            	<g:remoteLink action="favori" id="${ bean.id }" params="[favori: !bean.favori]" title="Favori" onSuccess="favoriteStar('#star-device-${ bean.id }', ${ !bean.favori})">
				            	<g:if test="${ bean.favori }">
				            		<span id="star-device-${ bean.id }" class="star aui-icon aui-icon-small aui-iconfont-star"></span>
				            	</g:if>
				            	<g:else>
				            		<span id="star-device-${ bean.id }" class="aui-icon aui-icon-small aui-iconfont-unstar"></span>
				            	</g:else>
			            	</g:remoteLink>
			            	
			            	<g:link action="edit" id="${bean.id }">
			            		<asset:image src="${ bean.deviceType.newDeviceType().icon() }" class="device-icon-list"/>
			            		${ bean.label }
			            	</g:link>
			            </td>
			            <td>${ bean.tableauBord ? bean.tableauBord + ' / ' : '' } ${ bean.groupe }</td>
			            <td>${ bean.mac }</td>
			            <td>${ bean.deviceType.libelle }</td>
			            <td>${ app.formatTimeAgo(date: bean.dateValue) }</td>
			            <td class="column-2-buttons command-column">
			            	<g:link class="btn btn-light confirm-button" title="Suppimer" action="delete" id="${ bean.id }">
			            		<app:icon name="trash"/>
			            	</g:link>
			            	
			            	<g:remoteLink class="btn btn-light" title="Partager" url="[action: 'dialogDeviceShare', controller: 'deviceShare', id: bean.id]" update="ajaxDialog" onComplete="showDeviceShareDialog()">
			            		<app:icon name="share"/>
			            	</g:remoteLink>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>