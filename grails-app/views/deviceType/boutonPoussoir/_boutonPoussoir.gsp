<div>
	<g:formRemote name="form-device-${ device.id }" url="[controller: 'device', action: 'invokeAction']" update="[failure: 'ajaxError']">
		<g:hiddenField name="actionName" value="push"/>
		<g:hiddenField name="id" value="${ device.id }"/>
		<g:actionSubmit value="Actionner" class="aui-button confirm-button" />
		<p class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>
	</g:formRemote>
</div>