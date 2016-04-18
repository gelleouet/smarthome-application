<h4>${ device.label } <span class="h6-normal"> ${ device.user.prenomNom } - ${ app.formatTimeAgo(date: device.dateValue) }</span>
</h4>
								
<div class="aui-group">
	<div class="aui-item filActualiteIcon">
		<asset:image src="${ device.newDeviceImpl().icon() }" class="device-icon-grid"/>
	</div>
	<div class="aui-item">
		<g:render template="${ device.newDeviceImpl().viewGrid() }" model="[device: device]"></g:render>
	</div>
</div>

<div style="padding-top:4px; width:100%">
	<g:render template="deviceToolbar" model="[device: device]"></g:render>
</div>
