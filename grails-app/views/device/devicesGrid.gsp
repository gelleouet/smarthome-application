<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:applyLayout name="applicationContent">
	
		<g:each var="groupe" in="${ deviceInstanceList.groupBy({ it.groupe }).sort{ it.key } }">
			
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
									<g:set var="icon" value="${ device.icon() }"/>
									<g:if test="${icon }">
										<asset:image src="${ icon }" class="device-icon-grid"/>
									</g:if>
								</div>
								<div class="device-grid-body-user">
									<g:render template="${ device.viewGrid() }" model="[device: device]"></g:render>
								</div>
							</div>
						</div>
						<div class="device-grid-body-menu">
							<g:link action="edit" id="${ device.id }" class="aui-button aui-button-subtle" title="Modifier"><span class="aui-icon aui-icon-small aui-iconfont-edit"></span></g:link>
							<g:link action="chart" id="${ device.id }" class="aui-button aui-button-subtle" title="Graphique"><span class="aui-icon aui-icon-small aui-iconfont-macro-gallery"></span></g:link>
						</div>
					</div>
				</div>
			</g:each>
		</g:each>
	
	</g:applyLayout>
</body>
</html>