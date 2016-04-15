<div class="buttons-container device-toolbar">
	<div class="buttons">
		<g:remoteLink class="aui-button aui-button-subtle" url="[action: 'dialogDeviceShare', controller: 'deviceShare', id: device.id]" update="ajaxDialog" onComplete="showDeviceShareDialog()" title="Partager">
			<span class="aui-icon aui-icon-small aui-iconfont-share"></span>
		</g:remoteLink>
		
		<g:link class="aui-button aui-button-subtle" controller="device" action="edit" id="${ device.id }" title="Modifier">
			<span class="aui-icon aui-icon-small aui-iconfont-edit"></span>
		</g:link>
		
		<g:link class="aui-button aui-button-subtle" controller="device" action="chartView" id="${ device.id }" title="Graphique">
			<span class="aui-icon aui-icon-small aui-iconfont-macro-gallery"></span>
		</g:link>

		<g:link class="aui-button aui-button-subtle" controller="device" action="deviceView" id="${ device.id }" title="Voir">
			<span class="aui-icon aui-icon-small aui-iconfont-view"></span>
		</g:link>
	</div>
</div>