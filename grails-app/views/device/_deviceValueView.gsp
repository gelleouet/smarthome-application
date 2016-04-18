<g:set var="impl" value="${ value.device.newDeviceImpl() }"/>

<div class="aui-group">
	<div class="aui-item filActualiteIcon">
		<asset:image src="${ impl.icon() }" class="filActualiteIcon"/>
	</div>
	<div class="aui-item">
		<h4>${ value.device.label } <span class="h6-normal">${ value.device.user.prenomNom } - ${ app.formatTimeAgo(date: value.dateValue).replace('Il y a ', '') }</span></h4>
		
		<div class="filActualiteText">
			<g:render template="${ impl.viewFilActualite() }" model="[value: value, impl: impl]"></g:render>
		</div>
		
		<div style="padding-top:4px;">
			<g:render template="/device/deviceToolbar" model="[device: value.device]"></g:render>
		</div>
	</div>
</div>