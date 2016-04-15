<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:set var="myDevices" value="${ deviceInstanceList.findAll{ it.user.id == user.id } }"></g:set>
	<g:set var="sharedDevices" value="${ deviceInstanceList.findAll{ it.user.id != user.id } }"></g:set>
	
	<g:set var="allDevices" value="${ params.shared ? sharedDevices : myDevices }"></g:set>
	<g:set var="groupeDevices" value="${ params.shared ? allDevices.groupBy({ it.user.prenomNom }) : allDevices.groupBy({ it.groupe }) }"></g:set>
	
	
	<nav class="aui-navgroup aui-navgroup-horizontal">
	    <div class="aui-navgroup-inner">
	        <div class="aui-navgroup-primary">
	            <ul class="aui-nav">
	                <li class="${ !params.shared ? 'aui-nav-selected': '' }"><g:link action="devicesGrid">Personnels <span class="aui-badge">${ myDevices.size() }</span></g:link></li>
	                <li class="${ params.shared ? 'aui-nav-selected': '' }"><g:link action="devicesGrid" params="[shared: true]">Mes amis <span class="aui-badge">${ sharedDevices.size() }</span></g:link></li>
	            </ul>
	        </div><!-- .aui-navgroup-primary -->
	    </div><!-- .aui-navgroup-inner -->
	</nav>

	<g:applyLayout name="applicationContent" params="[panelContentClass: 'panelContentGrey']">
	
		<g:if test="${ !deviceInstanceList?.size() }">
			<div class="aui-message">
				<h6>Vous n'avez pas encore d'objet enregistré sur votre compte. Vous pouvez :
					<ul>
						<li>Connecter un objet sur un agent pour qu'il soit détecté automatiquement</li>
						<li>Créer manuellement un objet <g:link action="create">en cliquant ici</g:link></li>
					</ul> 
				</h6>
			</div>
		</g:if>
			
	
		<div class="${ mobileAgent ? '' : 'grid-3column' }">
			<g:each var="groupe" in="${ groupeDevices?.sort{ it.key } }" status="statusGroupe">
					<h3 class="titreFilActualite">${ groupe.key ?: 'Autres' }</h3>
					
					<div class="filActualite">
						<g:each var="device" in="${ groupe.value.sort{ it.label } }">
							<div class="filActualiteItem">
								<h4>${ device.label } <span class="h6-normal">${ app.formatTimeAgo(date: device.dateValue) }</span></h4>
								
								<div class="aui-group">
									<div class="aui-item filActualiteIcon">
										<asset:image src="${ device.newDeviceImpl().icon() }" class="device-icon-grid"/>
									</div>
									<div class="aui-item">
										<g:render template="${ device.newDeviceImpl().viewGrid() }" model="[device: device, applicationKey: user.applicationKey]"></g:render>
									</div>
								</div>
								
								<div style="padding-top:4px;">
									<g:render template="deviceToolbar" model="[device: device]"></g:render>
								</div>
							</div>
						</g:each>
					</div>
				
			</g:each>
		</div>
	
	
	</g:applyLayout>
</body>
</html>