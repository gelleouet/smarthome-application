<%@ page import="smarthome.automation.Device" %>
<%@ page import="smarthome.automation.DeviceValue"  %>

<g:set var="deviceEtatMeta" value="${ device?.metadata('deviceEtat') }"/>
<g:set var="defaulttimer" value="${ device?.metadata('defaulttimer') }"/>


<div style="display:table;">
	<div style="display:table-cell; padding-right:10px;">
		<g:formRemote class="aui" name="form-device-${ device.id }" url="[action: 'invokeAction', controller: 'device']">
			<g:hiddenField name="actionName" value="onOff"/>
			<g:hiddenField name="id" value="${ device.id }"/>
			
			<div style="display:table">
				<div style="display:table-cell; vertical-align:bottom;">
					<g:if test="${ deviceEtatMeta?.value != null }">
						<g:set var="deviceEtat" value="${ Device.read(deviceEtatMeta?.value) }"/>
						<g:set var="libelle" value="${ DeviceValue.parseDoubleValue(deviceEtat?.value) == 1 ? libelleOn : libelleOff }"/>
						<aui-toggle class="smart-toggle" id="toggle-device-${ device.id }" label="${ libelle }" ${ deviceEtat?.value as Double == 1 ? 'checked=true' : '' }
							tooltip-on="Ouvrir" tooltip-off="Fermer" form="form-device-${ device.id }"
							data-autosubmit="true" name="value"/>
					</g:if>
					<g:else>
						<button class="aui-button confirm-button">Actionner</button>
					</g:else>
				</div>
				
				<div style="display:table-cell; padding-left:5px">
					<label>
						Timer 
					</label>
					<g:field type="number" name="params.timer" value="${ defaulttimer?.value }" class="text short-field" placeholder="min"/>
				</div>
				
				<div style="display:table-cell; padding-left:5px">
					<label>
						Delay
					</label>
					<g:field type="number" name="params.delay" class="text short-field" placeholder="min"/>
				</div>
			</div>
			
			
		</g:formRemote>
	</div>
</div>
