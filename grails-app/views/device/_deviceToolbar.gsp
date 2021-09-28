<div class="buttons-container device-toolbar">
	<div class="buttons">
		<g:remoteLink class="aui-button aui-button-subtle" url="[action: 'dialogDeviceShare', controller: 'deviceShare', id: device.id]" update="ajaxDialog" onComplete="showDeviceShareDialog()" title="Partager">
			<g:if test="${ device.shares?.size() }">
				<span class="aui-icon aui-icon-small aui-iconfont-group"></span> <span class="aui-badge">${ device.shares?.size() }</span>
			</g:if>
			<g:else>
				<span class="aui-icon aui-icon-small aui-iconfont-share"></span>
			</g:else>
		</g:remoteLink>
		
		<g:link class="aui-button aui-button-subtle" controller="device" action="edit" id="${ device.id }" title="Modifier">
			<span class="aui-icon aui-icon-small aui-iconfont-edit"></span>
		</g:link>
		
		<g:link class="aui-button aui-button-subtle" controller="device" action="deviceChart" params="['device.id': device.id, dateChart: app.formatPicker(date: device.dateValue)]" title="Graphique">
			<span class="aui-icon aui-icon-small aui-iconfont-macro-gallery"></span>
		</g:link>
	</div>
</div>