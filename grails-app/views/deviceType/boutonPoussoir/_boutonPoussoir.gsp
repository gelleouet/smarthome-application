<div>
	<g:form controller="device">
		<g:hiddenField name="actionName" value="push"/>
		<g:hiddenField name="id" value="${ device.id }"/>
		<g:actionSubmit value="Actionner" class="aui-button confirm-button" action="invokeAction"/>
		<p class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>
	</g:form>
</div>