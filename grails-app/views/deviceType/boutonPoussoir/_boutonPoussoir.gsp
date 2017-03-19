<%@ page import="smarthome.automation.Device" %>
<%@ page import="smarthome.automation.DeviceValue"  %>

<g:set var="deviceEtatMeta" value="${ device?.metadata('deviceEtat') }"/>

<div style="display:table;">
	<div style="display:table-cell; padding-right:10px;">
		<g:formRemote name="form-device-${ device.id }" url="[action: 'invokeAction', controller: 'device']">
			<g:hiddenField name="actionName" value="push"/>
			<g:hiddenField name="id" value="${ device.id }"/>
			
			<g:if test="${ deviceEtatMeta?.value != null }">
				<g:set var="deviceEtat" value="${ Device.read(deviceEtatMeta?.value) }"/>
				<g:set var="labelon" value="${ device?.metadata('labelon') }"/>
				<g:set var="labeloff" value="${ device?.metadata('labeloff') }"/>
				<g:set var="libelleOn" value="${ labelon?.value ?: 'On' }"/>
				<g:set var="libelleOff" value="${ labeloff?.value ?: 'Off' }"/>
				<g:set var="libelle" value="${ DeviceValue.parseDoubleValue(deviceEtat?.value) == 1 ? libelleOn : libelleOff }"/>
				<aui-toggle class="smart-toggle" id="toggle-device-${ device.id }" label="${ libelle }" ${ deviceEtat?.value as Double == 1 ? 'checked=true' : '' }
					tooltip-on="${ libelleOn }" tooltip-off="${ libelleOff }" form="form-device-${ device.id }"
					data-autosubmit="true" name="value"/>
			</g:if>
			<g:else>
				<g:set var="labelpush" value="${ device?.metadata('labelpush') }"/>
				<g:set var="libelle" value="${ labelpush?.value ?: 'Actionner' }"/>
				
				<button class="aui-button confirm-button">${ libelle }</button>
			</g:else>
			
		</g:formRemote>
	</div>
</div>
