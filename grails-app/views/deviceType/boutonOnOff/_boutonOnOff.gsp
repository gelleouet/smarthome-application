<%@ page import="smarthome.automation.DeviceValue"  %>

<g:set var="labelon" value="${ device?.metadata('labelon') }"/>
<g:set var="labeloff" value="${ device?.metadata('labeloff') }"/>
<g:set var="value" value="${  DeviceValue.parseDoubleValue(device.value) }"/>

<g:set var="libelleOn" value="${ labelon?.value ?: 'On' }"/>
<g:set var="libelleOff" value="${ labeloff?.value ?: 'Off' }"/>
<g:set var="libelle" value="${ value == 1 ? libelleOn : libelleOff }"/>

<div>
	<g:formRemote name="form-device-${ device.id }" url="[controller: 'device', action: 'invokeAction']" update="[failure: 'ajaxError']">
		<g:hiddenField name="actionName" value="onOff"/>
		<g:hiddenField name="id" value="${ device.id }"/>
		
		<aui-toggle class="smart-toggle" id="toggle-device-${ device.id }" label="${ libelle }" ${ value == 1 ? 'checked=true' : '' }
			tooltip-on="${ libelleOn }" tooltip-off="${ libelleOff }" form="form-device-${ device.id }"
			data-autosubmit="true"/>
		
	</g:formRemote>
</div>

