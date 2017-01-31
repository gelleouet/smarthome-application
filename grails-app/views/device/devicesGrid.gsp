<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>
	<g:set var="devices" value="${ deviceInstanceList }"></g:set>
	
	<nav class="aui-navgroup aui-navgroup-horizontal">
	    <div class="aui-navgroup-inner">
	        <div class="aui-navgroup-primary">
	            <ul class="aui-nav">
	                <li class="${ search.favori ? 'aui-nav-selected': '' }"><g:link action="devicesGrid" params="[favori: true]">Favoris</g:link></li>
	                <li class="${ search.sharedDevice ? 'aui-nav-selected': '' }"><g:link action="devicesGrid" params="[sharedDevice: true]">Partagés</g:link></li>
	                <g:each var="tableauBord" in="${ tableauBords }">
						<li class="${ search.tableauBord == tableauBord ? 'aui-nav-selected': '' } smart-droppable" data-droppable-value="${ tableauBord }" data-ondrop="onDropDeviceToTableauBord" data-ondrop-url="${ g.createLink(controller: 'device', action: 'moveToTableauBord') }">
							<g:link action="devicesGrid" params="[tableauBord: tableauBord]">${ tableauBord }</g:link>
						</li>	                
	                </g:each>
	            </ul>
	        </div><!-- .aui-navgroup-primary -->
	    </div><!-- .aui-navgroup-inner -->
	</nav>

	<g:applyLayout name="applicationContent" params="[panelContentClass: 'panelContentGrey']">
	
		<div class="${ mobileAgent ? '' : 'grid-3column' }">
			<g:each var="groupe" in="${ devices?.groupBy({ it.groupe })?.sort{ it.key } }" status="statusGroupe">
					<div class="filActualiteTitre smart-droppable" data-droppable-value="${ groupe.key }" data-ondrop="onDropDeviceToGroupe" data-ondrop-url="${ g.createLink(controller: 'device', action: 'moveToGroupe') }">
						<h3>${ groupe.key ?: 'Autres' }</h3>
					</div>
					
					<div class="filActualite">
						<g:each var="device" in="${ groupe.value.sort{ it.label } }">
							<div class="filActualiteItem smart-draggable" data-draggable-value="${ device.id }">
								<g:render template="deviceView" model="[device: device, user: user]"></g:render>
							</div>
						</g:each>
					</div>
				
			</g:each>
		</div>
	
	
	</g:applyLayout>
</body>
</html>