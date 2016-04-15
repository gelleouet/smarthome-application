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
	                <li class="${ !params.shared ? 'aui-nav-selected': '' }"><g:link action="devicesGrid1">Personnels <span class="aui-badge">${ myDevices.size() }</span></g:link></li>
	                <li class="${ params.shared ? 'aui-nav-selected': '' }"><g:link action="devicesGrid1" params="[shared: true]">Mes amis <span class="aui-badge">${ sharedDevices.size() }</span></g:link></li>
	            </ul>
	        </div><!-- .aui-navgroup-primary -->
	    </div><!-- .aui-navgroup-inner -->
	</nav>

	<g:applyLayout name="applicationContent">
	
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
			
	
		<g:each var="groupe" in="${ groupeDevices?.sort{ it.key } }" status="statusGroupe">
			
			<div class="separator" style="display:table; width:100%">
				<div style="display:table-cell">
					<h3>${ groupe.key ?: 'Autres' }</h3>
				</div>
				<div style="display:table-cell; text-align:right">
					<a href="#dropdown-groupe${ statusGroupe }" aria-owns="#dropdown-groupe${ statusGroupe }" aria-haspopup="true" class="aui-button aui-button-subtle aui-dropdown2-trigger aui-style-default"><span class="aui-icon aui-icon-small aui-iconfont-more"></span></a>
					<div id="#dropdown-groupe${ statusGroupe }" class="aui-dropdown2 aui-style-default">
						<ul>
							<li><a><span class="aui-icon aui-icon-small aui-iconfont-macro-gallery"></span> Graphique du groupe</a></li>
						</ul>
					</div>
				</div>
			</div>
		
			<g:each var="device" in="${ groupe.value.sort{ it.label } }">
				<div class="device-grid">
					<div class="device-grid-header">
						<div class="device-grid-header-title">
							<h5 class="device-grid-label">
								<g:if test="${ device.user.id == user.id }">
									<g:link style="color:white" action="edit" id="${ device.id }" title="Modifier">${ device.label }</g:link>								
								</g:if>
								<g:else>
									${ device.label }
								</g:else>
							</h5>
						</div>
						<div class="device-grid-header-menu">
							<div>
								<g:if test="${ device.user.id == user.id }">
									<g:remoteLink style="color:white" url="[action: 'dialogDeviceShare', controller: 'deviceShare', id: device.id]" update="ajaxDialog" onComplete="showDeviceShareDialog()" title="Partager">
										<g:if test="${ device.shares.size() }">
											<span class="aui-icon aui-icon-small aui-iconfont-admin-roles"></span>
										</g:if>
										<g:else>
											<span class="aui-icon aui-icon-small aui-iconfont-share"></span>
										</g:else>
									</g:remoteLink>
								</g:if>
							</div>
						</div>
					</div>
					<div class="device-grid-body">
						<div class="device-grid-body-content">
							<div>
								<div class="device-grid-body-icon">
									<g:link action="chartView" id="${ device.id }" title="Graphique">
										<asset:image src="${ device.newDeviceImpl().icon() }" class="device-icon-grid"/>
									</g:link>
								</div>
								<div class="device-grid-body-user">
									<g:render template="${ device.newDeviceImpl().viewGrid() }" model="[device: device, applicationKey: user.applicationKey]"></g:render>
								</div>
							</div>
						</div>
					</div>
				</div>
			</g:each>
		</g:each>
	
	</g:applyLayout>
</body>
</html>