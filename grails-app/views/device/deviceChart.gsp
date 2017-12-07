<html>
<head>
<meta name='layout' content='authenticated-chart' />
</head>

<body onload="onLoadDeviceChart();">
	<nav class="aui-navgroup aui-navgroup-horizontal">
	    <div class="aui-navgroup-inner">
	        <div class="aui-navgroup-primary">
	            <ul class="aui-nav">
	                <li><g:link action="devicesGrid" controller="device" params="[favori: true]">Favoris</g:link></li>
	                <li><g:link action="devicesGrid" controller="device" params="[sharedDevice: true]">Partagés</g:link></li>
	                <g:each var="tableauBord" in="${ tableauBords }">
						<li class="${ command.device.tableauBord == tableauBord ? 'aui-nav-selected': '' }">
							<g:link action="devicesGrid" controller="device" params="[tableauBord: tableauBord]">${ tableauBord }</g:link>
						</li>	                
	                </g:each>
	            </ul>
	        </div><!-- .aui-navgroup-primary -->
	    </div><!-- .aui-navgroup-inner -->
	</nav>


	<g:applyLayout name="applicationHeader">
		<h3><g:link style="color:black;" action="edit" controller="device" id="${ command.device.id }"> ${ command.device.label } (${ command.device.value })</g:link> <g:render template="/deviceAlert/deviceAlertLozenge" model="[alert: command.device.lastDeviceAlert()]"/>
		<span class="h6">${ app.formatUserDateTime(date: command.device.dateValue) } - Il y a ${ app.formatTimeAgo(date: command.device.dateValue) }</span>
		</h3>
		
		<div class="aui-group aui-group-split">
			<div class="aui-item">
				<g:form name="navigation-chart-form" action="deviceChart">
					<g:hiddenField name="device.id" value="${ command.device.id }"/>
					<g:render template="/chart/chartToolbar"/>
				</g:form>
			</div>
			<div class="aui-item">
				<g:remoteLink class="aui-button" url="[action: 'dialogAddDeviceValue', id: command.device.id]" update="ajaxDialog"
					onSuccess="showAddDeviceValueDialog()">
					<span class="aui-icon aui-icon-small aui-iconfont-add"></span> Ajouter valeur
				</g:remoteLink>
			</div>
		</div>	
	</g:applyLayout>


	<g:applyLayout name="applicationContent">
		<g:if test="${ command.deviceImpl.viewChart() }">
			<g:render template="${ command.deviceImpl.viewChart() }"/>	
		</g:if>
		<g:else>
			<g:render template="deviceChart"/>
		</g:else>
	</g:applyLayout>
	
</body>
</html>