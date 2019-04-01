<html>
<head>
<meta name='layout' content='authenticated-chart' />
</head>

<body onload="onLoadDeviceChart();">
	<g:applyLayout name="applicationHeader">
		<g:form name="navigation-chart-form" action="deviceChart" class="aui">
			<h3>
			<g:if test="${ command.device.user.id != secUser.id }">
				<g:link action="tableauBordFriend" controller="tableauBord" id="${ command.device.user.id }"> ${ command.device.user.prenomNom } </g:link>
				/ ${ command.device.label } (${ command.device.value })
			</g:if>
			<g:else>
				<g:link style="color:black;" action="edit" controller="device" id="${ command.device.id }"> ${ command.device.label } (${ command.device.value })</g:link> <g:render template="/deviceAlert/deviceAlertLozenge" model="[alert: command.device.lastDeviceAlert()]"/>
			</g:else>
			
			<span class="h6">${ app.formatUserDateTime(date: command.device.dateValue) } - Il y a ${ app.formatTimeAgo(date: command.device.dateValue) }</span>
			</h3>
		
			<div class="aui-group aui-group-split">
				<div class="aui-item">
					<g:hiddenField name="device.id" value="${ command.device.id }"/>
					<g:render template="/chart/chartToolbar"/>
				</div>
				<div class="aui-item">
					
				</div>
			</div>	
		</g:form>
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