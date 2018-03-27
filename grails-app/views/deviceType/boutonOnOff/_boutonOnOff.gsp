<%@ page import="smarthome.automation.DeviceValue"  %>

<g:set var="labelon" value="${ device?.metadata('labelon') }"/>
<g:set var="labeloff" value="${ device?.metadata('labeloff') }"/>
<g:set var="defaulttimer" value="${ device?.metadata('defaulttimer') }"/>
<g:set var="value" value="${  DeviceValue.parseDoubleValue(device.value) }"/>

<g:set var="libelleOn" value="${ labelon?.value ?: 'On' }"/>
<g:set var="libelleOff" value="${ labeloff?.value ?: 'Off' }"/>
<g:set var="libelle" value="${ value == 1 ? libelleOn : libelleOff }"/>


<g:formRemote class="aui" name="form-device-${ device.id }" url="[controller: 'device', action: 'invokeAction']" update="[failure: 'ajaxError']">
	<g:hiddenField name="actionName" value="onOff"/>
	<g:hiddenField name="id" value="${ device.id }"/>
	
	<div style="display:table">
		<div style="display:table-cell; vertical-align:bottom;">
			<aui-toggle class="smart-toggle" id="toggle-device-${ device.id }" label="${ libelle }" ${ value == 1 ? 'checked=true' : '' }
				tooltip-on="${ libelleOn }" tooltip-off="${ libelleOff }" form="form-device-${ device.id }"
				data-autosubmit="true">
			</aui-toggle>
		</div>
		
		<div style="display:table-cell; padding-left:5px">
			<label>
				Timer 
			</label>
			<g:field type="number" name="params.timer" value="${ value == 0 ? defaulttimer?.value : '' }" class="text short-field" placeholder="min"/>
		</div>
		
		<div style="display:table-cell; padding-left:5px">
			<label>
				Delay
			</label>
			<g:field type="number" name="params.delay" class="text short-field" placeholder="min"/>
		</div>
	</div>
	
</g:formRemote>

