<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationConfigure">
	
		<div class="aui-toolbar2">
		    <div class="aui-toolbar2-inner">
		        <div class="aui-toolbar2-primary">
		            <div>
		                <h3>Objets</h3>
		            </div>		            
		        </div>
		        <div class="aui-toolbar2-secondary">
		        	<g:form >
		            <div class="aui-buttons">
						<g:actionSubmit class="aui-button" value="Ajouter un objet" action="create"/>
		            </div>
		            </g:form>
		        </div>
		    </div><!-- .aui-toolbar-inner -->
		</div>

		
		<h4>
			<g:form class="aui" action="devices">
				<fieldset>
					<input autofocus="true" class="text long-field" type="text" placeholder="Nom, groupe ..." name="deviceSearch" value="${ deviceSearch }"/>
					<button class="aui-button aui-button-subtitle"><span class="aui-icon aui-icon-small aui-iconfont-search"></span></button>
				</fieldset>
			</g:form>
		</h4>
		
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
			            	<g:link class="aui-button aui-button-subtle confirm-button" title="Suppimer" action="delete" id="${ bean.id }">
			            		<span class="aui-icon aui-icon-small aui-iconfont-delete"></span>
			            	</g:link>
			            	
			            	<g:remoteLink class="aui-button aui-button-subtle" title="Partages" url="[action: 'dialogDeviceShare', controller: 'deviceShare', id: bean.id]" update="ajaxDialog" onComplete="showDeviceShareDialog()">
			            		<span class="aui-icon aui-icon-small aui-iconfont-share"></span>
			            	</g:remoteLink>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</app:datatable>
		
	</g:applyLayout>
	
</body>
</html>