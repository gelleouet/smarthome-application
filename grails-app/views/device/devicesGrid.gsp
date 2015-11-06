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
	
		<g:each var="groupe" in="${ deviceInstanceList?.groupBy({ it.groupe })?.sort{ it.key } }" status="statusGroupe">
			
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
										<asset:image src="${ device.newDeviceImpl().icon() }" class="device-icon-grid"/>
									</g:link>
									
									<a href="#dropdown-${device.id }" aria-owns="#dropdown-${device.id }" aria-haspopup="true" class="aui-button aui-button-subtle aui-dropdown2-trigger aui-style-default"><span class="aui-icon aui-icon-small aui-iconfont-more"></span></a>
									<div id="#dropdown-${device.id }" class="aui-dropdown2 aui-style-default">
										<ul>
											<li><g:link action="edit" id="${ device.id }" ><span class="aui-icon aui-icon-small aui-iconfont-edit"></span> Modifier</g:link></li>
											<li><g:link action="chartView" id="${ device.id }" ><span class="aui-icon aui-icon-small aui-iconfont-macro-gallery"></span> Graphique</g:link></li>
										</ul>
									</div>
								</div>
								<div class="device-grid-body-user">
									<g:render template="${ device.newDeviceImpl().viewGrid() }" model="[device: device]"></g:render>
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