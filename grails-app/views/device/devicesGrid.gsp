<%@ page import="smarthome.core.LayoutUtils" %>

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
	
	
		<g:if test="${ mobileAgent }">
			<g:each var="device" in="${ devices?.sort{ it.label } }">
				<div class="filActualiteItem2">
					<g:render template="deviceView" model="[device: device, user: user]"></g:render>
				</div>
			</g:each>
		</g:if>
		<g:else>
			<g:each var="deviceSplit" in="${ LayoutUtils.splitRow(devices?.sort{ it.label }, 3) }">
				<div class="aui-group">
					<g:each var="device" in="${ deviceSplit }" status="status">
						<div class="aui-item">
							<g:if test="${ device }">
								<div class="filActualiteItem2 smart-draggable" data-draggable-value="${ device.id }">
									<g:render template="deviceView" model="[device: device, user: user]"></g:render>
								</div>
							</g:if>
						</div>
					</g:each>
				</div>
			</g:each>
		</g:else>
	</g:applyLayout>
</body>
</html>