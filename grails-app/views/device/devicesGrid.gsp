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
							<g:link action="edit" id="${ device.id }" title="Modifier">
								<h5 class="device-grid-label">${ device.label }</h5>
							</g:link>
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
									<g:link action="chartView" id="${ device.id }" title="Graphique">
										<asset:image src="${ device.deviceType.newDeviceType().icon() }" class="device-icon-grid"/>
									</g:link>
								</div>
								<div class="device-grid-body-user">
									<g:render template="${ device.deviceType.newDeviceType().viewGrid() }" model="[device: device]"></g:render>
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