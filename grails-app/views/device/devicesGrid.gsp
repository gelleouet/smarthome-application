<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationContent">
	
		<g:if test="${ !deviceInstanceList?.size() }">
			<div class="aui-message">
				<h6>Vous n'avez pas encore de périphériques enregistrés sur votre compte. Vous pouvez :
					<ul>
						<li>Connecter un périphérique sur un agent pour qu'il soit détecté automatiquement</li>
						<li>Créer manuellement un périphérique <g:link action="create">en cliquant ici</g:link></li>
					</ul> 
				</h6>
			</div>
		</g:if>
	
		<g:each var="groupe" in="${ deviceInstanceList?.groupBy({ it.groupe })?.sort{ it.key } }">
			
			<h3 class="separator">${ groupe.key ?: 'Autres' }</h3>
		
			<g:each var="device" in="${ groupe.value.sort{ it.label } }">
				<div class="device-grid">
					<div class="device-grid-header">
						<div class="device-grid-header-title">
							<div>${ device.label }</div>
						</div>
						<div class="device-grid-header-menu">
							<div>
							</div>
						</div>
					</div>
					<div class="device-grid-body">
						<div class="device-grid-body-content">
							<div>
								<div class="device-grid-body-icon">
									<asset:image src="${ device.deviceType.newDeviceType().icon() }" class="device-icon-grid"/>
								</div>
								<div class="device-grid-body-user">
									<g:render template="${ device.deviceType.newDeviceType().viewGrid() }" model="[device: device]"></g:render>
								</div>
							</div>
						</div>
						<div class="device-grid-body-menu">
							<g:link action="edit" id="${ device.id }" class="aui-button aui-button-subtle" title="Modifier"><span class="aui-icon aui-icon-small aui-iconfont-edit"></span></g:link>
							<g:link action="chartView" id="${ device.id }" class="aui-button aui-button-subtle" title="Graphique"><span class="aui-icon aui-icon-small aui-iconfont-macro-gallery"></span></g:link>
						</div>
					</div>
				</div>
			</g:each>
		</g:each>
	
	</g:applyLayout>
</body>
</html>