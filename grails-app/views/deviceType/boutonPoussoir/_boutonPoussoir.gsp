<%@ page import="smarthome.automation.DeviceValue" %>

<g:set var="values" value="${ DeviceValue.doubleValueAggregategByDay(device) }"/>

<div style="display:table;">
	<div style="display:table-cell; padding-right:10px;">
		<g:formRemote name="form-device-${ device.id }" url="[controller: 'device', action: 'invokeAction']" update="[failure: 'ajaxError']">
			<g:hiddenField name="actionName" value="push"/>
			<g:hiddenField name="id" value="${ device.id }"/>
			<g:actionSubmit value="Actionner" class="aui-button confirm-button" />
		</g:formRemote>
	</div>
	<div style="display:table-cell; padding-left:10px;; vertical-align:middle" class="separator-left">
		<span class="aui-badge">${ values?.count ?: 0 }</span>
	</div>
</div>
