<div>
	<g:set var="url" value="${ g.createLink(controller: 'device', action: 'invokeAction') }"/>
	<g:set var="formId" value="form-device-${ device.id }"/>
	
	<g:form name="${ formId }">
		<g:hiddenField name="actionName" value="push"/>
		<g:hiddenField name="id" value="${ device.id }"/>
		<p class="aui-buttons">
			<a class="aui-button ajax-invoke-action-button" title="Fermer" data-action-name="close" data-url="${ url }" data-form-id="${ formId }"><span class="aui-icon aui-icon-small aui-iconfont-arrows-down"></span></a>
			<a class="aui-button ajax-invoke-action-button" title="Ouvrir" data-action-name="open" data-url="${ url }" data-form-id="${ formId }"><span class="aui-icon aui-icon-small aui-iconfont-arrows-up"></span></a>
		</p>
		<input name="value" class="ajax-invoke-action-button" type="range" min="0" max="99" step="24" data-url="${ url }" data-action-name="level" data-form-id="${ formId }" value="${ device.value }"/>
		<p class="h6">${ app.formatTimeAgo(date: device.dateValue) }</p>
	</g:form>
</div>