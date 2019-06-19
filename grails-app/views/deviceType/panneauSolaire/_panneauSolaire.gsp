<%@ page import="smarthome.automation.DeviceValue" %>


<div class="device-grid-body-content-large">
	<g:set var="surface" value="${  device?.metadata('surface') }"/>
	
	<div class="aui-group" >
		<div class="aui-item">
			<div style="text-align:center; margin-top:10px">
				<g:link controller="device" action="deviceChart" params="['device.id': device.id]">
					<span style="font-size:24px" class="aui-lozenge aui-lozenge-subtle">${ device?.value }Wh</span>
				</g:link>
			</div>
		</div>
		<div class="aui-item separator-left">
			<ul class="label" style="padding-inline-start:5px;">
				<li>Surface : ${ surface?.value }m²</li>
			</ul>
			
		</div>
	</div>
	
</div>